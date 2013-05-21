package org.scalafin.date.accrual.impl

import org.joda.time.DateMidnight
import org.scalafin.date.accrual.PlanningStrategyConvention

case class FixingDetails(
  date: DateMidnight,
  planningStrategyConvention: PlanningStrategyConvention,
  calendarOffsetDays: Int,
  rate: Double,
  adjustmentType: FixingAdjustmentType) {

}