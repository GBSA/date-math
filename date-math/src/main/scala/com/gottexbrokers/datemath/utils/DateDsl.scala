package com.gottexbrokers.datemath.utils

import org.joda.time.field.AbstractReadableInstantFieldProperty
import org.joda.time._

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 18/02/14
 * Time: 14:41
 *
 */

trait OrderingImplicits {

  implicit def comparableToInfixOps[A, B <: Comparable[B]](x:A)(implicit ev: A <:< B) :scala.math.Ordering[B]#Ops = {
    val ordering = scala.math.Ordering.ordered[B]
    new ordering.Ops(x)
  }

  implicit def comparableOrderBy[A,B<:Comparable[B]](implicit ev: A <:< B): scala.math.Ordering[A] = scala.math.Ordering.by[A, B](ev)

  implicit def readableInstantSubClassOrdering[A<:ReadableInstant] = comparableOrderBy[A,ReadableInstant]

  implicit def readableInstantSubclassToInfixOps[A](a:A)(implicit ev: A <:< ReadableInstant) :scala.math.Ordering[ReadableInstant]#Ops = comparableToInfixOps[A,ReadableInstant](a)

}




object OrderingImplicits extends OrderingImplicits

object RichJodaTimeExtensions {


  implicit class RichJodaTimeInstant(val start:ReadableInstant) extends AnyVal{

    def daysTo(end: ReadableDateTime): Long =  Days.daysBetween(start,end).getDays

  }


  implicit class RichJodaReadableDateTime(val readableDateTime:ReadableDateTime) extends AnyVal {

    implicit def asYearMonthDayTuple: (Int, Int, Int) =  (readableDateTime.getYearOfEra, readableDateTime.getMonthOfYear, readableDateTime.getDayOfMonth)

  }

  implicit class RichAbstractReadableProperty(val property:AbstractReadableInstantFieldProperty) extends AnyVal{

    def maxValue = property.getMaximumValue

  }





}
