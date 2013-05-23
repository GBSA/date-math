package org.scalafin.date.holiday

import org.joda.time.DateMidnight
import com.github.nscala_time.time.Imports._
import java.util.Calendar
import org.scalafin.date.BusinessDayConvention
import org.scalafin.date.FOLLOWING
import org.scalafin.date.MONTH_END_REFERENCE
import org.scalafin.date.PRECEDING
import org.scalafin.date.MODIFIED_PRECEDING
import org.scalafin.date.MODIFIED_FOLLOWING
import org.scalafin.date.UNADJUSTED

abstract class BusinessDayConventionAdjustment {
  def adjust(dt: DateMidnight, hc: HolidayCalendar): DateMidnight
}

case object UnadjustedAjustment extends BusinessDayConventionAdjustment {
  def adjust(dt: DateMidnight, hc: HolidayCalendar): DateMidnight = dt
}

case object PrecedingAdjustment extends BusinessDayConventionAdjustment {
  def adjust(dt: DateMidnight, hc: HolidayCalendar): DateMidnight = {
    def adjust_(dt1: DateMidnight): DateMidnight = {
      if (hc.isHoliday(dt1) || hc.isWeekend(dt1)) {
        adjust_(dt1 minusDays 1)
      } else {
        dt1
      }
    }
    adjust_(dt)
  }
}

case object FollowingAdjustment extends BusinessDayConventionAdjustment {
  def adjust(dt: DateMidnight, hc: HolidayCalendar): DateMidnight = {
    def adjust_(dt1: DateMidnight): DateMidnight = {
      if (hc.isHoliday(dt1) || hc.isWeekend(dt1)) {
        adjust_(dt1 plusDays 1)
      } else {
        dt1
      }
    }
    adjust_(dt)
  }
}
case object MonthEndReference extends BusinessDayConventionAdjustment {
  def adjust(dt: DateMidnight, hc: HolidayCalendar): DateMidnight = {
    dt.dayOfMonth.withMaximumValue
  }
}

case object ModifiedPrecedingAdjustment extends BusinessDayConventionAdjustment {
  def adjust(dt: DateMidnight, hc: HolidayCalendar): DateMidnight = {
    val adjustedDate = PrecedingAdjustment.adjust(dt, hc)
    if (adjustedDate.monthOfYear != dt.monthOfYear) {
      FollowingAdjustment.adjust(dt, hc)
    } else {
      adjustedDate
    }
  }
}

case object ModifiedFollowingAdjustment extends BusinessDayConventionAdjustment {
  def adjust(dt: DateMidnight, hc: HolidayCalendar): DateMidnight = {
    val adjustedDate = FollowingAdjustment.adjust(dt, hc)
    if (adjustedDate.monthOfYear != dt.monthOfYear) {
      PrecedingAdjustment.adjust(dt, hc)
    } else {
      adjustedDate
    }
  }
}

abstract class HolidayCalendar {
  def isWeekend(date: DateMidnight): Boolean
  def isHoliday(date: DateMidnight): Boolean
  def advanceBusinessDays(date: DateMidnight, offsetAmount: Int): DateMidnight = {
    advance(date, offsetAmount, UNADJUSTED)
  }

  def advance(date: DateMidnight, offsetAmount: Int, businessDayConvention: BusinessDayConvention): DateMidnight = {
    if (offsetAmount == 0) {
      forBusinessDayConvention(businessDayConvention).adjust(date, this)
    } else if (offsetAmount > 0) {
      val nextDate = date plusDays 1
      if (isWeekend(nextDate) || isHoliday(nextDate)) {
        advance(nextDate, offsetAmount, businessDayConvention)
      } else {
        advance(nextDate, offsetAmount - 1, businessDayConvention)
      }
    } else {
      val nextDate = date minusDays 1
      if (isWeekend(nextDate) || isHoliday(nextDate)) {
        advance(nextDate, offsetAmount, businessDayConvention)
      } else {
        advance(nextDate, offsetAmount + 1, businessDayConvention)
      }
    }
  }

  def adjustFinancialDate(date: DateMidnight, bdc: BusinessDayConvention): DateMidnight = {
    forBusinessDayConvention(bdc).adjust(date, this)
  }

  def forBusinessDayConvention(businessDayConvention: BusinessDayConvention): BusinessDayConventionAdjustment = {
    businessDayConvention match {
      case UNADJUSTED => UnadjustedAjustment
      case PRECEDING => PrecedingAdjustment
      case MODIFIED_PRECEDING => ModifiedPrecedingAdjustment
      case FOLLOWING => FollowingAdjustment
      case MODIFIED_FOLLOWING => ModifiedFollowingAdjustment
      case MONTH_END_REFERENCE => MonthEndReference
    }
  }

  def getBusinessDaysBetween(startDate: DateMidnight, endDate: DateMidnight): Long = {
    if (startDate >= endDate) {
      1
    } else {
      1 + getBusinessDaysBetween(advanceBusinessDays(startDate, 1), endDate)
    }
  }
  def advanceBusinessDay(date: DateMidnight, offsetAmount: Int): DateMidnight = advance(date, offsetAmount, UNADJUSTED)
}

