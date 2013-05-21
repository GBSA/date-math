package org.scalafin.date.accrual

import org.scalafin.date.BusinessDayConvention
import org.scalafin.date.holiday.HolidayCalendar

case class PlanningStrategyConvention(businessDayConvention: BusinessDayConvention, holidayCalendar: HolidayCalendar)