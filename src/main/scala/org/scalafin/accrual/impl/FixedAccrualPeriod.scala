package org.scalafin.accrual.impl

import org.scalafin.accrual.AccrualPeriod
import org.joda.time.DateMidnight
import org.scalafin.date.DatePeriod
import org.scalafin.holiday.HolidayCalendar
import org.scalafin.daycount.DaycountCalculator
import org.scalafin.convention.BusinessDayConvention
import org.scalafin.date.DateRange
import org.scalafin.accrual.PlanningStrategyConvention
import org.scalafin.accrual.PaymentType
import scalaz.Validation
import org.scalafin.date.DateRangeException

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