package org.scalafin.date.daycount.impl

import org.scalafin.date.daycount.DaycountCalculator
import org.joda.time.DateMidnight
import org.scalafin.date.DatePeriod
import com.github.nscala_time.time.Imports._
import scalaz.Validation
import org.scalafin.date.DateRangeException
import scalaz.Failure
import scalaz.Success
import org.scalafin.date.DateRange

class Actual360 extends DaycountCalculator {
  def calculateDaycountFraction(dateRange: DateRange, periodRange: Option[DateRange] = None): Double = {
    daysBetween(dateRange.startDate, dateRange.endDate) / 360.0d
  }
}