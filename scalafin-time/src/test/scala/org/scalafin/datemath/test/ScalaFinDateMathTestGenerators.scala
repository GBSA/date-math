package org.scalafin.datemath.test



import org.scalacheck.{Arbitrary, Gen}
import org.joda.time._
import org.scalafin.utils.Interval
import org.scalafin.datemath.PaymentPeriod
import scalaz.Show

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 19/02/14
 * Time: 13:23
 *
 */


trait ScalaFinDateMathTestGenerators extends CalendarsGenerators{

  self: ScalafinDateMathTestInstances =>

}

trait CalendarsGenerators extends JodaTimeGenerators {

	self:ScalafinDateMathTestInstances =>
	implicit def mbcHolidayCalendarArbitrary = Arbitrary {
		Arbitrary.arbitrary(Arbitrary.arbContainer[List,LocalDate]).map {
			dateList =>
				new SimpleMbcHolidayCalendar {
					override def listOfHolidays = dateList
				}

		}
	}




}

trait LongToJodaTimeConversions{

	/* This does not work correctly
	   implicit  def toJodaDateTime(millis:Long) = toJodaLocalDate(millis).toDateTimeAtStartOfDay
	 */
	implicit  def toJodaDateTime(millis:Long) = {
		val localDate = toJodaLocalDate(millis)
		new DateTime(localDate.getYear,localDate.getMonthOfYear,localDate.getDayOfMonth,0,0)
	}

	implicit  def toJodaLocalDate(millis:Long) = new LocalDate(millis)

}

trait BoundedLongGeneration {

	def min:Long

	def max:Long

	implicit val notTooLong:Arbitrary[Long] = Arbitrary {
			Gen chooseNum (min,max)
		}


}

trait LongGeneratorWithNoOverflow extends BoundedLongGeneration {


	val safetyFactory = 100000000L

	override val max: Long = Long.MaxValue / safetyFactory

	override val min: Long = - Long.MaxValue / safetyFactory

}



trait FromAmericanDiscoveryToJupiter  extends BoundedLongGeneration{

	val americanDiscovery = new DateTime(1492, 4, 1, 0, 0).getMillis

	val voyageOnJupiter = new DateTime(2400,1,1,0,0).getMillis

	val min = americanDiscovery

	val max = voyageOnJupiter

}

trait JodaTimeGenerators extends LongToJodaTimeConversions with BoundedLongGeneration{

	implicit val JodaDateTimeArbitrary:Arbitrary[DateTime] = arbitraryFromConversion[DateTime]

	implicit val JodaLocalDateArbitrary:Arbitrary[LocalDate] = arbitraryFromConversion[LocalDate]

	private def arbitraryFromConversion[T](implicit fromLongBuilder:Long => T):Arbitrary[T] = Arbitrary {
		notTooLong.arbitrary map fromLongBuilder
	}


	def periodNotDependingOnStart:Gen[Period] = {
		for {
			days <- Gen.choose(-31,31)
			years <- Gen.choose(-50,+50)
			hours <- Gen.choose(-24,24)
		  weeks <- Gen.choose(-52,52)
		  minutes <- Gen.choose(-120,120)
			seconds <- Gen.choose(-120,120)
			millisec <- Gen.choose(-2000,2000)
		} yield new Period(0,0,weeks,days + years *365,hours,minutes,seconds,millisec)
	}

}

trait ScheduledFinancialPeriodGenerators {


	def arbitraryFinancialPeriodWithNoReference[T]
	(implicit intervalArbitrary:Arbitrary[Interval[T]], show:Show[Interval[T]]):Arbitrary[PaymentPeriod[T]] = Arbitrary {
		intervalArbitrary.arbitrary.map {
			interval =>
				new PaymentPeriod[T] {
				override def actual: Interval[T] = interval
				override def reference:Option[Interval[T]] = None

				override def toString = s"Actual: ${show shows actual} reference: ${reference map show.shows}"
			}
		}
	}


}



