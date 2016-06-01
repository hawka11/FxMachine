package fxmachine.domain

case class CcyPair(base: Ccy, term: Ccy) {
}

object CcyPair {
  private var cache = Map[String, CcyPair]()

  def apply(ccyPair: String): CcyPair = {
    if(!cache.contains(ccyPair)) cache += (ccyPair -> new CcyPair(Ccy(ccyPair.substring(0, 3)), Ccy(ccyPair.substring(3, 6))))

    cache(ccyPair)
  }
}
