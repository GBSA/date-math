package org.scalafin.date

import org.joda.time.DateMidnight
import com.github.nscala_time.time.Imports._

trait DateOps {

  def daysBetween(d1: DateMidnight, d2: DateMidnight): Long = Math.round((d1 to d2).millis / (1000d * 60d * 60d * 24d))
  
  implicit def explodeDate(date: DateMidnight): (Int, Int, Int) = {
    (date.getYearOfEra, date.getMonthOfYear, date.getDayOfMonth)
  }
}

object DateOps extends DateOps