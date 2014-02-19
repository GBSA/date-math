package org.scalafin.datemath.utils

import org.joda.time.ReadableInstant
import org.scalafin.utils.Interval


object DatePeriodOrderings {

  trait MidPointOrdering {

    implicit def ordering[A<:ReadableInstant]:Ordering[Interval[A]] = new Ordering[Interval[A]]{

      def midOf(interval: Interval[A]): Long = {
        (interval.start.getMillis  + interval.end.getMillis) / 2
      }

      override def compare(x: Interval[A], y: Interval[A]): Int = {
        (midOf(x) - midOf(y)).toInt
      }
    }

  }

  object MidPointOrdering extends MidPointOrdering

  trait StartPointOrdering {

    implicit def ordering[A<:ReadableInstant]:Ordering[Interval[A]] = new Ordering[Interval[A]]{
      override def compare(x: Interval[A], y: Interval[A]): Int = {
        x.start compareTo y.start
      }

    }
  }
  object StartPointOrdering extends StartPointOrdering

  trait EndPointOrdering {
    implicit def ordering[A<:ReadableInstant]:Ordering[Interval[A]] = new Ordering[Interval[A]]{
      override def compare(x: Interval[A], y: Interval[A]): Int = {
        x.end compareTo y.end
      }

    }
  }
  object EndPointOrdering extends EndPointOrdering
}



