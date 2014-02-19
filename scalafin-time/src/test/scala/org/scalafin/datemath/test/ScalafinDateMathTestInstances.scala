package org.scalafin.datemath.test


import java.util.Date
import org.joda.time.{DateTime, ReadableDateTime, DateTimeConstants, LocalDate}
import org.scalafin.datemath.HolidayCalendar
import com.mbc.jfin.holiday.{HolidayCalendar => MbcHolidaycalendar}
import org.specs2.{Specification, ScalaCheck}
import org.scalacheck.{Arbitrary, Prop}
import Arbitrary._
import org.specs2.matcher.Parameters
import org.scalacheck.util.Pretty
import org.scalafin.date
import org.scalafin

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 19/02/14
 * Time: 13:21
 *
 */

trait ScalafinDateMathTestInstancesTestImplicits {

  implicit def toLocalDate(javaDate: Date): LocalDate = new LocalDate(javaDate.getTime)




}

trait ScalafinDateMathMocks {

  trait SimpleMbcHolidayCalendar extends MbcHolidaycalendar with ScalafinDateMathTestInstancesTestImplicits {

	  self =>

    def listOfHolidays: List[Date]



    override def isWeekend(date: LocalDate): Boolean = date.getDayOfWeek == DateTimeConstants.SATURDAY || date.getDayOfWeek == DateTimeConstants.SUNDAY


    override def isHoliday(date: LocalDate): Boolean = listOfHolidays exists {
      javaDate => (javaDate compareTo date) == 0
    }

    override def toString = s"SimpleMbcHolidayCalendar backed by $listOfHolidays"


    def toScalafinDateMathCalendar: HolidayCalendar = new HolidayCalendar {

	    implicit def toLocalDate(date: ReadableDateTime): LocalDate = new LocalDate(date.getMillis)

      override def isWeekend(date: ReadableDateTime): Boolean = self isWeekend date

      override def isHoliday(date: ReadableDateTime): Boolean =  self isHoliday date

      override def toString = s"ScalafinDateMathHolidayCalendar backed by $listOfHolidays"
    }


  }

}

trait ScalafinDateMathTestInstances extends ScalafinDateMathTestInstancesTestImplicits with ScalafinDateMathMocks {

  implicit def toScalafinDateMathCalendar(mbcHolidayCalendar: SimpleMbcHolidayCalendar): HolidayCalendar = mbcHolidayCalendar.toScalafinDateMathCalendar

}

