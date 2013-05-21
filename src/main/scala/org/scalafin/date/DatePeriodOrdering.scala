package org.scalafin.date

import org.scalafin.date.util.DateMidnightOrdering
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

  //
  //  trait StartDateOrdering {
  //    implicit val ordering: Ordering[DatePeriod] = new Ordering[DatePeriod] {
  //      def compare(x: DatePeriod, y: DatePeriod): Int = DateMidnightOrdering.ordering.compare(x.startDate, y.startDate)
  //    }
  //  }
  //
  //  object StartDateOrdering extends StartDateOrdering
  //
  //  trait EndDateOrdering {
  //    implicit val ordering: Ordering[DatePeriod] = new Ordering[DatePeriod] {
  //      def compare(x: DatePeriod, y: DatePeriod): Int = DateMidnightOrdering.ordering.compare(x.endDate, y.endDate)
  //    }
  //  }
  //
  //  object EndDateOrdering extends EndDateOrdering
  //
}



