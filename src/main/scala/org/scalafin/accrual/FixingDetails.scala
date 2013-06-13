package org.scalafin.accrual

import org.joda.time.DateMidnight
import org.scalafin.convention.UNADJUSTED

case class FixingDetails(
  date: DateMidnight,
  planningStrategyConvention: PlanningStrategyConvention,
  calendarOffsetDays: Int,
  rate: Double,
  adjustmentType: FixingAdjustmentType) {

  def calculateFixingDate(paymentDate: DateMidnight) = {
    adjustmentType match {
      case BUSINESS_DAYS => planningStrategyConvention.holidayCalendar.advanceBusinessDay(paymentDate, calendarOffsetDays)
      case _ => planningStrategyConvention.holidayCalendar.advance(paymentDate, calendarOffsetDays, UNADJUSTED)
    }
  }

  def adjustFinancialDate = {
    planningStrategyConvention.holidayCalendar.adjustFinancialDate(date, planningStrategyConvention.businessDayConvention)
  }

}