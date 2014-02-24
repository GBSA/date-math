package org.scalafin.datemath.utils

import org.joda.time.field.AbstractReadableInstantFieldProperty
import org.joda.time._
import org.scalafin.datemath._
import scala.annotation.tailrec
import org.scalafin.utils.Interval


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

trait Generifiers {


	implicit def generifyPaymentPeriod[A,B>:A](paymentPeriod:PaymentPeriod[A])(implicit ordering:Ordering[B]) = paymentPeriod.asMoreGeneric[B]

}




object OrderingImplicits extends OrderingImplicits

object RichJodaTimeExtensions {


  implicit class RichJodaTimeInstant(val start:ReadableInstant) extends AnyVal{

    def daysTo(end: ReadableDateTime): Long = {
	    val days = Days.daysBetween(start,end).getDays
	    println(days)
	    days
    }

  }


  implicit class RichJodaReadableDateTime(val readableDateTime:ReadableDateTime) extends AnyVal {

    implicit def asYearMonthDayTuple: (Int, Int, Int) =  (readableDateTime.getYearOfEra, readableDateTime.getMonthOfYear, readableDateTime.getDayOfMonth)

  }

  implicit class RichAbstractReadableProperty(val property:AbstractReadableInstantFieldProperty) extends AnyVal{

    def maxValue = property.getMaximumValue

  }

  implicit class RichDateIntervalProperty[A<:ReadableDateTime](val interval:Interval[A] ) extends AnyVal{

    import OrderingImplicits._

    final def businessDaysAccordingTo(implicit holidayCalendar:HolidayCalendar): Long = {

      import DateAdjustmentTools._

      @tailrec
      def getBusinessDaysBetween(start:ReadableDateTime,end:ReadableDateTime, counter:Int):Long ={
        if (start >= end) {
          counter + 1
        } else {
          val advancedResult = advance(start,holidayCalendar, 1,UnadjustedBusinessDayConvention)
          getBusinessDaysBetween(advancedResult, end, counter+1)
        }
      }
      // In an interval the start never follows the end, so we are sure the API call is correct
      getBusinessDaysBetween(interval.start,interval.end,0)

    }

  }

}
//
//trait ScalaFinDateMathImplicits extends OrderingImplicits {
//
//	implicit def toDateTime(readableDateTime:ReadableDateTime) = readableDateTime.toDateTime
//
//}
//
//object ScalaFinDateMathImplicits extends ScalaFinDateMathImplicits
//trait DateDsl extends OrderingImplicits{
//
//
//
//
//
//
//  def asDayStream(start: DateMidnight, end: DateMidnight): Stream[DateMidnight] = {
//    if (start > end)
//      Stream.empty[DateMidnight]
//    else
//      Stream.cons(start, asDayStream(start plusDays 1, end))
//  }
//
//}
//
//object DateDsl extends DateDsl
