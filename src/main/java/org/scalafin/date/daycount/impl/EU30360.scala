package org.scalafin.date.daycount.impl

import com.github.nscala_time.time.Imports._
import org.joda.time.DateMidnight
import org.scalafin.date.daycount.DaycountCalculator
import org.scalafin.date.DateRange
import scalaz.Validation
import org.scalafin.date.DateRangeException
import scalaz.Success
import scalaz.Failure

// TODO review
class EU30360 extends DaycountCalculator {
  def calculateDaycountFraction(dateRange: DateRange, periodRange: Option[DateRange] = None): Double = {

    val ((year1, month1, day1), (year2, month2, day2)) = explodeDateRange(dateRange)
    //    if (dayOfMonth1 == 31) {
    //      dayOfMonth1 = 30
    //    }
    //    if (dayOfMonth2 == 31) {
    //      dayOfMonth2 = 30
    //    }
    var numerator = 360 * (year2 - year1)
    numerator += 30 * (month2 - month1)
    numerator += day2 - day1
    if (day1 == 31) numerator += 1
    if (day2 == 31) numerator -= 1

    numerator / 360d
  }

  def explodeDateRange(dateRange: DateRange): ((Int, Int, Int), (Int, Int, Int)) = {
    (explodeDate(dateRange.startDate), explodeDate(dateRange.endDate))
  }

  def explodeDate(date: DateMidnight): (Int, Int, Int) = {
    (date.yearOfEra.get, date.monthOfYear.get, date.dayOfMonth.get)
  }
}