package com.gottexbrokers.datemath

import org.joda.time._
import scalaz.Validation
import scala.annotation.tailrec
import com.gottexbrokers.datemath.scheduler.Schedule


/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 13/03/14
 * Time: 12:53
 *
 */




trait HolidayCalendar {

	def isWeekend(date: ReadableDateTime): Boolean
	def isHoliday(date: ReadableDateTime): Boolean

}

object HolidayCalendar {

	val WeekendOnlyHolidayCalendar:HolidayCalendar = new HolidayCalendar {

		override def isWeekend(date: ReadableDateTime): Boolean =  date.getDayOfWeek == DateTimeConstants.SATURDAY || date.getDayOfWeek == DateTimeConstants.SUNDAY

		override def isHoliday(date: ReadableDateTime): Boolean = false

	}

}



trait Frequency {

	self =>

	def period: ReadablePeriod

	private implicit def toDateTime(readableDateTime:ReadableDateTime) = readableDateTime.toDateTime

	def addTo(date: ReadableDateTime): ReadableDateTime = date plus period

	def subtractFrom(date:ReadableDateTime):ReadableDateTime = date minus period

	def divide(factor:Int):Frequency =   new Frequency {

		override def period: ReadablePeriod =  self.period.toPeriod multipliedBy factor

	}

}

object Frequency {

	def apply(period1:ReadablePeriod) = new Frequency {
		override def period: ReadablePeriod = period1
	}
}

trait ExactFitInYear {

	def periodsPerYear: Int

}

trait BusinessDayConvention {

	def adjust(dt:ReadableDateTime)(implicit hc:HolidayCalendar):ReadableDateTime

}

trait UnadjustedBusinessDayConvention extends BusinessDayConvention{

	override final def adjust(dt: ReadableDateTime)(implicit hc: HolidayCalendar): DateTime = dt.toDateTime

}

case object UnadjustedBusinessDayConvention extends UnadjustedBusinessDayConvention

trait DayCountConvention{

	def calculator: DayCountCalculator

}

trait DayCountCalculator {

	def calculateDayCountFraction(start:ReadableDateTime, end:ReadableDateTime): Double


	def apply(start:ReadableDateTime, end:ReadableDateTime): Double = calculateDayCountFraction(start,end)

	def apply[A<:ReadableDateTime](period:Period[A]):Double = apply(period.start,period.end)

}

trait StubType


class SchedulingImpossibleException(message:String, cause:Option[Throwable] = None) extends Exception(message,cause getOrElse null)

trait Scheduler {


	def schedule(frequency:Frequency, start: ReadableDateTime, end: ReadableDateTime):Validation[SchedulingImpossibleException, Schedule]

}

trait Period[+A]{

	def start:A

	def end:A

	import Ordering.Implicits._

	def contains[B>:A](point: B)(implicit ordering:Ordering[B]): Boolean = start < point && end > point

}


/**
 * A generic time period typically resulting out of a schedule operation. Since schedule might result in stubbing,
 * the reference start and reference end are kept so they could be used at a later stage
 * @param start
 * @param end
 * @tparam A
 */
case class TimePeriod[+A<:ReadableDateTime](start:A, end:A, referenceStart:A, referenceEnd:A) extends Period[A]{

	import com.gottexbrokers.datemath.utils.OrderingImplicits._


	def splitInArrear[B>:A<:ReadableDateTime](f: (B) => B): Seq[TimePeriod[B]] = {
		@tailrec
		def splitInArrear(currentInterval:TimePeriod[B], currentSplits:Seq[TimePeriod[B]]):Seq[TimePeriod[B]] = {
			val previousInstant = f(currentInterval.end)
			if (previousInstant <= start)
				currentInterval.copy(referenceStart = previousInstant) +: currentSplits
			else {
				val lastPartOfCurrentInterval = TimePeriod[B] (previousInstant, end,previousInstant,end)
				val firstPartOfCurrentInterval = TimePeriod[B] (start, previousInstant,start,previousInstant)
				splitInArrear(firstPartOfCurrentInterval, lastPartOfCurrentInterval +: currentSplits)
			}
		}
		splitInArrear(this, Seq.empty[TimePeriod[B]])
	}


}


