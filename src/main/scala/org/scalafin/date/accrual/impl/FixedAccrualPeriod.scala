package org.scalafin.date.accrual.impl

import org.scalafin.date.accrual.AccrualPeriod
import org.joda.time.DateMidnight
import org.scalafin.date.DatePeriod
import org.scalafin.date.holiday.HolidayCalendar
import org.scalafin.date.daycount.DaycountCalculator
import org.scalafin.date.BusinessDayConvention
import org.scalafin.date.DateRange
import org.scalafin.date.accrual.PlanningStrategyConvention
import org.scalafin.date.accrual.PaymentType
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

  def getPaymentAmount(notional: Double): Double = getAdjustedDaycountFraction * fixedRate * notional
  def isPaymentPossible: Boolean = true

}