package pl.sag

import akka.actor.{Actor, ActorLogging}

import scala.util.Random

class WorkerActor
  extends Actor
    with ActorLogging
    with TextProcessingUtils
    with ProductFetcher {

  private val model: Products = fetchProducts(100)

  def randomProduct(i: Int): Product = {
    Product(Random.alphanumeric.take(5).toString, ProductId(Random.nextInt), Random.alphanumeric.take(10).toString)
  }

  override def receive: Receive = {
    case productsRequest: GetProductsRequest =>
      sender() ! findMostSimilarProducts(model, productsRequest.product, productsRequest.resultAmount)
    case _ => LogicError("Yo, here an Error :D")
  }

}