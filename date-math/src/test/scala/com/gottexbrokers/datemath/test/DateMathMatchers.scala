package com.gottexbrokers.datemath.test

import org.specs2.text.Sentences
import org.joda.time._
import org.specs2.matcher._
import java.util.Date
import com.gottexbrokers.datemath._
import JFinTypeAliases._
import com.mbc.jfin.schedule.SchedulePeriod
import com.gottexbrokers.datemath.utils.{OrderingImplicits, Generifiers}
import com.gottexbrokers.datemath.scheduler.Schedule
import com.mbc.jfin.holiday.DateAdjustmentService


/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 19/02/14
 * Time: 13:29
 *
 */

trait DateMathMatchers extends JodaTimeDateMatchers
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

	implicit def dateMathHolidayCalendar: HolidayCalendar

	implicit def adjustmentService: DateAdjustmentService

}

object AdjustmentContext {

	def apply[A <: MbcHolidaycalendar](cal: A, service: DateAdjustmentService)
		(implicit conversion: A => HolidayCalendar): AdjustmentContext = new AdjustmentContext {

		override implicit val adjustmentService: DateAdjustmentService = service

		override implicit val dateMathHolidayCalendar: HolidayCalendar = conversion(cal)

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

			val (dateMathConvention, jfinConvention) = t.value
			val jfinResultedAdjustDate = adjustmentContext.adjustmentService adjust(localDate, jfinConvention, adjustmentContext.mbcHolidayCalendar)
			val adjustedDate = dateMathConvention adjust dateTime
			val matchResult = jfinResultedAdjustDate must beEquivalentTo(adjustedDate)
			val message = s"jfin.$jfinConvention and -datemath.$dateMathConvention adjust date $localDate on calendar $mbcHolidayCalendar : ${matchResult.message}"
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
				val (dateMathCalculator, jfinCalculator) = t.value
				val dateMathResult = dateMathCalculator calculateDayCountFraction period
				val jfinCalculatorResult =  jfinCalculator calculateDaycountFraction period
				val matchResult = jfinCalculatorResult must_== dateMathResult
				val message = s"jfin.$jfinCalculator and -datemath.$dateMathCalculator adjust date $period to $dateMathCalculator and $jfinCalculatorResult: ${matchResult.message}"
				result(matchResult.isSuccess, message, negateSentence(message), t)

			}
		}

}

trait PeriodMatchers extends MustMatchers with JodaTimeDateMatchers{

	def beTheSameInstantAs(instant:ReadableInstant):Matcher[ReadableDateTime]

	def be(start:ReadableInstant, end:ReadableInstant):Matcher[math.Period[ReadableDateTime]] = new Matcher[math.Period[ReadableDateTime]]{

		override def apply[S <: math.Period[ReadableDateTime]](t: Expectable[S]): MatchResult[S] = {
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



trait ScheduleMatchers extends MatchersImplicits with MustMatchers with JodaTimeDateMatchers{

	def beAnIntervalEquivalentTo(start:ReadablePartial, end:ReadablePartial):Matcher[math.Period[ReadableDateTime]] = new Matcher[math.Period[ReadableDateTime]]{

		override def apply[S <: math.Period[ReadableDateTime]](t: Expectable[S]): MatchResult[S] = {
			val interval = t.value
			val startIsCorrect = start aka "The start of interval" must beEquivalentTo(interval.start)
			val endIsCorrect = end aka "The end of interval " must beEquivalentTo(interval.end)
			val resultMatch = startIsCorrect and endIsCorrect
			result(resultMatch,t)

		}
	}


	def beAScheduleEquivalentTo(jfinSchedule:JFinSchedule):Matcher[Schedule[ReadableDateTime]] = new Matcher[Schedule[ReadableDateTime]]{

		override def apply[S <: Schedule[ReadableDateTime]](t: Expectable[S]): MatchResult[S] = {
			val value = t.value
			import scala.collection.JavaConversions._
			val zippedPeriods = jfinSchedule.zipWithIndex.zip(value.periods)
			val matchResult = forall(zippedPeriods)    {
				case((jfinSchedulerPeriod,index), period) => period must beAPeriodEquivalentTo(jfinSchedulerPeriod).updateMessage(s => s"For period $index $s")
			}
			result(matchResult,t)
		}
	}

	def beAPeriodEquivalentTo(paymentPeriod:JFinSchedulePeriod):Matcher[PaymentPeriod[ReadableDateTime]] = new Matcher[PaymentPeriod[ReadableDateTime]]{
		override def apply[S <: PaymentPeriod[ReadableDateTime]](t: Expectable[S]): MatchResult[S] = {
			val value = t.value
			val actualMatch = value.actual aka "The actual interval" must beAnIntervalEquivalentTo(paymentPeriod.getStart,paymentPeriod.getEnd)
			//TODO use applicative syntax
//			import scalaz.Scalaz._
//			val referenceOption = ( Option(paymentPeriod.getReferenceStart) |@|  Option(paymentPeriod.getReferenceEnd)) {(x,y) => (x,y) }
			val referenceOption = if(paymentPeriod.getReferenceStart!=null || paymentPeriod.getReferenceEnd!=null)
																Some((paymentPeriod.getReferenceStart,paymentPeriod.getReferenceEnd))
														else
																None
			val referenceMatch = referenceOption match {
					case Some((x,y)) => value.reference aka "The reference interval" must beLike{
						case Some(r) => r must beAnIntervalEquivalentTo(x,y)
						case None => value.actual must beAnIntervalEquivalentTo(x,y)
					}
					case None => value.reference aka "The reference interval" must beNone
			}
			result(referenceMatch and actualMatch, t)

		}
	}


}