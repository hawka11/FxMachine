package fxmachine

import java.lang.System.nanoTime

import akka.actor.{Actor, Props}
import fxmachine.domain.{Ccy, CcyPair, ExecutionReport, Side}
import fxmachine.position.{PositionMessage, PositionSupervisorActor}
import org.slf4j.LoggerFactory

class ExecutedReportSimulatorActor extends Actor {

  private val logger = LoggerFactory.getLogger(ExecutedReportSimulatorActor.this.getClass)
  private val positionSupervisor = context.actorOf(Props[PositionSupervisorActor], "positionSupervisor")
  private var simCounter = 0

  override def preStart() = {
    logger.info(s"path is: ${this.self.path}")
    simulate
  }

  private def simulate() = {
    if (simCounter == 0) self ! PositionMessage(ExecutionReport(nanoTime(), CcyPair("AUDUSD"), Side.ASK, Ccy("USD"), 1.55, 800))
    if (simCounter == 1) self ! PositionMessage(ExecutionReport(nanoTime(), CcyPair("AUDGBP"), Side.ASK, Ccy("GBP"), 1.55, 1000))
    if (simCounter == 2) self ! PositionMessage(ExecutionReport(nanoTime(), CcyPair("AUDCNY"), Side.ASK, Ccy("CNY"), 1.55, 1200))
    if (simCounter == 3) self ! PositionMessage(ExecutionReport(nanoTime(), CcyPair("AUDUSD"), Side.BID, Ccy("USD"), 1.55, 300))
    if (simCounter == 4) self ! PositionMessage(ExecutionReport(nanoTime(), CcyPair("AUDGBP"), Side.BID, Ccy("GBP"), 1.55, 400))
    if (simCounter == 5) self ! PositionMessage(ExecutionReport(nanoTime(), CcyPair("AUDCNY"), Side.BID, Ccy("CNY"), 1.55, 420))

    simCounter = (simCounter + 1) % 6
  }

  override def receive: Receive = {
    case msg: PositionMessage => {
      positionSupervisor ! msg
      Thread.sleep(20)
      simulate
    }
  }
}
