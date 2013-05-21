package org.scalafin.date.accrual.impl

import org.scalafin.date.accrual.AccrualPeriod
import org.scalafin.date.BusinessDayConvention
import org.joda.time.DateMidnight
import org.scalafin.date.holiday.HolidayCalendar
import org.scalafin.date.daycount.DaycountCalculator
import org.scalafin.date.DateRange
import org.scalafin.date.accrual.PlanningStrategyConvention
import org.scalafin.date.accrual.PaymentType
import org.scalafin.date.UNADJUSTED
import scalaz.Validation
import org.scalafin.date.DateRangeException
import scalaz.Success

case class FloatAccrualPeriod(
  dateRange: DateRange,
  paymentPlanningStrategyConvention: PlanningStrategyConvention,
  accrualPlanningStrategyConvention: PlanningStrategyConvention,
  daycountCalculator: DaycountCalculator,
  fixingDetails: FixingDetails,
  paymentType: PaymentType,
  originalDateRange: Option[DateRange]) extends AccrualPeriod {

  val fixingCalendar = fixingDetails.adjustmentType match {
    case BUSINESS_DAYS => fixingDetails.planningStrategyConvention.holidayCalendar.advanceBusinessDay(paymentDate, fixingDetails.calendarOffsetDays)
    case _ => fixingDetails.planningStrategyConvention.holidayCalendar.advance(paymentDate, fixingDetails.calendarOffsetDays, UNADJUSTED)
  }

  def getPaymentAmount(notional: Double): Double = {
    if (isPaymentPossible)
      getAdjustedDaycountFraction * fixingDetails.rate * notional
    else
      -1
  }
  def isPaymentPossible: Boolean = fixingDetails.rate != -1
  def getAdjustedFixingDate: DateMidnight = fixingDetails.planningStrategyConvention.holidayCalendar.adjust(fixingDetails.date, fixingDetails.planningStrategyConvention.businessDayConvention)
}