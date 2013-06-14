package org.scalafin.date

import org.joda.time.DateMidnight

object DatePeriodOrderings {

  trait MidPointOrdering {
    implicit val ordering: Ordering[DatePeriod] = new Ordering[DatePeriod] {
      def getDoubleMidPoint(datePeriod: DatePeriod): Long = {
        (datePeriod.dateRange.startDate.getMillis + datePeriod.dateRange.endDate.getMillis) / 2
      }
      def getDoubleMidPoint(dt: DateMidnight): Long = dt.getMillis
      def compare(x: DatePeriod, y: DatePeriod): Int = {
        (getDoubleMidPoint(x) - getDoubleMidPoint(y)).toInt
      }
    }
  }
  object MidPointOrdering extends MidPointOrdering

  trait StartPointOrdering {
    implicit val ordering: Ordering[DatePeriod] = new Ordering[DatePeriod] {
      def compare(x: DatePeriod, y: DatePeriod): Int = {
        (x.dateRange.startDate.getMillis - y.dateRange.startDate.getMillis).toInt
      }
    }
  }
  object StartPointOrdering extends StartPointOrdering

  trait EndPointOrdering {
    implicit val ordering: Ordering[DatePeriod] = new Ordering[DatePeriod] {
      def compare(x: DatePeriod, y: DatePeriod): Int = {
        (x.dateRange.endDate.getMillis - y.dateRange.endDate.getMillis).toInt
      }
    }
  }
  object EndPointOrdering extends EndPointOrdering
}



