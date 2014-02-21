package org.scalafin.datemath.test



import org.scalacheck.{Gen, Arbitrary}
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

trait JavaToJodaTimeConversions{

	implicit  def toJodaDateMidnight(millis:Long) = new DateMidnight(millis)

	implicit  def toJodaDateTime(millis:Long) = new DateTime(millis)

	implicit  def toJodaLocalDate(millis:Long) = new LocalDate(millis)

}

trait BoundedLongGeneration {

	def min:Long

	def max:Long

	implicit val notTooLong:Arbitrary[Long] = Arbitrary {
		Gen chooseNum (min,max)
	}


}

trait FromAmericanDiscoveryToJupiter  extends BoundedLongGeneration{

	val americanDiscovery = new DateTime(1492, 4, 1, 0, 0).getMillis

	val voyageOnJupiter = new DateTime(2400,1,1,0,0).getMillis

	val min = americanDiscovery

	val max = voyageOnJupiter

}

trait JodaTimeGenerators extends JavaToJodaTimeConversions {

	implicit def JodaDateMidnightArbitrary(implicit arbitrary:Arbitrary[Long]):Arbitrary[DateMidnight] = arbitraryFromConversion[DateMidnight]

	implicit def JodaDateTimeArbitrary(implicit arbitrary:Arbitrary[Long]):Arbitrary[DateTime] = arbitraryFromConversion[DateTime]

	implicit def JodaLocalDateArbitrary(implicit arbitrary:Arbitrary[Long]):Arbitrary[LocalDate] = arbitraryFromConversion[LocalDate]

	private def arbitraryFromConversion[T](implicit arbitrary:Arbitrary[Long], fromLongBuilder:Long => T):Arbitrary[T] = Arbitrary {
		arbitrary.arbitrary map fromLongBuilder
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



