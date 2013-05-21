package org.scalafin.date.daycount.impl

import com.github.nscala_time.time.Imports._
import org.joda.time.DateMidnight
import org.scalafin.date.daycount.DaycountCalculator
import org.scalafin.date.DateRange
import scalaz.Validation
import org.scalafin.date.DateRangeException
import scalaz.Failure
import org.joda.time.DateTimeConstants

//TODO REVIEW
class US30360 extends DaycountCalculator {
  def calculateDaycountFraction(dateRange: DateRange, periodRange: Option[DateRange] = None): Double = {
    val ((year1, month1, day1), (year2, month2, day2)) = explodeDateRange(dateRange)
    val isStartDateLstDayOfFebruary = isLastDayOfFebruary(dateRange.startDate)
    val isEndDateLstDayOfFebruary = isLastDayOfFebruary(dateRange.endDate)

    var correctedDay1 = day1
    var correctedDay2 = day2

    if (isStartDateLstDayOfFebruary && isEndDateLstDayOfFebruary) {
      correctedDay2 = 30
    }

    if (isStartDateLstDayOfFebruary) {
      correctedDay1 = 30
    }

    if (correctedDay2 == 31 && correctedDay1 >= 30) {
      correctedDay2 = 30
    }

    if (correctedDay1 == 31) {
      correctedDay1 = 30
    }

    val numerator = 360 * (year2 - year1) + 30 * (correctedDay2 - correctedDay1) + (day2 - day1)

    numerator / 360d
  }

  def explodeDateRange(dateRange: DateRange): ((Int, Int, Int), (Int, Int, Int)) = {
    (explodeDate(dateRange.startDate), explodeDate(dateRange.endDate))
  }

  def explodeDate(date: DateMidnight): (Int, Int, Int) = {
    (date.yearOfEra.get, date.monthOfYear.get, date.dayOfMonth.get)
  }

  def isLastDayOfFebruary(date: DateMidnight): Boolean = {
    date.getMonthOfYear == date.dayOfMonth.maximumValue && date.monthOfYear == DateTimeConstants.FEBRUARY
  }
}
