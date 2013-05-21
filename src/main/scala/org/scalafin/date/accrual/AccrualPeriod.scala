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

  def getPaymentAmount(notional: Double): Double
  def isPaymentPossible: Boolean

  val paymentDate = paymentType match {
    case IN_ADVANCE => dateRange.startDate
    case IN_ARREARS => dateRange.endDate
  }

  def getAdjustedEndDate: DateMidnight = {
    if (accrualPlanningStrategyConvention.businessDayConvention == UNADJUSTED)
      dateRange.endDate
    else
      accrualPlanningStrategyConvention.holidayCalendar.adjust(dateRange.endDate, accrualPlanningStrategyConvention.businessDayConvention)
  }

  def getAdjustedPaymentDate: DateMidnight = {
    if (paymentPlanningStrategyConvention.businessDayConvention == UNADJUSTED)
      paymentDate
    else
      paymentPlanningStrategyConvention.holidayCalendar.adjust(paymentDate, paymentPlanningStrategyConvention.businessDayConvention)
  }

  def getAdjustedStartDate: DateMidnight = {
    if (accrualPlanningStrategyConvention.businessDayConvention == UNADJUSTED)
      dateRange.startDate
    else
      accrualPlanningStrategyConvention.holidayCalendar.adjust(dateRange.startDate, accrualPlanningStrategyConvention.businessDayConvention)
  }

  def getAdjustedDaycountFraction: Double = daycountCalculator.calculateDaycountFraction(dateRange)

}
