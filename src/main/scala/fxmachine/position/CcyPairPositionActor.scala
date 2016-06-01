package fxmachine.position

import akka.actor.{Actor, ActorRef}
import fxmachine.domain.{CcyPair, ExecutionReport, Side}
import fxmachine.hedge.{HedgeMessage, HedgerActor}
import org.slf4j.LoggerFactory

class CcyPairPositionActor() extends Actor {

  private val logger = LoggerFactory.getLogger(CcyPairPositionActor.this.getClass)

  private val hedger: ActorRef = context.actorOf(HedgerActor.props(), "audUsdPosition")

  override def receive = position(0)

  private def position(amount: Double): Receive = {
    case PositionMessage(msg: ExecutionReport) =>
      msg match {
        case ExecutionReport(_, _, Side.ASK, _, _, amt) => applyPosition(msg, amount + amt)
        case ExecutionReport(_, _, Side.BID, _, _, amt) => applyPosition(msg, amount - amt)
      }
  }

  private def applyPosition(msg: ExecutionReport, newAmount: Double): Unit = {
    logger.info(s"position time: ${(System.nanoTime() - msg.timestamp) / 1000}")

    hedger ! HedgeMessage(msg.timestamp, msg.ccyPair, newAmount)

    context.become(position(newAmount))
  }
}
