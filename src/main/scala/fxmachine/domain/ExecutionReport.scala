package fxmachine.domain

import fxmachine.domain.Side.Side

case class ExecutionReport(
                            timestamp: Long,
                            ccyPair: CcyPair,
                            side: Side,
                            dealt: Ccy,
                            rate: Double,
                            dealtAmount: Double
                          )
