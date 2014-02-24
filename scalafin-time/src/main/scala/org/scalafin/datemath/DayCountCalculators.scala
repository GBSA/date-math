package org.scalafin.datemath

import org.joda.time.{DateTime, ReadableDateTime,  DateTimeConstants}
import org.scalafin.utils.Interval
import org.scalafin.datemath.utils.OrderingImplicits._


object DayCountCalculators{

  import org.scalafin.datemath.utils.RichJodaTimeExtensions._

	private implicit def toDateTime(readableDateTime:ReadableDateTime) = readableDateTime.toDateTime


  trait NormalizedActualDayCountCalculator extends DayCountCalculator{
    
    def normalizationFactor:Double

    override def calculateDayCountFraction(period: PaymentPeriod[ReadableDateTime]): Double = {
	    val days = (period.actual.start daysTo period.actual.end)
	    println(days)
	    days / normalizationFactor
    }

  }

  case object Actual360DayCountCalculator extends NormalizedActualDayCountCalculator{
    override val normalizationFactor: Double = 360d
  }
  

  case object Actual365FixedDayCountCalculator extends NormalizedActualDayCountCalculator{
    override val normalizationFactor: Double = 365d
  }

  case object Actual366DayCountCalculator extends NormalizedActualDayCountCalculator{

    override val normalizationFactor: Double = 366d
    
  }

  case object AFBActualActualDayCountCalculator extends DayCountCalculator {

    private def previousDateBuilder(readableDateTime: ReadableDateTime) = {
      val date = readableDateTime.toDateTime
      date minusDays  date.dayOfYear.maxValue
    }

    private def february29InYear(year: Int): Option[DateTime] = {
      val dateTime = new DateTime(year, 1, 1)
      if (dateTime.dayOfYear.maxValue == 366)
        Some(dateTime.withMonthOfYear(2).withDayOfMonth(29))
      else
        None
    }

    private def diy(interval: Interval[ReadableDateTime]): Double = {
      val dates = interval.extremesAsSeq
      val februaries29 = dates.flatMap {
        date => february29InYear(date.getYear)
      }.filter{
               february29 =>
                february29  >= interval.start   && february29  <= interval.end }

      if (februaries29.isEmpty)
        365.0
      else
        366.0
    }

    private def tau(interval: Interval[ReadableDateTime]):Double = (interval.start daysTo interval.end) / diy(interval)


    override def calculateDayCountFraction(period: PaymentPeriod[ReadableDateTime]): Double = {
      val splittedPeriods = period.actual splitInArrear previousDateBuilder
      val splittedTaus = splittedPeriods map tau
      splittedTaus.sum
    }




  }

  case object EU30360DayCountCalculator extends DayCountCalculator {


    override def calculateDayCountFraction(period: PaymentPeriod[ReadableDateTime]): Double = {
      val (year1, month1, day1): (Int, Int, Int) = period.actual.start.asYearMonthDayTuple
      val (year2, month2, day2): (Int, Int, Int) = period.actual.end.asYearMonthDayTuple
      var numerator = 360 * (year2 - year1) + 30 * (month2 - month1) + day2 - day1
      if (day1 == 31) numerator += 1
      if (day2 == 31) numerator -= 1
      numerator / 360d
    }


  }

  case object ISDAActualActualDayCountCalculator extends DayCountCalculator {


    override def calculateDayCountFraction(period: PaymentPeriod[ReadableDateTime]): Double = {
      val splittedPeriods = period.actual splitInArrear previousDateBuilder
      val taus = splittedPeriods map tau
      taus.sum

    }


    private  def previousDateBuilder(readableDateTime: ReadableDateTime) = {
        val date = readableDateTime.toDateTime
        date minusDays  date.dayOfYear.maxValue
    }
    private def tau(dateRange: Interval[ReadableDateTime]):Double = {
      val maxDaysInFirstYear = dateRange.start.toDateTime.dayOfYear().maxValue
      (dateRange.start daysTo  dateRange.end) / maxDaysInFirstYear
    }



  }

