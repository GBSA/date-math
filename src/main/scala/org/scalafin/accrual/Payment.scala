package org.scalafin.accrual

import org.joda.time.DateMidnight
import org.scalafin.convention.Iso4217Currency
import java.util.Calendar

case class Payment(var amount: Double, val currency: Iso4217Currency, val paymentDate: DateMidnight) {
  def hasSameDateAndCurrency(payment: Payment): Boolean = {
    paymentDate == payment.paymentDate && currency == payment.currency
  }

  def addSimilarPaymentsTogether(listPayments: List[Payment]): (Boolean, Double) = {
    var returnAmount: Double = 0
    for (payment <- listPayments) {
      if (hasSameDateAndCurrency(payment))
        returnAmount += payment.amount
    }
    if (returnAmount != 0) {
      (true, amount + returnAmount)
    } else {
      (false, returnAmount)
    }
  }
}
