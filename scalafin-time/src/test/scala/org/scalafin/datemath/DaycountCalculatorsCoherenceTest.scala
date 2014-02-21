package org.scalafin.datemath

import com.mbc.jfin.daycount.impl.{DaycountCalculator => JFinDaycountCalculator}
import com.mbc.jfin.daycount.impl.calculator._
import org.scalafin.datemath.DayCountCalculators._
import org.specs2.{ScalaCheck, Specification}
import org.scalafin.datemath.test._
import org.specs2.specification.Fragments
import org.scalacheck.Prop
import org.joda.time._
import org.scalacheck.util.Pretty
import org.scalafin.utils.{Interval, DefaultIntervalBuilder, IntervalGenerators}
import org.scalafin.datemath.utils.OrderingImplicits
import org.specs2.matcher.Parameters

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 19/02/14
 * Time: 15:54
 *
 */



class DayCountCalculatorsCoherenceTest extends Specification
                                                   with ScalaCheck
                                                   with FragmentBuildingTools
                                                   with ScheduledFinancialPeriodGenerators
                                                   with ScalafinDateMathTestInstances
                                                   with DayCountConventionTupleMatchers
                                                   with JodaTimeGenerators
                                                   with IntervalGenerators
                                                   with OrderingImplicits
																									 with FromAmericanDiscoveryToJupiter{


	implicit val params =  Parameters(workers=4)

	override val defaultPrettyParams = Pretty.Params(2)

	def periodIsNotTooLongForJoda(paymentPeriod:PaymentPeriod[DateMidnight]):Boolean  ={

		def intervalIsNotTooLongForJoda(interval:Interval[DateMidnight]):Boolean = {

			math.abs( (interval.end.getMillis - interval.start.getMillis)/DateTimeConstants.MILLIS_PER_DAY)  < Int.MaxValue
		}
		intervalIsNotTooLongForJoda(paymentPeriod.actual) &&  (paymentPeriod.reference map intervalIsNotTooLongForJoda).getOrElse(true)
	}

	// Shows for easier debugging

	import org.scalafin.datemath.utils._
	import IntervalShows._
	import MillisShows._
	import PaymentPeriodShows._





	def daysIn(paymentPeriod:PaymentPeriod[DateMidnight]):Int = {
		def days(interval:Interval[DateMidnight]):Int = Days.daysBetween(interval.start,interval.end).getDays
		math.max(days(paymentPeriod.actual) , (paymentPeriod.reference map days).getOrElse(0))
	}



	def testTuple(filteringCondition:PaymentPeriod[DateMidnight] => Boolean)(tuple: (DayCountCalculator, JFinDaycountCalculator)) = {
		val (scalafinDateMathCalculator, jfinConvention) = tuple
		val exampleName = s"jfin.$jfinConvention and scalafin-datemath.$scalafinDateMathCalculator must compute the same value "
		exampleName ! Prop.forAll {
			(period: PaymentPeriod[DateMidnight]) => (  periodIsNotTooLongForJoda(period) && filteringCondition(period) ) ==> Prop.collect(daysIn(period)){
				tuple must computeIdenticalDayCountFor(period)
			}

		}
	}

	val tuple1 =  Actual360DayCountCalculator -> new Actual360DaycountCalculator()

	val tuple2 = Actual365FixedDayCountCalculator ->  new Actual365FixedDaycountCalculator()

	val tuple3 = Actual366DayCountCalculator -> new Actual366DaycountCalculator()

	val tuple4 = EU30360DayCountCalculator -> new EU30360DaycountCalculator()

	val tuple5 = IT30360DayCountCalculator -> new IT30360DaycountCalculator()

	val tuple6 = US30360DayCountCalculator -> new US30360DaycountCalculator()

	val nonSplittingDayCountCalculator = Seq(tuple1, tuple2, tuple3, tuple4, tuple5, tuple6, tuple6)

	val tuple7 = AFBActualActualDayCountCalculator -> new AFBActualActualDaycountCalculator()

	val splittingDayCountCalculator = Seq(tuple4)

	implicit val intervalBuilder = DefaultIntervalBuilder

	implicit val periodGen =  arbitraryFinancialPeriodWithNoReference[DateMidnight]

	val isPeriodShort = (paymentPeriod:PaymentPeriod[DateMidnight]) => daysIn(paymentPeriod) < 3650

	override def is: Fragments = testChunk(nonSplittingDayCountCalculator, "The non-splitting day count calculator should be equivalent between Jfin and scalafin-datemath", testTuple(_ => true) _) ^
															testChunk(splittingDayCountCalculator, "The splitting day count calculators should be equivalent between Jfin and scalafin-datemath", testTuple(isPeriodShort) _)



}



