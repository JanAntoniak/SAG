package pl.sag.microservice

import akka.actor.{Actor, ActorLogging, ActorRef, Props, ReceiveTimeout}
import akka.routing.ConsistentHashingRouter.ConsistentHashableEnvelope
import akka.routing.FromConfig

import scala.collection.mutable
import scala.concurrent.duration._
import scala.language.postfixOps

//This will be my masterWorker that will delegate text processing to Workers

//#service
class MainWorker extends Actor with ActorLogging {

  private val workerRouter: ActorRef = context.actorOf(FromConfig.props(Props[WorkerActor]), name = "workerRouter")

  private val numberOfWorkers = 5

  def receive: Receive = {
    case getProductsRequest: GetProductsRequest =>
      val replyTo = sender()
      val numberOfResults = getProductsRequest.resultAmount
      val aggregator = context.actorOf(Props(
        classOf[ResultsAggregator], numberOfWorkers, numberOfResults, replyTo))
      (1 to numberOfWorkers) foreach { _ =>
        workerRouter.tell(
          ConsistentHashableEnvelope(getProductsRequest, getProductsRequest), aggregator)
      }
    case other =>
      log.error(s"Got message: $other, which I don't understand.")
      DontUnderstand(other)
  }
}
//#service

class ResultsAggregator(numberOfWorkers: Int, numberOfResults: Int, replyTo: ActorRef) extends Actor {

  private val results = mutable.ArrayBuffer[ProductsWithCosDist]()
  context.setReceiveTimeout(20 seconds)

  def receive: Receive = {
    case products: ProductsWithCosDist =>
      results += products
      if (results.size == numberOfWorkers) {
        replyTo ! transformResults(results.toList)
        context.stop(self)
      }
    case ReceiveTimeout =>
      replyTo ! transformResults(results.toList)
      context.stop(self)
  }

  def transformResults(resList: List[ProductsWithCosDist]): ProductsResponse = {
    ProductsResponse(resList.reduce(_ ++ _).products.take(numberOfResults).map(_._id.toString))
  }
}
