package org.scalafin.date.daycount

import com.github.nscala_time.time.Imports._
import org.scalafin.date.DatePeriod
import org.joda.time.DateMidnight
import scalaz.Validation
import org.scalafin.date.DateRangeException
import org.scalafin.date.DateRange
import org.scalafin.date.holiday.HolidayCalendar
import org.scalafin.date.DateOps

trait DaycountCalculator extends DateOps {
  
  def calculateDaycountFraction(dateRange: DateRange): Double
  
  def calculateDaycountFraction(period: DatePeriod): Double = calculateDaycountFraction(period.dateRange)
  
  def apply(period:DatePeriod):Double = calculateDaycountFraction(period)
  
  def apply(dateRange:DateRange):Double = calculateDaycountFraction(dateRange)

  
  
  
}

class SimpleDaycountCalculator(val f: DateRange => Double) extends DaycountCalculator{
  
    def calculateDaycountFraction(dateRange: DateRange): Double = f(dateRange)
    
}


