package org.scalafin.date.daycount

import com.github.nscala_time.time.Imports._
import org.scalafin.date.DatePeriod
import org.joda.time.DateMidnight
import scalaz.Validation
import org.scalafin.date.DateRangeException
import org.scalafin.date.DateRange

trait DaycountCalculator {
  def calculateDaycountFraction(dateRange: DateRange, periodRange: Option[DateRange] = None): Double
  def calculateDaycountFraction(period: DatePeriod): Double = calculateDaycountFraction(period.dateRange, period.originalDateRange)
  def daysBetween(d1: DateMidnight, d2: DateMidnight): Long = Math.round((d1 to d2).millis / (1000d * 60d * 60d * 24d))
}