package fxmachine.domain

case class Ccy(ccy: String) {

  object Ccy {
    private var cache = Map[String, Ccy]()

    def apply(ccy: String): Ccy = {
      if (!cache.contains(ccy)) cache += (ccy -> new Ccy(ccy))

      cache(ccy)
    }
  }

}