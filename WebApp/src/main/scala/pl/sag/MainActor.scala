package pl.sag

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.Future.sequence
import scala.concurrent.duration._
import scala.language.postfixOps

object MainActor {
  def props(numberOfChildren: Int): Props = Props(new MainActor(numberOfChildren))
}

class MainActor(val numberOfChildren: Int)
  extends Actor
    with ActorLogging
    with TextProcessingUtils {

  val fetcher: ProductFetcher = new ProductFetcherImpl("database")

  implicit val timeout: Timeout = Timeout(60 seconds)

  val workers: List[ActorRef] = (1 to numberOfChildren).toList.map(createActor)

  def createActor(i: Int): ActorRef = context.actorOf(WorkerActor.props(fetcher), s"workerNo.$i")

  override def receive: Receive = {
    case productRequest: GetProductsRequest =>
      val products = workers.map(_ ? productRequest).map(_.map(_.asInstanceOf[Products]))

      val resultProducts = sequence(products).map(_.reduce(_ ++ _)).flatMap { products =>
        findMostSimilarProducts(Future.successful(products), productRequest.product, productRequest.resultAmount)
      }

      resultProducts pipeTo sender

    case KillAndDie =>
      workers foreach (_ ! PoisonPill)
      context.system.terminate pipeTo sender
  }
}
