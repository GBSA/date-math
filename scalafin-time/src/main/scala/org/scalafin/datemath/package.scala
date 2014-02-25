package org.scalafin

import org.scalafin.utils.{IntervalBuilder, Interval}
import org.joda.time._
import scalaz.std.math.ordering
import scalaz.Validation
import scala.Some
import scala.concurrent.duration.Duration
import org.joda.time.Duration
import scala.annotation.tailrec

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 12/02/14
 * Time: 11:27
 *
 */
package object datemath {

  type DateInterval = Interval[ReadableDateTime]

	trait PaymentPeriod[A]{

		self =>

		def actual:Interval[A]

		def reference: Option[Interval[A]]

		def asMoreGeneric[B>:A](implicit ordering:Ordering[B]) = new PaymentPeriod[B] {

			def actual = self.actual.asMoreGeneric[B]

			def reference = self.reference.map{_.asMoreGeneric[B]}
		}

	}

	trait PaymentPeriodBuilder{

		def build[A](actual: Interval[A], reference: Option[Interval[A]]):PaymentPeriod[A]

	}

	trait SimplePaymentPeriodBuilder extends PaymentPeriodBuilder{

		def build[A](actual1: Interval[A], reference1: Option[Interval[A]]):PaymentPeriod[A]={

			new PaymentPeriod[A] {

				override def reference: Option[Interval[A]] = reference1

				override def actual: Interval[A] = actual1

				}

		}

	}

	object SimplePaymentPeriodBuilder extends SimplePaymentPeriodBuilder
	
  case class ScheduledFinancialPeriod[T<:ReadableInstant] (actual: Interval[T], reference: Option[Interval[T]] = None) extends PaymentPeriod[T] {

	  override lazy val toString: String = {
		  reference match{
			  case Some(ref) => s"Actual: $actual , Reference: $ref"
			  case None => actual.toString
		  }
	  }


  }


  trait Frequency {

	  self =>

	  protected def asPeriod: ReadablePeriod

	  private implicit def toDateTime(readableDateTime:ReadableDateTime) = readableDateTime.toDateTime

    def addTo(date: ReadableDateTime): ReadableDateTime = date plus asPeriod

	  def subtractFrom(date:ReadableDateTime):ReadableDateTime = date minus asPeriod

	  def divide(factor:Int):Frequency =   new Frequency {

		  override protected def asPeriod: ReadablePeriod =  {
			  val initialPeriod = self.asPeriod
			  @tailrec
			  def add(count:Int, period:Period):Period = {
				  if(count==0)
					  period
				  else add(count-1, period plus initialPeriod)
			  }
			  add(factor,initialPeriod.toPeriod)

		  }

	  }

  }

  trait ExactFitInYear {

    def periodsPerYear: Int

  }

  trait BusinessDayConvention {

    def adjust(dt:ReadableDateTime)(implicit hc:HolidayCalendar):ReadableDateTime

  }

  trait UnadjustedBusinessDayConvention extends BusinessDayConvention{

    override final def adjust(dt: ReadableDateTime)(implicit hc: HolidayCalendar): DateTime = dt.toDateTime

  }

  case object UnadjustedBusinessDayConvention extends UnadjustedBusinessDayConvention

  trait DayCountConvention{

    def calculator: DayCountCalculator

  }

  trait DayCountCalculator {

    def calculateDayCountFraction(period: PaymentPeriod[ReadableDateTime]): Double

    def apply(period: PaymentPeriod[ReadableDateTime]): Double = calculateDayCountFraction(period)

  }

  trait StubType


}
