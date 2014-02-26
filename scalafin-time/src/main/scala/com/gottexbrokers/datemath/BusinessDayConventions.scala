package com.gottexbrokers.datemath

import org.joda.time.ReadableDateTime
import com.gottexbrokers.datemath.utils.RichJodaTimeExtensions._
import scala.annotation.tailrec



/**
 * <p>
 * Enumeration of the various business day conventions:
 * </p>
 *
 * <ul>
 * <li> Unadjusted
 * <li> Preceding
 * <li> Modified Preceding
 * <li> Following
 * <li> Modified Following
 * <li> Month End Reference
 * </ul>
 */

object BusinessDayConventions {

	private implicit def toDateTime(readableDateTime:ReadableDateTime) = readableDateTime.toDateTime

  /**
   * Do not adjust.
   */
  case object UNADJUSTED extends UnadjustedBusinessDayConvention


  /**
   * Choose the first business day before the given holiday.
   */
  case object PRECEDING extends BusinessDayConvention {

    @tailrec
    def adjust(dt: ReadableDateTime)(implicit hc: HolidayCalendar): ReadableDateTime = {
      if (! ( hc.isHoliday(dt) || hc.isWeekend(dt)) ) {
        dt
      } else {
        adjust(dt minusDays 1)
      }

    }
  }


  /**
   * Choose the first business day after the given holiday.
   */
  case object FOLLOWING extends BusinessDayConvention {
    @tailrec
    def adjust(dt: ReadableDateTime)(implicit hc: HolidayCalendar) : ReadableDateTime = {
      if ( !( hc.isHoliday(dt) || hc.isWeekend(dt))) {
        dt
      } else {
        adjust(dt plusDays 1)
      }

    }
  }



  /**
   * Choose the first business day after the given holiday, if the original
   * date falls on last business day of month result reverts to first business
   * day before month-end
   */
  case object MONTH_END_REFERENCE extends BusinessDayConvention  {

    def adjust(dt: ReadableDateTime)(implicit hc: HolidayCalendar): ReadableDateTime = {
      PRECEDING adjust dt.dayOfMonth.withMaximumValue
    }

  }




  /**
   * Choose the first business day before the given holiday unless it belongs
   * to a different month, in which case choose the first business day after
   * the holiday.
   */
  case object MODIFIED_PRECEDING extends BusinessDayConvention{
    def adjust(dt: ReadableDateTime)(implicit hc: HolidayCalendar): ReadableDateTime = {
      val adjustedDate = PRECEDING adjust dt
      if (adjustedDate.monthOfYear != dt.monthOfYear) {
        FOLLOWING adjust dt
      } else {
        adjustedDate
      }
    }
  }

  /**
   * Choose the first business day after the given holiday unless it belongs
   * to a different month, in which case choose the first business day before
   * the holiday.
   */
  case object MODIFIED_FOLLOWING extends BusinessDayConvention {
    def adjust(dt: ReadableDateTime)(implicit hc: HolidayCalendar): ReadableDateTime = {
      val adjustedDate = FOLLOWING adjust dt
      if (adjustedDate.monthOfYear != dt.monthOfYear) {
        PRECEDING adjust dt
      } else {
        adjustedDate
      }
    }

  }




}


