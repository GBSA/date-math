package com.gottexbrokers.datemath.test


import org.joda.time.{ReadableDateTime, DateTimeConstants, LocalDate}
import com.gottexbrokers.datemath.HolidayCalendar
import com.mbc.jfin.holiday.{HolidayCalendar => MbcHolidaycalendar}

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 19/02/14
 * Time: 13:21
 *
 */


trait DateMathMocks {

  trait SimpleMbcHolidayCalendar extends MbcHolidaycalendar {

	  self =>

    def listOfHolidays: List[LocalDate]


    override def isWeekend(date: LocalDate): Boolean = date.getDayOfWeek == DateTimeConstants.SATURDAY || date.getDayOfWeek == DateTimeConstants.SUNDAY

    override def isHoliday(date: LocalDate): Boolean = listOfHolidays exists { _ == date }

    override def toString = s"SimpleMbcHolidayCalendar backed by $listOfHolidays"


    def toDateMathCalendar: HolidayCalendar = new HolidayCalendar {

	    private implicit def toLocalDate(date: ReadableDateTime): LocalDate = new LocalDate(date.getMillis)

      override def isWeekend(date: ReadableDateTime): Boolean = self isWeekend date

      override def isHoliday(date: ReadableDateTime): Boolean =  self isHoliday date

      override def toString = s"DateMathHolidayCalendar backed by $listOfHolidays"
    }


  }

}

trait DateMathTestInstances extends DateMathMocks {

  implicit def toDateMathCalendar(mbcHolidayCalendar: SimpleMbcHolidayCalendar): HolidayCalendar = mbcHolidayCalendar.toDateMathCalendar

}

