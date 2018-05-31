package pl.sag

import akka.actor.{ActorSystem, Terminated}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps

class Main extends Logger {

  private val system = ActorSystem("systemActor")
  private val mainActor = system.actorOf(MainActor.props(50), "mainActor")
  private implicit val timeout: Timeout = Timeout(60 seconds)

  def findBestFittingProducts(productsRequest: GetProductsRequest): Future[ProductsResponse] = {
    val result = (mainActor ? productsRequest).map(_.asInstanceOf[ProductsWithCosDist])
    result.map { products =>
      info(s"Returining products: $products")
    }
    result.map { products =>
      ProductsResponse(products.products.map { product =>
        ProductResponse(product._id.toString, product.imageURL)
      })
    }
  }

  def cleanUp = {
    for {
      terminationResult <- (mainActor ? KillAndDie).map(_.asInstanceOf[Terminated])
    } yield terminationResult
  }
}