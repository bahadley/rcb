package rcb.tpcdi

import akka.actor._
import akka.stream.actor.{ActorPublisher}
import ActorPublisherMessage._


case class TaxRate(tx_id: String, tx_name: String, tx_rate: Double)


class Producer extends ActorPublisher[TaxRate] with ActorLogging {

  val rnd = new java.util.Random()

  def receive = {

    case Request(cnt) => 
      log.debug("Received request ({}) from subscriber", cnt)
      sendTaxRates()
    case Cancel => 
      log.info("Cancel message received -- stopping")
      context.stop(self)
    case _ =>
  }

  def sendTaxRates() {
    while(isActive && totalDemand > 0) {
      onNext(nextTaxRate())
    }
  }

  def nextTaxRate(): TaxRate = {
    TaxRate("US1", 
      "U.S. Income Tax Bracket for the poor", 
      rnd.nextDouble() * 100)    
  }   
}
