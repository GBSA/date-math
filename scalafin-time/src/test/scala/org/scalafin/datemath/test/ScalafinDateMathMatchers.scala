package org.scalafin.datemath.test

import org.specs2.text.Sentences
import org.joda.time._
import org.specs2.matcher.{MustMatchers, MatchResult, Expectable, Matcher}
import java.util.Date
import org.scalafin.datemath.{PaymentPeriod, DayCountCalculator, HolidayCalendar, BusinessDayConvention}
import com.mbc.jfin.holiday.{BusinessDayConvention => JFinBusinessDayConvention, HolidayCalendar => MbcHolidaycalendar, DateAdjustmentService}
import com.mbc.jfin.daycount.impl.{DaycountCalculator => JFinDaycountCalculator}
import com.mbc.jfin.schedule.SchedulePeriod
import org.scalafin.datemath.utils.{OrderingImplicits, Generifiers}
import org.scalafin.utils.Interval

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 19/02/14
 * Time: 13:29
 *
 */

//TODO: check naming conventions
//TODO: add scalarestyle maven plugin
trait ScalafinDateMathMatchers extends JodaTimeDateMatchers
                                       with BusinessDayConventionTupleMatchers
                                       with DayCountConventionTupleMatchers {


}

trait JodaTimeDateMatchers extends Sentences {

	def beEquivalentTo(readableDateTime: ReadableDateTime): Matcher[ReadablePartial] = new Matcher[ReadablePartial] {
		override def apply[S <: ReadablePartial](t: Expectable[S]): MatchResult[S] = {
			val partial = t.value
			val dateTime = partial toDateTime readableDateTime
			val matchResult = dateTime compareTo readableDateTime
			val message = s"$dateTime is equal to $readableDateTime"
			result(matchResult == 0, message, negateSentence(message), t)


		}
	}

}

trait AdjustmentContext {

	implicit def mbcHolidayCalendar: MbcHolidaycalendar

	implicit def scalaFinDateMathHolidayCalendar: HolidayCalendar

	implicit def adjustmentService: DateAdjustmentService

}

object AdjustmentContext {

	def apply[A <: MbcHolidaycalendar](cal: A, service: DateAdjustmentService)
		(implicit conversion: A => HolidayCalendar): AdjustmentContext = new AdjustmentContext {

		override implicit val adjustmentService: DateAdjustmentService = service

		override implicit val scalaFinDateMathHolidayCalendar: HolidayCalendar = conversion(cal)

		override implicit val mbcHolidayCalendar: MbcHolidaycalendar = cal

	}

}


trait BusinessDayConventionTupleMatchers extends JodaTimeDateMatchers with MustMatchers {


	def produceIdenticalAdjustmentOn(date: Date)(implicit adjustmentContext: AdjustmentContext): Matcher[(BusinessDayConvention, JFinBusinessDayConvention)] = new
			Matcher[(BusinessDayConvention, JFinBusinessDayConvention)] {

		val localDate = new LocalDate(date.getTime)

		val dateTime = new DateTime(date.getTime)

		import adjustmentContext._

		override def apply[S <: (BusinessDayConvention, JFinBusinessDayConvention)](t: Expectable[S]): MatchResult[S] = {

			val (scalafinDateMathConvention, jfinConvention) = t.value
			val jfinResultedAdjustDate = adjustmentContext.adjustmentService adjust(localDate, jfinConvention, adjustmentContext.mbcHolidayCalendar)
			val scalafinAdjustedDate = scalafinDateMathConvention adjust dateTime
			val matchResult = jfinResultedAdjustDate must beEquivalentTo(scalafinAdjustedDate)
			val message = s"jfin.$jfinConvention and scalafin-datemath.$scalafinDateMathConvention adjust date $localDate on calendar $mbcHolidayCalendar : ${matchResult.message}"
			result(matchResult.isSuccess, message, negateSentence(message), t)

		}

	}


}

trait DayCountConventionTupleMatchers extends JodaTimeDateMatchers with MustMatchers with Generifiers with OrderingImplicits{



	implicit def periodAs[T<:ReadableInstant](period:PaymentPeriod[T]):SchedulePeriod = {
		implicit def toLocalDate(t:T):LocalDate = new LocalDate(t.getMillis,t.getChronology)
		val start:LocalDate = period.actual.start
		val end:LocalDate = period.actual.end
		val referenceStart = period.reference map {referencePeriod => toLocalDate(referencePeriod.start)}
		val referenceEnd  = period.reference map { referencePeriod => toLocalDate(referencePeriod.end)}
		new SchedulePeriod(start,end,referenceStart.getOrElse(null),referenceEnd.getOrElse(null))
	}

	def computeIdenticalDayCountFor[T<:ReadableDateTime](period:PaymentPeriod[T]):Matcher[(DayCountCalculator,JFinDaycountCalculator)] =
		new Matcher[(DayCountCalculator,JFinDaycountCalculator)]{

			override def apply[S <: (DayCountCalculator, JFinDaycountCalculator)](t: Expectable[S]): MatchResult[S] = {
				val (scalafinDateMathCalculator, jfinCalculator) = t.value
				val scalaFinDateMathResult = scalafinDateMathCalculator calculateDayCountFraction period
				val jfinCalculatorResult =  jfinCalculator calculateDaycountFraction period
				val matchResult = jfinCalculatorResult must_== scalaFinDateMathResult
				val message = s"jfin.$jfinCalculator and scalafin-datemath.$scalafinDateMathCalculator adjust date $period to $scalaFinDateMathResult and $jfinCalculatorResult: ${matchResult.message}"
				result(matchResult.isSuccess, message, negateSentence(message), t)

			}
		}

}

trait PeriodMatchers extends MustMatchers{

	def beTheSameInstantAs(instant:ReadableInstant):Matcher[ReadableDateTime]

	def be(start:ReadableInstant, end:ReadableInstant):Matcher[Interval[ReadableDateTime]] = new Matcher[Interval[ReadableDateTime]]{

		override def apply[S <: Interval[ReadableDateTime]](t: Expectable[S]): MatchResult[S] = {
			val interval = t.value
			val startIsCorrect = interval.start aka "The start of interval " must beTheSameInstantAs(start)
			val endIsCorrect = interval.end aka "The end of interval " must beTheSameInstantAs(end)
			val resultMatch = startIsCorrect and endIsCorrect
			result(resultMatch,t)

		}
	}


}

trait ComparableMatchers extends Sentences{

	def beEqualAccordingToCompare[B<:Comparable[B],A<:B](b:B):Matcher[A] = new Matcher[A]{

		override def apply[S <: A](t: Expectable[S]): MatchResult[S] = {
			val s = t.value
			val compareResult = s compareTo b
			val message = s"$s is equal to $b according to java.lang.Comparable interface"
			result(compareResult==0,message,negateSentence(message),t)
		}

	}

}