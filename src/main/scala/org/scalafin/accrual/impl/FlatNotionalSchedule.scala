package org.scalafin.accrual.impl

import org.joda.time.DateMidnight
import org.scalafin.convention.Iso4217Currency
import org.scalafin.accrual.Notional
import org.scalafin.accrual.NotionalPeriod
import org.scalafin.accrual.NotionalSchedule
import org.scalafin.date.DateRange

class FlatNotionalSchedule(val amount: Double, val currency: Iso4217Currency) extends NotionalSchedule {

  def getNotionalPeriodsBetween(dateRange: DateRange): List[NotionalPeriod] = {
    val notionalPeriod = new NotionalPeriod(dateRange, notional, amount, currency, None)
    List(notionalPeriod)
  }

  val notional: Notional = new Notional(amount, currency)
}