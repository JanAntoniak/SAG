package pl.sag

import akka.actor.{Actor, ActorLogging}

import scala.util.Random

class WorkerActor extends Actor with ActorLogging {

  def randomProduct(i: Int): Product = {
    Product(Random.alphanumeric.take(5).toString, ProductId(Random.nextInt), Random.alphanumeric.take(10).toString)
  }

  def findMostSimilarProducts(product: Product): Products = {
    Products((1 to 3).toList.map(randomProduct))
  }

  override def receive: Receive = {
    case productsRequest: GetProductsRequest =>
      sender() ! findMostSimilarProducts(productsRequest.product)
    case _ => LogicError("Yo, here an Error :D")
  }
}
