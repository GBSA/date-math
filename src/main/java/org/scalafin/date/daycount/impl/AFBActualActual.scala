package org.scalafin.date.daycount.impl

import com.github.nscala_time.time.Imports._
import org.scalafin.date.daycount.DaycountCalculator
import org.joda.time.DateMidnight
import org.scalafin.date.DateRange

class AFBActualActual extends DaycountCalculator {

  def previousDateBuilder(date: DateMidnight) = date minus (date.dayOfYear.maximumValue)

  def february29InYear(year: Int): Option[DateMidnight] = {
    val dateMidnight = new DateMidnight(year, 1, 1)
    if (dateMidnight.dayOfYear.maximumValue == 366)
      Some(dateMidnight.withMonthOfYear(2).withDayOfMonth(29))
    else
      None
  }

  def diy(dateRange: DateRange): Double = {
    val dates = dateRange extremesAsSeq
    val februaries29 = dates.flatMap {
      date => february29InYear(date.year.get)
    }.filter(february29 => february29 >= dateRange.startDate && february29 <= dateRange.endDate)
    if (februaries29.isEmpty)
      365.0
    else
      366.0
  }

  def tau(dateRange: DateRange) = daysBetween(dateRange.startDate, dateRange.endDate) / diy(dateRange)

  def calculateDaycountFraction(dateRange: DateRange, periodRange: Option[DateRange] = None): Double = {
    val splittedPeriods = dateRange splitInArrear previousDateBuilder
    val splittedTaus = splittedPeriods map tau
    splittedTaus.sum
  }

}