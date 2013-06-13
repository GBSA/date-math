package org.scalafin.holiday.impl

import org.scalafin.holiday.HolidayCalendar
import org.joda.time.DateMidnight
import org.joda.time.DateTimeConstants

class WeekendHolidayCalendar extends HolidayCalendar {
  def isWeekend(dt: DateMidnight): Boolean = dt.dayOfWeek == DateTimeConstants.SATURDAY || dt.dayOfWeek == DateTimeConstants.SUNDAY

  def isHoliday(dt: DateMidnight): Boolean = false
}