package org.scalafin.date.daycount.impl

import org.scalafin.date.daycount.DaycountCalculator
import com.github.nscala_time.time.Imports._
import org.joda.time.DateMidnight
import org.scalafin.date.DateRange
import scalaz.Failure
import scalaz.Success
import scalaz.Validation
import org.scalafin.date.DateRangeException

class Actual366 extends DaycountCalculator {
  def calculateDaycountFraction(dateRange: DateRange, periodRange: Option[DateRange] = None): Double = {
    daysBetween(dateRange.startDate, dateRange.endDate) / 366.0d
  }
}