package com.gottexbrokers.datemath.utils

import org.joda.time.ReadableDateTime
import com.gottexbrokers.datemath.{BusinessDayConvention, HolidayCalendar}




/**
  * Adjust a date for given BusinessDayConvention and HolidayCalendar
  *
  * @author jeremy.nguyenxuan
  *
  */

trait DateAdjustmentTools {

	private implicit def toDateTime(readableDateTime:ReadableDateTime) = readableDateTime.toDateTime

   def advance(date: ReadableDateTime, hc:HolidayCalendar, offsetAmount: Int, businessDayConvention: BusinessDayConvention): ReadableDateTime = {
     if (offsetAmount == 0) {
       businessDayConvention.adjust(date)(hc)
     } else if (offsetAmount > 0) {
       val nextDate = date plusDays 1
       if (hc.isWeekend(nextDate) || hc.isHoliday(nextDate)) {
         advance(nextDate, hc, offsetAmount, businessDayConvention)
       } else {
         advance(nextDate, hc,offsetAmount - 1, businessDayConvention)
       }
     } else {
       val nextDate = date minusDays 1
       if (hc.isWeekend(nextDate) || hc.isHoliday(nextDate)) {
         advance(nextDate, hc,offsetAmount, businessDayConvention)
       } else {
         advance(nextDate, hc,offsetAmount + 1, businessDayConvention)
       }
     }
   }

 }

object DateAdjustmentTools extends DateAdjustmentTools