package org.scalafin.date.accrual

import org.scalafin.date.BusinessDayConvention
import org.scalafin.date.holiday.HolidayCalendar
import org.joda.time.DateMidnight
import org.scalafin.date.UNADJUSTED

case class PlanningStrategyConvention(businessDayConvention: BusinessDayConvention, holidayCalendar: HolidayCalendar) {
  def adjustEndDate(date: DateMidnight): DateMidnight = {
    if (businessDayConvention == UNADJUSTED)
      date
    else
      holidayCalendar.adjust(date, businessDayConvention)
  }

  def adjustPaymentDate(date: DateMidnight): DateMidnight = {
    if (businessDayConvention == UNADJUSTED)
      date
    else
      holidayCalendar.adjust(date, businessDayConvention)
  }

  def adjustStartDate(date: DateMidnight): DateMidnight = {
    if (businessDayConvention == UNADJUSTED)
      date
    else
      holidayCalendar.adjust(date, businessDayConvention)
  }
}