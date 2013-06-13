package org.scalafin.accrual

import org.scalafin.convention.BusinessDayConvention
import org.scalafin.holiday.HolidayCalendar
import org.joda.time.DateMidnight
import org.scalafin.convention.UNADJUSTED

case class PlanningStrategyConvention(businessDayConvention: BusinessDayConvention, holidayCalendar: HolidayCalendar) {
  def adjustEndDate(date: DateMidnight): DateMidnight = {
    if (businessDayConvention == UNADJUSTED)
      date
    else
      holidayCalendar.adjustFinancialDate(date, businessDayConvention)
  }

  def adjustPaymentDate(date: DateMidnight): DateMidnight = {
    if (businessDayConvention == UNADJUSTED)
      date
    else
      holidayCalendar.adjustFinancialDate(date, businessDayConvention)
  }

  def adjustStartDate(date: DateMidnight): DateMidnight = {
    if (businessDayConvention == UNADJUSTED)
      date
    else
      holidayCalendar.adjustFinancialDate(date, businessDayConvention)
  }
}