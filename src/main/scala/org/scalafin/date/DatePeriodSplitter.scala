package org.scalafin.date

import org.joda.time.DateMidnight
import scalaz.{ Success, Lens, Validation }
import Ordering.Implicits._
import DateOrdering._
import scala.annotation.tailrec
import scala.collection.immutable.TreeSet
import scalaz.Failure

trait DatePeriodSplitter[T <: DatePeriod] {

  def split(period: T, date: DateMidnight): Option[(T, T)] = {
    splitNative(period, date).toOption
  }

  def splitNative(period: T, date: DateMidnight): Validation[InvalidSplitDateException, (T, T)]

  def cut(period: T, dates: Seq[DateMidnight]): Seq[T] = {
    @tailrec
    def splitPeriods(period: T, currentDate: DateMidnight, dateSplit: Iterator[DateMidnight], previousPeriods: Seq[T]): Seq[T] = {
      val (splittedPeriods) = split(period, currentDate)
      if (!dateSplit.hasNext)
        previousPeriods ++ splittedPeriods.map {
          tuple => Seq(tuple._1, tuple._2)
        }.getOrElse(Seq.empty[T])
      else if (splittedPeriods.isEmpty)
        previousPeriods
      else {
        val nextDate = dateSplit.next()
        val (period1, period2) = splittedPeriods.get
        val (toSplit, toKeep) = if (period1.dateRange.endDate > nextDate) (period1, period2) else (period2, period1)
        splitPeriods(toSplit, nextDate, dateSplit, previousPeriods :+ toKeep)
      }
    }
    if (dates.isEmpty)
      Seq(period)
    else {
      val iterator = dates.sorted.iterator
      splitPeriods(period, iterator.next(), iterator, Seq.empty[T])
    }
  }
}

trait DatePeriodOps[T <: DatePeriod] {
  def splitter: DatePeriodSplitter[T]
  def self: T
  def split(date: DateMidnight): Option[(T, T)] = splitter.split(self, date)
  def cut(dates: Seq[DateMidnight]): Seq[T] = splitter.cut(self, dates)
}

trait DatePeriodOpsFunc {
  implicit def toDatePeriodOps[T <: DatePeriod](t: T)(implicit splitter0: DatePeriodSplitter[T]) = new DatePeriodOps[T] {
    val self = t
    val splitter = splitter0
  }
}

object DatePeriodOpsFunc extends DatePeriodOpsFunc

object DatePeriodSplitter {
  implicit val lensSimplePeriod = Lens.lensu[SimplePeriod, DateRange]((period, dateRange) => period.copy(dateRange = dateRange), _.dateRange)

  implicit def splitter[T <: DatePeriod](implicit lens: Lens[T, DateRange]): DatePeriodSplitter[T] = new DatePeriodSplitter[T] {
    def splitNative(period: T, date: DateMidnight): Validation[InvalidSplitDateException, (T, T)] = {
      val period1 = period.dateRange.copyAndModify(newEnd = Some(date)) map (lens.set(period, _))
      val period2 = period.dateRange.copyAndModify(newStart = Some(date)) map (lens.set(period, _))
      val tuple = for {
        p1 <- period1
        p2 <- period2
      } yield (p1, p2)
      tuple match {
        case Failure(e) => Failure(new InvalidSplitDateException("Can't split into periods for: " + date + " due to " + e))
        case Success(s) => Success(s)
      }
    }
  }
  
}

class InvalidSplitDateException(val message: String) extends Exception(message)