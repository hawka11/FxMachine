package fxmachine

object Main {

  def main(args: Array[String]): Unit = {
    akka.Main.main(Array(classOf[ExecutedReportSimulatorActor].getName))
  }
}