package org.scalafin.accrual

import org.scalafin.accrual.{FixingDetails, AccrualPeriod, PlanningStrategyConvention, PaymentType}
import org.scalafin.convention.BusinessDayConvention
import org.joda.time.DateMidnight
import org.scalafin.holiday.HolidayCalendar
import org.scalafin.daycount.DaycountCalculator
import org.scalafin.date.DateRange
import org.scalafin.convention.UNADJUSTED
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

  val fixingDate = fixingDetails.calculateFixingDate(paymentDate)

  def paymentAmount(notional: Double): Double = {
    if (isPaymentPossible)
      adjustedDaycountFraction * fixingDetails.rate * notional
    else
      -1 // FIXME better to return a Validation ? 
  }
  
  def isPaymentPossible: Boolean = fixingDetails.rate != -1
  def getAdjustedFixingDate: DateMidnight = fixingDetails.adjustFinancialDate
}