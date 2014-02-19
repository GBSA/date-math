package org.scalafin.datemath.test

import org.scalacheck.Arbitrary._
import java.util.Date

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

