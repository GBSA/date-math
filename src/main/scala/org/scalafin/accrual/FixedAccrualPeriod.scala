package org.scalafin.accrual

import org.scalafin.daycount.DaycountCalculator
import org.scalafin.date.DateRange

case class FixedAccrualPeriod(
  dateRange: DateRange,
  paymentPlanningStrategyConvention: PlanningStrategyConvention,
  accrualPlanningStrategyConvention: PlanningStrategyConvention,
  daycountCalculator: DaycountCalculator,
  fixedRate: Double,
  paymentType: PaymentType,
  originalDateRange: Option[DateRange])
  extends AccrualPeriod {

  def paymentAmount(notional: Double): Double = adjustedDaycountFraction * fixedRate * notional
  def isPaymentPossible: Boolean = true

}