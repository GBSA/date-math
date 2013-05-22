package org.scalafin.date.accrual

import org.scalafin.date.DatePeriod
import org.joda.time.DateMidnight
import org.scalafin.date.holiday.HolidayCalendar
import org.scalafin.date.daycount.DaycountCalculator
import org.scalafin.date.BusinessDayConvention
import org.scalafin.date.UNADJUSTED
import org.scalafin.date.DateRange
import scalaz.Validation
import org.scalafin.date.DateRangeException

trait AccrualPeriod extends DatePeriod {

  def accrualPlanningStrategyConvention: PlanningStrategyConvention
  def paymentPlanningStrategyConvention: PlanningStrategyConvention
  def daycountCalculator: DaycountCalculator
  def paymentType: PaymentType

  def paymentAmount(notional: Double): Double
  def isPaymentPossible: Boolean

  val paymentDate = paymentType match {
    case IN_ADVANCE => dateRange.startDate
    case IN_ARREARS => dateRange.endDate
  }

  def adjustedEndDate: DateMidnight = accrualPlanningStrategyConvention.adjustEndDate(dateRange.endDate)

  def adjustedPaymentDate: DateMidnight =paymentPlanningStrategyConvention.adjustPaymentDate(paymentDate) 

  def adjustedStartDate: DateMidnight = accrualPlanningStrategyConvention.adjustStartDate(dateRange.startDate)

  def adjustedDaycountFraction: Double = daycountCalculator.calculateDaycountFraction(dateRange)

}
