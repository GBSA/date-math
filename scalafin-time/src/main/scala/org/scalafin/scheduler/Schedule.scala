package org.scalafin.scheduler

import org.scalafin.datemath.PaymentPeriod


case class Schedule[A](periods:Stream[PaymentPeriod[A]]){

}

//class Schedule[A,B <: Interval[A]] private (val periods: Seq[T]) extends IntervalSplitter {
//
//  def cutSchedule[A <: Period](cutTemplate: Seq[A]): Schedule[T] = {
//    val splitDates = cutTemplate flatMap { _.dateRange.extremesAsSeq }
//    cutScheduleByDates(splitDates)
//  }
//
//  def cutScheduleByDates(splitDates: Seq[DateMidnight]): Schedule[T] = {
//    val splittedPeriods = periods flatMap { _.cut(splitDates) }
//    new Schedule(splittedPeriods)
//  }
//
//}
//
//object Schedule {
//
//  def apply[A,B <: Interval[A]](intervals: Seq[T])(implicit periodSplitter: IntervalSplitter[T]): Validation[InvalidDateSequenceException, Schedule[T]] = {
//    import org.scalafin.date.JodaTimeOrderingInstances._
//    val overlappingPeriods = for {
//      period1 <- dates
//      period2 <- dates if period1 != period2 && period1.dateRange.endDate > period2.dateRange.startDate
//    } yield (period1, period2)
//
//    if (overlappingPeriods.isEmpty)
//      Success(new Schedule(dates))
//    else
//      Failure(new InvalidDateSequenceException(overlappingPeriods, "Cannot create the schedule with overlapping periods"))
//  }
//
//}
//
//class InvalidDateSequenceException(val overlappingPeriods: Traversable[(Period, Period)], message: String) extends Exception(message)