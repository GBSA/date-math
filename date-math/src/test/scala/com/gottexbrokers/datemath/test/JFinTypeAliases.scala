package com.gottexbrokers.datemath.test

trait JFinTypeAliases {

  type JFinBusinessDayConvention = com.mbc.jfin.holiday.BusinessDayConvention

  type MbcHolidaycalendar = com.mbc.jfin.holiday.HolidayCalendar

  type JFinDaycountCalculator = com.mbc.jfin.daycount.impl.DaycountCalculator

  type JFinSchedule = com.mbc.jfin.schedule.Schedule

  type JFinSchedulePeriod = com.mbc.jfin.schedule.SchedulePeriod

}

object JFinTypeAliases extends JFinTypeAliases
