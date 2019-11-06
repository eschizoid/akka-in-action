package aia.tesdriven

import akka.actor.{Actor, ActorRef}

object SilentActor {
  case class SilentMessage(data: String)
  case class GetState(receiver: ActorRef)
}

class SilentActor extends Actor {
  import SilentActor._
  var internalState: Vector[String] = Vector[String]()

  override def receive: PartialFunction[Any, Unit] = {
    case SilentMessage(data) => internalState = internalState :+ data
    case GetState(receiver)  => receiver ! internalState
  }

  def state: Vector[String] = internalState
}
