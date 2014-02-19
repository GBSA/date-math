package org.scalafin.datemath

import org.joda.time.{DateTimeConstants, ReadableDateTime}

import org.scalafin.date._






trait HolidayCalendar {

  def isWeekend(date: ReadableDateTime): Boolean
  def isHoliday(date: ReadableDateTime): Boolean

}

object HolidayCalendar {

  lazy val WeekendOnlyHolidayCalendar = new HolidayCalendar {

    override def isWeekend(date: ReadableDateTime): Boolean =  date.getDayOfWeek == DateTimeConstants.SATURDAY || date.getDayOfWeek == DateTimeConstants.SUNDAY

    override def isHoliday(date: ReadableDateTime): Boolean = false

  }

}

