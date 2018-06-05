package pl.sag.microservice

import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.cluster.singleton.{ClusterSingletonManager, ClusterSingletonManagerSettings, ClusterSingletonProxy, ClusterSingletonProxySettings}
import com.typesafe.config.ConfigFactory


object NodeCreator {

  def mainTest(args: Array[String]): Unit = {
    nodeStartup(List("2551", "2552", "0"))
    HttpEndpointBoot.main(Array.empty)
  }

  def main(args: Array[String]): Unit = args.toList match {
    case _::help::Nil if help == "help" =>
      println(helpMsg)
    case _::test::Nil if test == "test" =>
      mainTest(Array.empty)
    case _::http::Nil if http == "http" =>
      HttpEndpointBoot.main(Array.empty)
    case _::lstArg =>
      nodeStartup(lstArg)
  }

  def nodeStartup(ports: List[String]): Unit = {
    ports foreach { port =>
      val config =
        ConfigFactory.parseString(s"""
          akka.remote.netty.tcp.port=$port
          akka.remote.artery.canonical.port=$port
          """).withFallback(
          ConfigFactory.parseString("akka.cluster.roles = [worker]")).
          withFallback(ConfigFactory.load("application"))

      val system = ActorSystem("ClusterSystem", config)

      system.actorOf(ClusterSingletonManager.props(
        singletonProps = Props[MainWorker],
        terminationMessage = PoisonPill,
        settings = ClusterSingletonManagerSettings(system).withRole("worker")),
        name = "mainWorker")

      system.actorOf(ClusterSingletonProxy.props(singletonManagerPath = "/user/mainWorker",
        settings = ClusterSingletonProxySettings(system).withRole("worker")),
        name = "textProcessingServiceProxy")
    }
  }

  val helpMsg: String =
    """
      |"You should specify port/ports on which worker node/nodes will work or put 0 if you don't care about it.
      | Example usage: sbt "run pl.sag.microservice.NodeCreation 2551 2552 0"
      | This command creates three nodes in the same JVM and run them on ports 2551, 2552 and third on any available.
      | If remote node if preferred you should run commands in separated terminals.
    """.stripMargin
}
