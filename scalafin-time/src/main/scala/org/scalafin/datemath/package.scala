package org.scalafin

import org.scalafin.utils.Interval
import org.joda.time.{ReadableInstant, ReadableDateTime}

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 12/02/14
 * Time: 11:27
 *
 */
package object datemath {

  type DateInterval = Interval[ReadableDateTime]

  case class FinancialPeriod[T<:ReadableInstant] (actual: Interval[T], reference: Option[Interval[T]] = None) {

    override lazy val toString: String = {
      reference match{
        case Some(ref) => s"Actual: $actual , Reference: $ref"
        case None => actual.toString
      }
    }

  }


  trait Frequency {

    def add(amount: Int, date: ReadableDateTime): ReadableDateTime

  }

  trait ExactFitInYear {

    def periodsPerYear: Int

  }

  trait BusinessDayConvention {

    def adjust(dt:ReadableDateTime)(implicit hc:HolidayCalendar):ReadableDateTime

  }

  trait UnadjustedBusinessDayConvention extends BusinessDayConvention{

    override final def adjust(dt: ReadableDateTime)(implicit hc: HolidayCalendar): ReadableDateTime = dt

  }

  case object UnadjustedBusinessDayConvention extends UnadjustedBusinessDayConvention

  trait DayCountConvention{

    def calculator: DayCountCalculator

  }

  trait DayCountCalculator {

    def calculateDayCountFraction(period: FinancialPeriod[ReadableDateTime]): Double

    def apply(period: FinancialPeriod[ReadableDateTime]): Double = calculateDayCountFraction(period)

  }

  trait StubType


}
