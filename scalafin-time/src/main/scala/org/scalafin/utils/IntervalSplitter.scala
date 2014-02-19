package org.scalafin.utils


import scalaz.{Success, Failure,  Validation}


/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 10/02/14
 * Time: 11:47
 *
 */
trait IntervalSplitter {

  private def unsafeSplit[A,C[+_]<:Interval[_]](period:  Interval[A], splitInstant: A)(implicit intervalBuilder:IntervalBuilder[C]): (C[A], C[A]) = {
    split(period,splitInstant) match{
      case Success(successTuple) => successTuple
      case Failure(x)  => throw x
    }
  }

  def split[A, B[+_]<:Interval[_]](interval:  Interval[A], splitPoint: A)(implicit intervalBuilder:IntervalBuilder[B]): Validation[InvalidIntervalException, ( B[A],  B[A])] ={
    import interval._
    for {
      period1 <- intervalBuilder apply (interval.start, splitPoint)
      period2 <- intervalBuilder apply (splitPoint,interval.end)
    } yield (period1,period2)
  }

  def cut[A[+_] <: Interval[_],B,C>:B, D[+_] <:Interval[_] forSome {type Z} ](interval: A[B], splitAt: Seq[C])(implicit intervalBuilder:IntervalBuilder[D]): Validation[Exception,Seq[D[C]]] = {

    null


//    //@tailrec
//    def splitPeriods(period: Interval[C], currentSplitPoint: C, nextSplitInstants: Iterator[C], previousIntervals: Seq[D[C]]): Seq[D[C]] = {
//      implicit val cOrdering = intervalBuilder.ordering
//      import Ordering.Implicits._
//      val (subInterval1, subInterval2) = unsafeSplit[C,D](period, currentSplitPoint)
//
//      if (!nextSplitInstants.hasNext)
//        previousIntervals ++ Seq(subInterval1,subInterval2)
//      else {
//        val nextSplitPoint = nextSplitInstants.next()
//        val (toSplit, toKeep) = {
//          val c:C = subInterval1.start
//          if (subInterval1.start > nextSplitPoint) (subInterval1, subInterval2) else (subInterval2, subInterval1)
//        }
//        splitPeriods(toSplit, nextDate, dateSplit, previousPeriods :+ toKeep)
//      }
//    }
//    if (splitAt.isEmpty)
//      Seq(period)
//    else {
//      val failingPoint = splitAt.find( splitPoint  => splitPoint > interval.end || splitPoint < interval.start )
//      failingPoint match{
//        case Some(x) => Failure(new Exception(s"The sequence of split points contains $x ,outside X"))
//        case None =>
//          val iterator = splitAt.sorted.iterator
//          Success(splitPeriods(period, iterator.next(), iterator, Seq.empty[D[C]]))
//
//      }

//    }
  }

}
