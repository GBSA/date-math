package org.scalafin.date.daycount.impl

import com.github.nscala_time.time.Imports._
import org.joda.time.DateMidnight
import org.scalafin.date.daycount.DaycountCalculator
import org.scalafin.date.DateRange
import scalaz.Validation
import org.scalafin.date.DateRangeException
import scalaz.Failure
import org.joda.time.DateTimeConstants

//TODO verify with official formula
class IT30360 extends DaycountCalculator {
  def calculateDaycountFraction(dateRange: DateRange, periodRange: Option[DateRange] = None): Double = {
    val ((year1, month1, day1), (year2, month2, day2)) = explodeDateRange(dateRange)

    var correctedDay1 = day1
    var correctedDay2 = day2

    if (isAfter27OfFebruary(dateRange.startDate)) {
      correctedDay1 = 30
    }

    if (isAfter27OfFebruary(dateRange.endDate)) {
      correctedDay2 = 30
    }

    (year2 - year2) + ((30 * (month2 - month2 - 1) + Math.max(0, 30 - correctedDay1) + Math.min(30, correctedDay2)) / 360.0d)
  }

  def explodeDateRange(dateRange: DateRange): ((Int, Int, Int), (Int, Int, Int)) = {
    (explodeDate(dateRange.startDate), explodeDate(dateRange.endDate))
  }

  def explodeDate(date: DateMidnight): (Int, Int, Int) = {
    (date.getYearOfEra, date.getMonthOfYear, date.getDayOfMonth)
  }

  def isAfter27OfFebruary(date: DateMidnight): Boolean = {
    date.getMonthOfYear > 27 && date.monthOfYear == DateTimeConstants.FEBRUARY
  }
}
