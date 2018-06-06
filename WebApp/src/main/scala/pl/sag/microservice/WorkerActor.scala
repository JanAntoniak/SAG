package pl.sag.microservice

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import pl.sag.microservice.utils.{ProductFetcherImpl, TextProcessingUtils}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Success, Try}

//#worker
class WorkerActor
  extends Actor
    with ActorLogging
    with TextProcessingUtils {

  private lazy val model: Future[Products] =
    Model.model.map(list => Products(scala.util.Random.shuffle(list.products).take(200)))

  override def receive: Receive = {
    case productsRequest: GetProductsRequest =>
      log.info(s"$self got request: $productsRequest")
      Try {
          findMostSimilarProducts(model, productsRequest.product, productsRequest.resultAmount) pipeTo sender()
      }.recoverWith {
        case ex =>
          log.error(s"An error occurred: $ex in actor $self")
          Success(ProductsWithCosDist(Nil))
      }
    case ex =>
      log.error(s"An error occurred in $self: $ex")
      sender() ! DontUnderstand(ex)
  }

  override def preStart(): Unit = println(f"\n\nUp and running worker actor is listening at ${self.path} \n\n")

}
//#worker

object Model {
  val model: Future[Products] = {
    new ProductFetcherImpl("database").fetchProducts(5000)
  }
}