  case object IT30360DayCountCalculator extends DayCountCalculator {


    override def calculateDayCountFraction(period: PaymentPeriod[ReadableDateTime]): Double = {
      val dateRange = period.actual
      val (year1, month1, day1): (Int, Int, Int) = dateRange.start.asYearMonthDayTuple
      val (year2, month2, day2): (Int, Int, Int) = dateRange.end.asYearMonthDayTuple
      val correctedDay1 = correctDay(day1, dateRange.start)
      val correctedDay2 = correctDay(day2, dateRange.end)
      (year2 - year1) + ((30 * (month2 - month1 - 1) + Math.max(0, 30 - correctedDay1) + Math.min(30, correctedDay2)) / 360.0d)
    }


    def isAfter27OfFebruary(date: ReadableDateTime): Boolean = {
      date.getDayOfMonth > 27 && date.getMonthOfYear == DateTimeConstants.FEBRUARY
    }

    def correctDay(dayInt: Int, date: ReadableDateTime): Int = if (isAfter27OfFebruary(date)) 30 else dayInt

  }

  case object US30360DayCountCalculator extends DayCountCalculator {


    override def calculateDayCountFraction(period: PaymentPeriod[ReadableDateTime]): Double = {
      val dateRange = period.actual
      val (year1, month1, _): (Int, Int, Int) = dateRange.start.asYearMonthDayTuple
      val (year2, month2, _): (Int, Int, Int) = dateRange.end.asYearMonthDayTuple
      val februaryAdjustedDays = februaryAdjustedDates(dateRange)
      val (correctedDay1, correctedDay2) = adjustEomDates(februaryAdjustedDays)
      val numerator = 360 * (year2 - year1) + 30 *(month2-month1) +(correctedDay2 - correctedDay1)
      numerator / 360d
    }

    private def isLastDayOfFebruary(date: ReadableDateTime): Boolean = {
      date.getDayOfMonth == date.dayOfMonth.maxValue && date.getMonthOfYear == DateTimeConstants.FEBRUARY
    }

    private def februaryAdjustedDates(dateRange: Interval[ReadableDateTime]): (Int, Int) = {
      if (isLastDayOfFebruary(dateRange.start))
        if (isLastDayOfFebruary(dateRange.end))
          (30, 30)
        else (30, dateRange.end.getDayOfMonth)
      else
        (dateRange.start.getDayOfMonth, dateRange.end.getDayOfMonth)
    }

    private def adjustEomDates(days: (Int, Int)): (Int, Int) = {

	    def adjust1(dayOfMonths:(Int,Int)):(Int,Int) = {
		    if(dayOfMonths._2==31 && dayOfMonths._1 >=30)
			    (dayOfMonths._1,30)
		    else
			    dayOfMonths
	    }

	    def adjust2(dayOfMonths:(Int,Int)):(Int,Int) = {
		    if(dayOfMonths._1==31)
			    (30,dayOfMonths._2)
		    else
			    dayOfMonths

	    }
	    (adjust1 _ andThen adjust2)(days)
    }

  }

  case class Business252DayCountCalculator(hc: HolidayCalendar) extends DayCountCalculator{

    override def calculateDayCountFraction(period: PaymentPeriod[ReadableDateTime]): Double = period.actual.businessDaysAccordingTo(hc) / 252.0

  }



  case class ISMAActualActualDayCountCalculator(frequency: Frequency with ExactFitInYear) extends DayCountCalculator {


    override def calculateDayCountFraction(period: PaymentPeriod[ReadableDateTime]): Double = {
      val dateRange = period.actual
      val firstCouponDate = frequency add (1, dateRange.start)
      val daysInPeriod = dateRange.start daysTo firstCouponDate
      val daysInRange: Double = dateRange.start daysTo dateRange.end
      daysInRange / (frequency.periodsPerYear * daysInPeriod)
    }


  }

}

