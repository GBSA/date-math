package org.scalafin.datemath.utils

import org.joda.time.field.AbstractReadableInstantFieldProperty
import org.joda.time.{DateMidnight, ReadableInstant, ReadableDateTime}
import org.scalafin.datemath._
import scala.annotation.tailrec


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

  val millisInDay = 1000d * 60d * 60d * 24d

  implicit class RichJodaTimeInstant(val start:ReadableInstant) extends AnyVal{

    def to(end:ReadableInstant) = new org.joda.time.Interval(start,end)

    def daysTo(end: ReadableDateTime): Long = math.round(to(end).toDurationMillis / millisInDay)

  }


  implicit class RichJodaReadableDateTime(val readableDateTime:ReadableDateTime) extends AnyVal {

    implicit def asYearMonthDayTuple: (Int, Int, Int) =  (readableDateTime.getYearOfEra, readableDateTime.getMonthOfYear, readableDateTime.getDayOfMonth)

  }

  implicit class RichAbstractReadableProperty(val property:AbstractReadableInstantFieldProperty) extends AnyVal{

    def maxValue = property.getMaximumValue

  }

  implicit class RichDateIntervalProperty(val interval:DateInterval ) extends AnyVal{

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
trait DateDsl extends OrderingImplicits{



  implicit def toDateTime(readableDateTime:ReadableDateTime) = readableDateTime.toDateTime


  def asDayStream(start: DateMidnight, end: DateMidnight): Stream[DateMidnight] = {
    if (start > end)
      Stream.empty[DateMidnight]
    else
      Stream.cons(start, asDayStream(start plusDays 1, end))
  }

}

object DateDsl extends DateDsl
