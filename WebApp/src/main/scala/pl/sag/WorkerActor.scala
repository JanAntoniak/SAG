package pl.sag

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.pipe

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import scala.math.random

class WorkerActor(fetcher: ProductFetcher)
  extends Actor
    with ActorLogging
    with TextProcessingUtils {

  private val model: Future[Products] = fetcher.fetchProducts(100)

  override def receive: Receive = {
    case productsRequest: GetProductsRequest =>
      findMostSimilarProducts(model, productsRequest.product, productsRequest.resultAmount) pipeTo sender
    case ex =>
      log.error(s"An error occured in $self: $ex")
      sender ! Future.successful(LogicError(s"Error in $self: $ex"))
  }
}

object WorkerActor {
  def props(fetcher: ProductFetcher): Props = Props(new WorkerActor(fetcher))
}