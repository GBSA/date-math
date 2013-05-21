package org.scalafin.date.daycount.impl

import com.github.nscala_time.time.Imports._
import org.joda.time.DateMidnight
import org.scalafin.date.daycount.DaycountCalculator
import org.scalafin.date.holiday.HolidayCalendar
import org.scalafin.date.DateRange
import scalaz.Validation
import org.scalafin.date.DateRangeException
import scalaz.Success
import scalaz.Failure

class Business252(hc: HolidayCalendar) extends DaycountCalculator {
  def calculateDaycountFraction(dateRange: DateRange, periodRange: Option[DateRange] = None): Double = {
    val businessDay = hc.getBusinessDaysBetween(dateRange.startDate, dateRange.endDate)
    businessDay / 252.0d
  }
}