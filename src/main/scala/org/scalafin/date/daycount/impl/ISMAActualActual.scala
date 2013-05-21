package org.scalafin.date.daycount.impl

import com.github.nscala_time.time.Imports._
import org.joda.time.DateMidnight
import org.scalafin.date.daycount.DaycountCalculator
import org.scalafin.date.DateRange
import scalaz.Validation
import org.scalafin.date.DateRangeException
import scalaz.Success
import scalaz.syntax.semigroup._
import scalaz.std.list._
import scalaz.ValidationFunctions
import scalaz.Scalaz._
import scalaz.Failure
// TODO REVIEW
// also called ICMA Actual/Actual
// Factor = (date2 - date1) / [Freq x (date3 - date1)]
class ISMAActualActual extends DaycountCalculator {
  def calculateDaycountFraction(dateRange: DateRange, periodRange: Option[DateRange] = None): Double = {
    var refStart = if (periodRange == None) dateRange.startDate else periodRange.get.startDate
    var refEnd = if (periodRange == None) dateRange.endDate else periodRange.get.endDate

    val monthsEstimate = daysBetween(refStart, refEnd) * (12 / 365)
    var months = Math.round(monthsEstimate).toInt

    if (months == 0) {
      refStart = dateRange.startDate
      refEnd = dateRange.startDate plusYears 1
      months = 12
    }

    val period: Double = months / 12

    if (dateRange.endDate <= refEnd) {
      if (dateRange.startDate >= refStart) {
        period * daysBetween(dateRange.startDate, dateRange.endDate) * daysBetween(refStart, refEnd)
      } else {
        if (dateRange.endDate > refStart) {
          val x = calculateDaycountFraction(dateRange, dateRange.copyAndModify(newEnd = Some(refStart)).toOption)
          val y = dateRange.copyAndModify(newStart = Some(refStart)).map(calculateDaycountFraction(_, dateRange.copyAndModify(Some(refStart), Some(refEnd)).toOption))
          (y.map(x + _)).toOption.get
        } else {
          calculateDaycountFraction(dateRange, dateRange.copyAndModify(newEnd = Some(refStart)).toOption)
        }
      }
    } else {
      var sum = dateRange.copyAndModify(newEnd = Some(refEnd)).map(calculateDaycountFraction(_, dateRange.copyAndModify(Some(refStart), Some(refEnd)).toOption))
      var i = 0
      var newRefStart = DateMidnight.now
      var newRefEnd = DateMidnight.now
      do {
        newRefStart = refEnd.plusMonths(months * i)
        newRefEnd = refEnd.plusMonths(months * (i + 1))
        if (dateRange.endDate >= newRefEnd) sum = sum.map(_ + period)
        i += 1
      } while (dateRange.endDate < newRefEnd)
      val secondSum = dateRange.copyAndModify(newStart = Some(newRefStart)).map(calculateDaycountFraction(_, dateRange.copyAndModify(Some(newRefStart), Some(newRefEnd)).toOption))
      sum.toOption.get + secondSum.toOption.get
    }
  }
}
