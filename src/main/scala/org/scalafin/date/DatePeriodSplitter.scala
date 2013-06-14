package org.scalafin.date

import org.joda.time.DateMidnight
import scalaz.Lens
import scalaz.Validation
import scalaz.Scalaz._

trait DatePeriodSplitter[T <: DatePeriod] {

  def split(period: T, date: DateMidnight): Option[(T, T)] = {
    val (p1, p2) = splitNative(period, date)
    (for {
      period1 <- p1
      period2 <- p2
    } yield (period1, period2)).toOption
  }

  def splitNative(period: T, date: DateMidnight): (Validation[DateRangeException, T], Validation[DateRangeException, T])

}

trait DatePeriodOps[T <: DatePeriod] {

  def split(date: DateMidnight): Option[(T, T)]

  def cut(dates: Seq[DateMidnight]): Seq[T] = {
    val result = for {
      date <- dates
      splitPeriod <- split(date)
    } yield Seq(splitPeriod._1, splitPeriod._2)
    result.flatten
  }

}

trait DatePeriodOpsFunc {
  implicit def toDatePeriodOps[T <: DatePeriod](t: T)(implicit splitter: DatePeriodSplitter[T]) = new DatePeriodOps[T] {
    def split(date: DateMidnight): Option[(T, T)] = splitter.split(t, date)
  }
}

object DatePeriodOpsFunc extends DatePeriodOpsFunc

object DatePeriodSplitter {

  implicit val lensSimplePeriod = Lens.lensu[SimplePeriod, DateRange]((period, dateRange) => period.copy(dateRange = dateRange), _.dateRange)

  implicit def splitter[T <: DatePeriod](implicit lens: Lens[T, DateRange]): DatePeriodSplitter[T] = new DatePeriodSplitter[T] {
    def splitNative(period: T, date: DateMidnight): (Validation[DateRangeException, T], Validation[DateRangeException, T]) = {
      val period1 = period.dateRange.copyAndModify(newEnd = Some(date)) map (lens.set(period, _))
      val period2 = period.dateRange.copyAndModify(newStart = Some(date)) map (lens.set(period, _))
      (period1, period2)
    }
  }

}