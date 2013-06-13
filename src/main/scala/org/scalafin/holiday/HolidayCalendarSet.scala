package org.scalafin.holiday

import collection.mutable.Set
import org.joda.time.DateMidnight

class HolidayCalendarSet private (val holidayCalendars: Set[HolidayCalendar]) {
  def add(hc: HolidayCalendar): Boolean = {
    holidayCalendars.add(hc)
  }

  def remove(hc: HolidayCalendar): Boolean = {
    holidayCalendars.remove(hc)
  }

  def isWeekend(date: DateMidnight): Boolean = holidayCalendars.exists(_.isWeekend(date))

  def isHoliday(date: DateMidnight): Boolean = holidayCalendars.exists(_.isHoliday(date))

}

object HolidayCalendarSet {
  def apply(initialSet: Set[HolidayCalendar]): HolidayCalendarSet = {
    new HolidayCalendarSet(initialSet)
  }
}