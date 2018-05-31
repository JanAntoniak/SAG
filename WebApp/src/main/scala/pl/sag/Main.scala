package pl.sag

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

object Main {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("systemActor")
    val mainActor = system.actorOf(MainActor.props(5), "mainActor")
    implicit val timeout: Timeout = Timeout(5 seconds)
    val product = Product("Product name", ProductId(1), "Product description")
    val result = (mainActor ? GetProductsRequest(product)).map(_.asInstanceOf[Products])
    result map println
    mainActor ! KillAndDie
    system terminate
  }
}