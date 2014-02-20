package org.scalafin.datemath.test

import org.scalacheck.Arbitrary._
import java.util.Date
import org.scalacheck.{Arbitrary, Gen}
import org.joda.time.{LocalDate, DateTime, DateMidnight}
import org.scalafin.utils.{IntervalBuilder, Interval}

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

trait CalendarsGenerators {

  self:ScalafinDateMathTestInstances =>
  def mbcHolidayCalendarGen = arbitrary[List[Date]].map {
                                                          dateList =>
                                                            new SimpleMbcHolidayCalendar {
                                                              override def listOfHolidays = dateList
                                                            }

                                                        }



}

trait JavaToJodaTimeConversions{

	implicit  def toJodaDateMidnight(javaDate:Date) = new DateMidnight(javaDate.getTime)

	implicit  def toJodaDateTime(javaDate:Date) = new DateTime(javaDate.getTime)

	implicit  def toJodaLocalDate(javaDate:Date) = new LocalDate(javaDate.getTime)

}

trait JodaTimeGenerators extends JavaToJodaTimeConversions {

	implicit val JodaDateMidnightArbitrary = arbitraryFromConversion[DateMidnight]

	implicit val JodaDateTimeArbitrary = arbitraryFromConversion[DateTime]

	implicit val JodaLocalDateArbitrary = arbitraryFromConversion[LocalDate]


	private def arbitraryFromConversion[T](implicit dateConverter:Date => T):Arbitrary[T] = Arbitrary {
		Arbitrary.arbitrary[Date] map dateConverter
	}





}



