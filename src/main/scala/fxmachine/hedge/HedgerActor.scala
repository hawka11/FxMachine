package fxmachine.hedge

import java.lang.System._
import java.util.Random

import akka.actor.{Actor, Props}
import fxmachine.domain.Side.Side
import fxmachine.domain.{CcyPair, ExecutionReport, Side}
import fxmachine.position.PositionMessage
import org.slf4j.LoggerFactory

class HedgerActor(randomHedgeLimit: Long) extends Actor {

  private val logger = LoggerFactory.getLogger(HedgerActor.this.getClass)
  private val positionSupervisor = context.actorSelection("//Main/user/app/positionSupervisor")

  override def preStart(): Unit = {

  }

  override def receive = {
    case msg: HedgeMessage =>
      if (msg.amount > randomHedgeLimit) sell(msg)
      else if (msg.amount < (randomHedgeLimit * -1)) buy(msg)
      else logger.info(s"Nothing to hedge ${msg.ccyPair} ${msg.amount}")
  }

  private def sell(msg: HedgeMessage) = buySell(msg, "Sell", Side.ASK)

  private def buy(msg: HedgeMessage) = buySell(msg, "Buy", Side.BID)

  private def buySell(msg: HedgeMessage, buySell: String, side: Side) = {
    logger.info(s"Random is: $randomHedgeLimit")
    logger.info(s"$buySell ${msg.ccyPair} ${msg.amount}")
    Thread.sleep(20)
    positionSupervisor ! PositionMessage(ExecutionReport(nanoTime(), msg.ccyPair, side, msg.ccyPair.term, 1.55, Math.abs(msg.amount)))
  }
}

object HedgerActor {
  private val random = new Random(444)

  def props(): Props = Props(new HedgerActor(Math.abs(random.nextLong() % 4000)))
}

case class HedgeMessage(timestamp: Long, ccyPair: CcyPair, amount: Double)
