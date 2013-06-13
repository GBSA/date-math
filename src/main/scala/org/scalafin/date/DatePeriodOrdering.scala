package org.scalafin.date

import org.scalafin.date.DateOrdering
import org.joda.time.DateMidnight

object DatePeriodOrderings {

  trait MidPointOrdering {
    implicit val ordering: Ordering[DatePeriod] = new Ordering[DatePeriod] {

      def getDoubleMidPoint(datePeriod: DatePeriod): Long = {
        datePeriod.dateRange.startDate.getMillis + datePeriod.dateRange.endDate.getMillis
      }

      def getDoubleMidPoint(dt: DateMidnight): Long = 2 * dt.getMillis

      def compare(x: DatePeriod, y: DatePeriod): Int = {
        val retval: Int = ((getDoubleMidPoint(x) - getDoubleMidPoint(y)) / 60000l).toInt
        if (retval == 0) {
          0
        } else {
          retval / Math.abs(retval)
        }
      }
    }
  }

  object MidPointOrdering extends MidPointOrdering

  // TODO ?
  // implement startPoint and endPoint ordering?
  
}



