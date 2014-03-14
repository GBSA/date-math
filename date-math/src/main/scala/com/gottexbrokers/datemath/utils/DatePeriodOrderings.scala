package com.gottexbrokers.datemath.utils

import org.joda.time.ReadableInstant
import com.gottexbrokers.datemath.Period


object DatePeriodOrderings {

  trait MidPointOrdering {

    implicit def ordering[A<:ReadableInstant]:Ordering[Period[A]] = new Ordering[Period[A]]{

      def midOf(interval: Period[A]): Long = {
        (interval.start.getMillis  + interval.end.getMillis) / 2
      }

      override def compare(x: Period[A], y: Period[A]): Int = {
        (midOf(x) - midOf(y)).toInt
      }
    }

  }

  object MidPointOrdering extends MidPointOrdering

  trait StartPointOrdering {

    implicit def ordering[A<:ReadableInstant]:Ordering[Period[A]] = new Ordering[Period[A]]{
      override def compare(x: Period[A], y: Period[A]): Int = {
        x.start compareTo y.start
      }

    }
  }
  object StartPointOrdering extends StartPointOrdering

  trait EndPointOrdering {
    implicit def ordering[A<:ReadableInstant]:Ordering[Period[A]] = new Ordering[Period[A]]{
      override def compare(x: Period[A], y: Period[A]): Int = {
        x.end compareTo y.end
      }

    }
  }
  object EndPointOrdering extends EndPointOrdering
}



