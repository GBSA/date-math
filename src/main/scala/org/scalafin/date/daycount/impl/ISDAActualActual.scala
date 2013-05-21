package org.scalafin.date.daycount.impl

import com.github.nscala_time.time.Imports._
import org.joda.time.DateMidnight
import org.scalafin.date.daycount.DaycountCalculator
import org.scalafin.date.DateRange

//TODO very similar to AFBActualActual
class ISDAActualActual extends DaycountCalculator {

  def previousDateBuilder(date: DateMidnight) = date minus (date.dayOfYear.get)
  def tau(dateRange: DateRange) = daysBetween(dateRange.startDate, dateRange.endDate) / dateRange.startDate.dayOfYear.getMaximumValue

  def calculateDaycountFraction(dateRange: DateRange, periodRange: Option[DateRange] = None): Double = {
    val splittedPeriods = dateRange splitInArrear previousDateBuilder
    val taus = splittedPeriods map tau
    taus.sum
  }
}