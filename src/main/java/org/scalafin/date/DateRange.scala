package org.scalafin.date

import org.joda.time.DateMidnight
import com.github.nscala_time.time.Imports._
import scalaz.Success
import scalaz.Validation
import scalaz.Failure

class DateRange private (val startDate: DateMidnight, val endDate: DateMidnight) {

  lazy val asDays = DateRangeOps.asDayStream(startDate, endDate)

  def copyAndModify(newStart: Option[DateMidnight] = None, newEnd: Option[DateMidnight] = None): Validation[DateRangeException, DateRange] = {
    DateRange(newStart.getOrElse(startDate), newEnd.getOrElse(endDate))
  }

  def splitInArrear(previousDateBuilder: (DateMidnight) => DateMidnight): Seq[DateRange] = {
    val previousDate = previousDateBuilder(endDate)
    if (previousDate < startDate)
      Seq(this)
    else {
      val currentRange = new DateRange(previousDate, endDate)
      val previousRange = new DateRange(startDate, previousDate)
      currentRange +: (previousRange splitInArrear previousDateBuilder)
    }

  }

  lazy val extremesAsSeq: Seq[DateMidnight] = Seq(startDate, endDate)
}

object DateRange {
  def apply(startDate: DateMidnight, endDate: DateMidnight): Validation[DateRangeException, DateRange] = {
    if (startDate > endDate) {
      Failure(new DateRangeException(startDate, endDate, "StartDate is after EndDate!"))
    } else {
      Success(new DateRange(startDate, endDate))
    }
  }
}

class DateRangeException(date1: DateMidnight, date2: DateMidnight, message: String) extends Exception(message)

object DateRangeOps {
  def asDayStream(start: DateMidnight, end: DateMidnight): Stream[DateMidnight] = {
    if (start > end)
      Stream.empty[DateMidnight]
    else
      Stream.cons(start, asDayStream(start plusDays 1, end))
  }

}