package org.scalafin.date.daycount

import org.joda.time.DateMidnight
import org.scalafin.date.DateRange
import org.scalafin.date.holiday.HolidayCalendar
import com.github.nscala_time.time.Imports._
import org.joda.time.DateTimeConstants
import org.scalafin.date.Frequency
import org.scalafin.date.ExactFitInYear
import org.scalafin.date.DateOps
import scalaz.Functor

object DaycountCalculators extends DateOps {

  case object Actual360DaycountCalculator extends SimpleDaycountCalculator(dateRange => daysBetween(dateRange.startDate, dateRange.endDate) / 360.0d)

  case object Actual365FixedDaycountCalculator extends SimpleDaycountCalculator(dateRange => daysBetween(dateRange.startDate, dateRange.endDate) / 365.0d)

  case object Actual366DaycountCalculator extends SimpleDaycountCalculator(dateRange => daysBetween(dateRange.startDate, dateRange.endDate) / 366.0d)

  case object AFBActualActualDaycountCalculator extends DaycountCalculator {

    def previousDateBuilder(date: DateMidnight) = date minus (date.dayOfYear.maximumValue)

    def february29InYear(year: Int): Option[DateMidnight] = {
      val dateMidnight = new DateMidnight(year, 1, 1)
      if (dateMidnight.dayOfYear.maximumValue == 366)
        Some(dateMidnight.withMonthOfYear(2).withDayOfMonth(29))
      else
        None
    }

    def diy(dateRange: DateRange): Double = {
      val dates = dateRange extremesAsSeq
      val februaries29 = dates.flatMap {
        date => february29InYear(date.getYear)
      }.filter(february29 => february29 >= dateRange.startDate && february29 <= dateRange.endDate)
      if (februaries29.isEmpty)
        365.0
      else
        366.0
    }

    def tau(dateRange: DateRange) = daysBetween(dateRange.startDate, dateRange.endDate) / diy(dateRange)

    def calculateDaycountFraction(dateRange: DateRange): Double = {
      val splittedPeriods = dateRange splitInArrear previousDateBuilder
      val splittedTaus = splittedPeriods map tau
      splittedTaus.sum
    }

  }

  case object EU30360DaycountCalculator extends DaycountCalculator {

    def calculateDaycountFraction(dateRange: DateRange): Double = {
      val (year1, month1, day1): (Int, Int, Int) = dateRange.startDate
      val (year2, month2, day2): (Int, Int, Int) = dateRange.endDate
      var numerator = 360 * (year2 - year1) + 30 * (month2 - month1) + day2 - day1
      if (day1 == 31) numerator += 1
      if (day2 == 31) numerator -= 1
      numerator / 360d
    }

  }

  case object ISDAActualActualDaycountCalculator extends DaycountCalculator {

    def previousDateBuilder(date: DateMidnight) = date minus (date.getDayOfYear)

    def tau(dateRange: DateRange) = daysBetween(dateRange.startDate, dateRange.endDate) / dateRange.startDate.dayOfYear.getMaximumValue

    def calculateDaycountFraction(dateRange: DateRange): Double = {
      val splittedPeriods = dateRange splitInArrear previousDateBuilder
      val taus = splittedPeriods map tau
      taus.sum
    }

  }

  case object IT30360DaycountCalculator extends DaycountCalculator {

    def calculateDaycountFraction(dateRange: DateRange): Double = {
      val (year1, month1, day1): (Int, Int, Int) = dateRange.startDate
      val (year2, month2, day2): (Int, Int, Int) = dateRange.endDate
      val correctedDay1 = correctDay(day1, dateRange.startDate)
      val correctedDay2 = correctDay(day2, dateRange.endDate)
      (year2 - year2) + ((30 * (month2 - month2 - 1) + Math.max(0, 30 - correctedDay1) + Math.min(30, correctedDay2)) / 360.0d)
    }

    def isAfter27OfFebruary(date: DateMidnight): Boolean = {
      date.getDayOfMonth > 27 && date.monthOfYear == DateTimeConstants.FEBRUARY
    }

    def correctDay(dayInt: Int, date: DateMidnight): Int = if (isAfter27OfFebruary(date)) 30 else dayInt

  }

  case object US30360DaycountCalculator extends DaycountCalculator {

    def calculateDaycountFraction(dateRange: DateRange): Double = {
      val (year1, month1, day1): (Int, Int, Int) = dateRange.startDate
      val (year2, month2, day2): (Int, Int, Int) = dateRange.endDate
      val februaryAdjustedDays = februaryAdjustedDates(dateRange)
      val (correctedDay1, correctedDay2) = adjustEomDates(februaryAdjustedDays)

      val numerator = 360 * (year2 - year1) + 30 * (correctedDay2 - correctedDay1) + (day2 - day1)

      numerator / 360d
    }

    def isLastDayOfFebruary(date: DateMidnight): Boolean = {
      date.getMonthOfYear == date.dayOfMonth.maximumValue && date.monthOfYear == DateTimeConstants.FEBRUARY
    }

    def februaryAdjustedDates(dateRange: DateRange): (Int, Int) = {
      if (isLastDayOfFebruary(dateRange.startDate))
        if (isLastDayOfFebruary(dateRange.endDate))
          (30, 30)
        else (30, dateRange.endDate.getDayOfMonth)
      else
        (dateRange.startDate.getDayOfMonth, dateRange.endDate.getDayOfMonth)
    }

    def adjustEomDates(days: (Int, Int)): (Int, Int) = {
      val (correctedDay1, correctedDay2) = days
      if (correctedDay2 == 31 && correctedDay1 >= 30) {
        if (correctedDay1 == 31) {
          (30, 30)
        } else
          (correctedDay1, 30)
      } else
        days
    }

  }

  case class Business252DaycountCalculator(hc: HolidayCalendar) extends SimpleDaycountCalculator(dateRange => hc.getBusinessDaysBetween(dateRange.startDate, dateRange.endDate) / 252.0)

  case class ISMAActualActualDaycountCalculator(frequency: Frequency with ExactFitInYear) extends DaycountCalculator {

    def calculateDaycountFraction(dateRange: DateRange) = {

      val firstCouponDate = frequency add (1, dateRange.startDate)

      val daysInPeriod: Double = daysBetween(dateRange.startDate, firstCouponDate)

      val daysInRange: Double = daysBetween(dateRange.startDate, dateRange.endDate)
      daysInRange / (frequency.periodsPerYear * daysInPeriod)
    }
  }

}

