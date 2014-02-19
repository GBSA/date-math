package org.scalafin.datemath

import org.specs2.{ScalaCheck, Specification}
import com.mbc.jfin.holiday.{BusinessDayConvention => JFinBusinessDayConvention, HolidayCalendar => MbcHolidaycalendar}
import org.scalacheck.{Arbitrary, Prop}
import Arbitrary._
import java.util.Date
import com.mbc.jfin.holiday.impl.DefaultDateAdjustmentServiceImpl
import org.specs2.specification.Fragments
import org.scalafin.datemath.test._
import org.joda.time.{DateTime, LocalDate}

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 18/02/14
 * Time: 15:16
 *
 */
class CoherenceTests extends Specification {

  override def is: Fragments = include(new BusinessDayConventionsCoherenceTests)

}



//class BusinessDayConventionSimpleTest extends BusinessDayConventionsCoherenceTests {
//
//  override def is: Fragments = testChunk(tuples, "The business day conventions should be equivalent between Jfin and scalafin-datemath", testTuple)
//
//  def testTuple(tuple: (BusinessDayConvention, JFinBusinessDayConvention)) = {
//    val (scalafinDateMathConvention, jfinConvention) = tuple
//    val jodaDate = new DateTime(2022200,1,21,0,0,0,0)
//    val jodaDate2 = new DateTime(2014,2,19,0,0,0,0)
//    val javaDate1 = new Date(jodaDate.getMillis)
//    val javaDate2 = new Date(jodaDate2.getMillis)
//    val exampleName = s"jfin.$jfinConvention and scalafin-datemath.$scalafinDateMathConvention must adjust dates equally on $jodaDate"
//    val calendar = new SimpleMbcHolidayCalendar {
//      override def listOfHolidays: List[Date] = List(javaDate1,javaDate2)
//    }
//    implicit val context = AdjustmentContext(calendar,service)
//    exampleName ! ( tuple must produceIdenticalAdjustmentOn(javaDate2) )
//
//  }
//
//
//}
//
//
//class BusinessDayConventionTest extends BusinessDayConventionsCoherenceTests {
//
//
//
//
//}
