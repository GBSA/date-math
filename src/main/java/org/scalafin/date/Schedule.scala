package org.scalafin.date

import scalaz.Validation
import scalaz.Success
import scalaz.Failure
import scala.math.Ordering.Implicits._
import org.scalafin.date.util.DateMidnightOrdering
import org.joda.time.DateMidnight
import org.scalafin.date.DatePeriodOpsFunc._

class Schedule[+T <: DatePeriod] private (val periods: Seq[T])(implicit periodSplitter: DatePeriodSplitter[T]) {

  def cutSchedule[A <: DatePeriod](cutTemplate: Seq[A]): Schedule[T] = {
    val splitDates: Seq[DateMidnight] = cutTemplate flatMap { _.dateRange.extremesAsSeq }
    cutScheduleByDates(splitDates)
  }

  def cutScheduleByDates(splitDates: Seq[DateMidnight]): Schedule[T] = {
    val splittedPeriods = periods flatMap { _.cut(splitDates) }
    new Schedule(splittedPeriods)
  }

}

object Schedule {
  def apply[T <: DatePeriod](dates: Seq[T])(implicit periodSplitter: DatePeriodSplitter[T]): Validation[InvalidDateSequenceException, Schedule[T]] = {
    import DateMidnightOrdering._
    val overlappingPeriods = for {
      period1 <- dates
      period2 <- dates if (period1 != period2 && period1.dateRange.endDate > period2.dateRange.startDate)
    } yield (period1, period2)

    if (overlappingPeriods.isEmpty)
      Success(new Schedule(dates))
    else
      Failure(new InvalidDateSequenceException(overlappingPeriods, "Cannot create the schedule with overlapping periods"))
  }

}

class InvalidDateSequenceException(val overlappingPeriods: Traversable[(DatePeriod, DatePeriod)], message: String) extends Exception(message)