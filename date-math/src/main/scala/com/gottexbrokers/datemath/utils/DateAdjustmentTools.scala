package com.gottexbrokers.datemath.utils

import org.joda.time.{ Period, ReadableDateTime }
import com.gottexbrokers.datemath.{ BusinessDayConvention, HolidayCalendar }
import com.gottexbrokers.datemath.scheduler.Schedule

/**
 * Adjust a date for given BusinessDayConvention and HolidayCalendar
 *
 */

trait DateAdjustmentTools {

  private implicit def toDateTime(readableDateTime: ReadableDateTime) = readableDateTime.toDateTime

  def advance(date: ReadableDateTime, hc: HolidayCalendar, offsetAmount: Int, businessDayConvention: BusinessDayConvention): ReadableDateTime = {
    if (offsetAmount == 0) {
      businessDayConvention.adjust(date)(hc)
    } else if (offsetAmount > 0) {
      val nextDate = date plusDays 1
      if (hc.isWeekend(nextDate) || hc.isHoliday(nextDate)) {
        advance(nextDate, hc, offsetAmount, businessDayConvention)
      } else {
        advance(nextDate, hc, offsetAmount - 1, businessDayConvention)
      }
    } else {
      val nextDate = date minusDays 1
      if (hc.isWeekend(nextDate) || hc.isHoliday(nextDate)) {
        advance(nextDate, hc, offsetAmount, businessDayConvention)
      } else {
        advance(nextDate, hc, offsetAmount + 1, businessDayConvention)
      }
    }
  }

  def adjust(schedule: Schedule, businessDayConvention: BusinessDayConvention)(implicit holidayCalendar: HolidayCalendar): Schedule = {
    // Since the adjustment is done on all the dates with the same convention, we can assume it to be safe
    val adjustedStart = businessDayConvention adjust schedule.start
    val adjustedEnd = businessDayConvention adjust schedule.end
    val periods = schedule.periods map {
      period => period copy (start = businessDayConvention adjust period.start, end = businessDayConvention adjust period.end)
    }
    new Schedule(periods, adjustedStart, adjustedEnd)
  }

}

object DateAdjustmentTools extends DateAdjustmentTools