package pl.sag

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.io.StdIn

object Server extends Directives with JsonSupport {

  def main(args: Array[String]) {

    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher
    val main = new Main()
    val route =
      post {
        path("") {
          entity(as[GetProductsRequest]) { requestBody =>
            val products: Future[ProductsResponse] = main.findBestFittingProducts(requestBody)
            onComplete(products) { doneProducts =>
              complete(doneProducts)
            }
          }
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ =>
        main.cleanUp.map {_ =>
          system.terminate()
        }
    )
  }
}