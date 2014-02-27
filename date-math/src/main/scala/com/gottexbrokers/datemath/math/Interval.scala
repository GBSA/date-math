package com.gottexbrokers.datemath.math

import scalaz.{Failure, Success, Validation}
import Ordering.Implicits._
import scala.annotation.tailrec


/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 10/02/14
 * Time: 11:10
 *
 */

sealed trait Interval[A]{


  type Repr[X] <: Interval[X]

  def extremesAsSeq = Seq(start,end)

  def start:A

  def end:A

  implicit def selfBuilder: IntervalBuilder[Repr]

  implicit def ordering:Ordering[A]

  def contains[B>:A](point: B)(implicit ordering:Ordering[B]): Boolean = start < point && end > point

  def splitInArrear[B<:A](previousEndGenerator: (A) => B): Seq[Interval[A]] = {
	  @tailrec
	  def splitInArrear(currentInterval:Interval[A], currentSplits:Seq[Interval[A]]):Seq[Interval[A]] = {
		  val previousInstant = previousEndGenerator(end)
		  require(previousInstant < end, "The generator does not generate new dates in arrear, the loop will last forever")
		  if (previousInstant <= start)
			  currentInterval +: currentSplits
		  else {
			  val lastPartOfCurrentInterval = selfBuilder unsafe (previousInstant, end)
			  val firstPartOfCurrentInterval = selfBuilder unsafe (start, previousInstant)
			  splitInArrear(firstPartOfCurrentInterval, lastPartOfCurrentInterval +: currentSplits)
		  }
	  }
	  splitInArrear(this, Seq.empty[Interval[A]])


  }

  implicit def asMoreGeneric[B>:A](implicit ordering:Ordering[B]) = selfBuilder.unsafe[B](start,end)

   def copy(newStart: Option[A] = None, newEnd: Option[A] = None): Validation[InvalidIntervalException, Interval[A]] = {
     selfBuilder apply (newStart.getOrElse(start), newEnd.getOrElse(end))
   }





}



sealed trait IntervalBuilder[+A[_]<:Interval[_]]{

  self =>




  final def apply[B](start:B,end:B)(implicit ordering:Ordering[B]):Validation[InvalidIntervalException, A[B]] =
    if(start<=end)
    Success(unsafe(start,end))
  else
    Failure(InvalidIntervalException(s"$start does not preceed $end"))

  private [datemath] def unsafe[B](s:B,e:B)(implicit ordering:Ordering[B]):A[B]

}

trait DefaultIntervalBuilder extends IntervalBuilder[Interval]{
  self =>


  override private[datemath] def unsafe[B](s: B, e: B)(implicit ordering1: Ordering[B]): Interval[B] = new Interval[B]{

    type Repr[X] = Interval[X]

    override implicit def selfBuilder: IntervalBuilder[Interval] = self

    override val start = s

    override val end = e

    override implicit def ordering: Ordering[B] = ordering1

    override def equals(other:Any):Boolean = {
      other match {
        case otherInterval:Interval[_] => otherInterval.start == start && otherInterval.end == end && selfBuilder == otherInterval.selfBuilder // Ordering will be implied by this
        case _ => false
      }
    }

    override def toString = s"$start to $end"
  }


}

object DefaultIntervalBuilder  extends DefaultIntervalBuilder{


}

case class InvalidIntervalException(message: String) extends Exception(message)
