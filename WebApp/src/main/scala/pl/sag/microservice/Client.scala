package pl.sag.microservice

import java.util.concurrent.ThreadLocalRandom

import akka.actor.{Actor, ActorRef, ActorSystem, Address, Props, RelativeActorPath, RootActorPath}
import akka.cluster.ClusterEvent._
import akka.cluster.{Cluster, MemberStatus}

class Client(servicePath: String) extends Actor {
  val cluster = Cluster(context.system)
  private val servicePathElements = servicePath match {
    case RelativeActorPath(elements) => elements
    case _ => throw new IllegalArgumentException(
      "servicePath [%s] is not a valid relative actor path" format servicePath)
  }
  var nodes = Set.empty[Address]

  override def preStart(): Unit = cluster.subscribe(self, classOf[MemberEvent], classOf[ReachabilityEvent])
  override def postStop(): Unit = {
    cluster.unsubscribe(self)
  }

  def receive: Receive = {
    case request: GetProductsRequest if nodes.nonEmpty =>
      // choose anyone
      val address = nodes.toIndexedSeq(ThreadLocalRandom.current.nextInt(nodes.size))
      val service = context.actorSelection(RootActorPath(address) / servicePathElements)
      service forward request
    case state: CurrentClusterState =>
      nodes = state.members.collect {
        case m if m.hasRole("worker") && m.status == MemberStatus.Up => m.address
      }
    case MemberUp(m) if m.hasRole("worker")        => nodes += m.address
    case other: MemberEvent                         => nodes -= other.member.address
    case UnreachableMember(m)                       => nodes -= m.address
    case ReachableMember(m) if m.hasRole("worker") => nodes += m.address
  }
}

object ClientFactory {
  def masterActor: ActorRef = {
    val system = ActorSystem("ClusterSystem")
    val actor = system.actorOf(Props(classOf[Client], "/user/textProcessingServiceProxy"), "client")
    actor
  }
}

