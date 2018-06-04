package pl.sag.microservice

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import pl.sag.microservice.utils.JsonSupport
import scala.concurrent.duration._
import scala.language.postfixOps

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn

object HttpEndpointBoot extends Directives with JsonSupport {

  def main(args: Array[String]) {

    implicit val system: ActorSystem = ActorSystem(name="sagWebApp", config=ConfigFactory.load("applicationSupervisor"))
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher
    implicit val timeout: Timeout = 20 seconds
    val masterActor = ClientFactory.masterActor
    val route =
      post {
        path("") {
          entity(as[GetProductsRequest]) { requestBody =>
            val products: Future[ProductsResponse] = (masterActor ? requestBody).map(_.asInstanceOf[ProductsResponse])
            onComplete(products) { doneProducts =>
              complete(doneProducts)
            }
          }
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ =>
      {
        //          main.killActors
        system.terminate()
      }
      )
  }
}
