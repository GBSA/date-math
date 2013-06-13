package org.scalafin.accrual

import org.scalafin.date.DatePeriod
import org.joda.time.DateMidnight
import org.scalafin.convention.Iso4217Currency
import org.scalafin.date.DateRange

case class NotionalPeriod(
  dateRange: DateRange,
  notional: Notional,
  amount: Double,
  currency: Iso4217Currency,
  originalDateRange: Option[DateRange])
  extends DatePeriod