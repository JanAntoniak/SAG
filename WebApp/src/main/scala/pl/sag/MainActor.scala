package pl.sag

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
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

  implicit val timeout: Timeout = Timeout(5 seconds)

  val workers: List[ActorRef] = (1 to numberOfChildren).toList.map(createActor)

  def createActor(i: Int): ActorRef = context.actorOf(Props[WorkerActor], s"workerNo.$i")

  override def receive: Receive = {
    case productRequest: GetProductsRequest =>
      val products = workers.map(_ ? productRequest).map(_.map(_.asInstanceOf[Products]))
      sequence(products).map(_.reduce(_ ++ _)).map { products =>
        findMostSimilarProducts(products, productRequest.product, productRequest.resultAmount)
      } pipeTo sender()

    case KillAndDie =>
      workers foreach (_ ! PoisonPill)
      context stop self
  }
}
