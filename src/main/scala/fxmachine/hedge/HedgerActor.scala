package fxmachine.hedge

import java.lang.System._
import java.util.Random

import akka.actor.{Actor, Props}
import fxmachine.domain.{CcyPair, ExecutionReport, Side}
import fxmachine.position.PositionMessage
import org.slf4j.LoggerFactory

class HedgerActor(randomHedgeLimit: Long) extends Actor {

  private val logger = LoggerFactory.getLogger(HedgerActor.this.getClass)
  private val simulator = context.actorSelection("//Main/user/app")

  override def preStart(): Unit = {
    logger.info(s"Random is: $randomHedgeLimit")
  }

  override def receive = {
    case msg: HedgeMessage =>
      if (msg.amount > randomHedgeLimit) sell(msg)
      else if (msg.amount < (randomHedgeLimit * -1)) logger.info(s"Buy ${msg.ccyPair} ${msg.amount}")
      else logger.info(s"Nothing to hedge ${msg.ccyPair} ${msg.amount}")
  }

  def sell(msg: HedgeMessage): Unit = {
    logger.info(s"Sell ${msg.ccyPair} ${msg.amount}")
    simulator ! PositionMessage(ExecutionReport(nanoTime(), msg.ccyPair, Side.BID, msg.ccyPair.term, 1.55, msg.amount))
  }
}

object HedgerActor {
  private val random = new Random(444)

  def props(): Props = Props(new HedgerActor(Math.abs(random.nextLong() % 4000)))
}

case class HedgeMessage(timestamp: Long, ccyPair: CcyPair, amount: Double)
