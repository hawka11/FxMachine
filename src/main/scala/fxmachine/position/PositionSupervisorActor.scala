package fxmachine.position

import akka.actor.{Actor, ActorRef, Props}
import fxmachine.domain.{CcyPair, ExecutionReport}

case class RegisteredCcyPairPosition(ref: ActorRef)

class PositionSupervisorActor extends Actor {

  private var positions = Map[CcyPair, RegisteredCcyPairPosition]()

  override def preStart(): Unit = {
    //TODO: CcyPairPosition should self register (started distributed)
    positions += (CcyPair("AUDUSD") -> RegisteredCcyPairPosition(context.actorOf(Props[CcyPairPositionActor], "audUsdPosition")))
    positions += (CcyPair("AUDGBP") -> RegisteredCcyPairPosition(context.actorOf(Props[CcyPairPositionActor], "audGbpPosition")))
    positions += (CcyPair("AUDCNY") -> RegisteredCcyPairPosition(context.actorOf(Props[CcyPairPositionActor], "audCnyPosition")))
  }

  def receive = {
    case PositionMessage(msg: ExecutionReport) =>
      if (positions.contains(msg.ccyPair)) positions(msg.ccyPair).ref ! PositionMessage(msg)
      else println(s"No such position with ccy [${msg.ccyPair}], ignoring")
  }
}

case class PositionMessage(msg: Any)