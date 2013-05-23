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

  val fixingDate = fixingDetails.calculateFixingDate(paymentDate)

  def paymentAmount(notional: Double): Double = {
    if (isPaymentPossible)
      adjustedDaycountFraction * fixingDetails.rate * notional
    else
      -1 // FIXME better to return an exception or a Validation ? 
  }
  def isPaymentPossible: Boolean = fixingDetails.rate != -1
  def getAdjustedFixingDate: DateMidnight = fixingDetails.adjustFinancialDate
}