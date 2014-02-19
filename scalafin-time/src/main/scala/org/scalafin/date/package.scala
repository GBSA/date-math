package org.scalafin



package object date {








//  trait ambaracada {
//
//    trait DatePeriodOps[A, B <: Interval[A]] extends IntervalSplitter{
//      def self: B
//
////      def split[C>:A,D[+_]<:Interval[_]](splitPoint: C)(implicit intervalBuilder:IntervalBuilder[D]): Validation[InvalidIntervalException, ( D[C],  D[C])] = super.split(self, splitPoint)
////
////      def cut[C>:A,D[+_]<:Interval[_]](dates: Seq[C])(implicit intervalBuilder:IntervalBuilder[D]):Validation[Exception, Seq[D[C]]] = super.cut(self, dates)
//
//    }
//
//    trait DatePeriodOpsFunc {
//      implicit def toDatePeriodOps[A,B<:Interval[A]](t: B) = new DatePeriodOps[A,B] {
//        val self = t
//
//      }
//    }
//
//    object DatePeriodOpsFunc extends DatePeriodOpsFunc
//  }

  
}
