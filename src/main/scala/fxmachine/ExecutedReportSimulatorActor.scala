package fxmachine

import java.lang.System.nanoTime

import akka.actor.{Actor, Props}
import fxmachine.domain.{Ccy, CcyPair, ExecutionReport, Side}
import fxmachine.position.{PositionMessage, PositionSupervisorActor}

class ExecutedReportSimulatorActor extends Actor {

  override def preStart(): Unit = {
    val positionSupervisor = context.actorOf(Props[PositionSupervisorActor], "positionSupervisor")

    while (true) {
      positionSupervisor ! PositionMessage(ExecutionReport(nanoTime(), CcyPair("AUDUSD"), Side.ASK, Ccy("USD"), 1000, 1.55))
      positionSupervisor ! PositionMessage(ExecutionReport(nanoTime(), CcyPair("AUDGBP"), Side.ASK, Ccy("GBP"), 1000, 1.55))
      positionSupervisor ! PositionMessage(ExecutionReport(nanoTime(), CcyPair("AUDCNY"), Side.ASK, Ccy("CNY"), 1000, 1.55))
      positionSupervisor ! PositionMessage(ExecutionReport(nanoTime(), CcyPair("AUDUSD"), Side.ASK, Ccy("USD"), 1000, 1.55))
      positionSupervisor ! PositionMessage(ExecutionReport(nanoTime(), CcyPair("AUDGBP"), Side.ASK, Ccy("GBP"), 1000, 1.55))
      positionSupervisor ! PositionMessage(ExecutionReport(nanoTime(), CcyPair("AUDCNY"), Side.ASK, Ccy("CNY"), 1000, 1.55))

      Thread.sleep(20)
    }
  }

  def receive: Receive = {
    case msg: Any => println(msg)
  }
}
