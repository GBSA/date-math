package org.scalafin.datemath

import com.mbc.jfin.daycount.impl.{DaycountCalculator => JFinDaycountCalculator}
import com.mbc.jfin.daycount.impl.calculator._
import org.scalafin.datemath.DayCountCalculators._
import org.specs2.{ScalaCheck, Specification}
import org.scalafin.datemath.test._
import org.specs2.specification.{Example, Fragments}
import org.scalacheck.{Gen, Arbitrary, Prop}
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


	implicit val params =  Parameters(workers=4,minTestsOk = 1000,maxSize = 5000)

	override val defaultPrettyParams = Pretty.Params(2)

	def periodIsNotTooLongForJoda(paymentPeriod:PaymentPeriod[DateMidnight]):Boolean  ={

		def intervalIsNotTooLongForJoda(interval:Interval[DateMidnight]):Boolean = {

			math.abs( (interval.end.getMillis - interval.start.getMillis)/DateTimeConstants.MILLIS_PER_DAY)  < Int.MaxValue
		}
		intervalIsNotTooLongForJoda(paymentPeriod.actual) &&  (paymentPeriod.reference map intervalIsNotTooLongForJoda).getOrElse(true)
	}

	// Shows for easier debugging

	import org.scalafin.datemath.utils._
	import MillisShows._
	import IntervalShows._
	import PaymentPeriodShows._

	def daysIn(paymentPeriod:PaymentPeriod[DateMidnight]):Int = {
		def days(interval:Interval[DateMidnight]):Int = Days.daysBetween(interval.start,interval.end).getDays
		math.max(days(paymentPeriod.actual) , (paymentPeriod.reference map days).getOrElse(0))
	}



	def testTuple(tuple: (DayCountCalculator, JFinDaycountCalculator))(implicit arbitrary:Arbitrary[PaymentPeriod[DateMidnight]]):Example = {
		val (scalafinDateMathCalculator, jfinConvention) = tuple
		val exampleName = s"jfin.$jfinConvention and scalafin-datemath.$scalafinDateMathCalculator must compute the same value "
		exampleName ! Prop.forAll {
			(period: PaymentPeriod[DateMidnight]) => periodIsNotTooLongForJoda(period) ==> Prop.collect(daysIn(period)){
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

	val nonSplittingDayCountCalculator = Seq(tuple1, tuple2, tuple3, tuple4, tuple5, tuple6)

	val tuple7 = AFBActualActualDayCountCalculator -> new AFBActualActualDaycountCalculator()

	val splittingDayCountCalculator = Seq(tuple7)

	implicit val intervalBuilder = DefaultIntervalBuilder

	def testNotSplitting:Fragments = {
		import IndepedendentExtremesIntervalGenerator._
		val periodGen =  arbitraryFinancialPeriodWithNoReference[DateMidnight]
		 val arbitrary = Arbitrary{
			periodGen.arbitrary filter periodIsNotTooLongForJoda
		}
		testChunk(nonSplittingDayCountCalculator, "The non-splitting day count calculator should be equivalent between Jfin and scalafin-datemath", testTuple(_: (DayCountCalculator, JFinDaycountCalculator))(arbitrary))

	}


	def testSplitting: Fragments = {
		val maxMillisTenYears = Years.years(10) get DurationFieldType.millis()
		implicit val millisGen = Gen.choose(0L,maxMillisTenYears)
		implicit val adder = (d1:DateMidnight, millis:Long) => d1 plus millis
		import MaxSizedGenerator._
		val periodGen =  arbitraryFinancialPeriodWithNoReference[DateMidnight]
		val arbitrary = Arbitrary{
			periodGen.arbitrary filter periodIsNotTooLongForJoda
		}
		testChunk(splittingDayCountCalculator, "The non-splitting day count calculator should be equivalent between Jfin and scalafin-datemath", testTuple(_: (DayCountCalculator, JFinDaycountCalculator))(arbitrary))

	}



	//override def is: Fragments = testNotSplitting ^ testSplitting

	override def is: Fragments = testKnownCase


	def testKnownCase:Example = {
		val tuple = tuple3
		val (scalafinDateMathCalculator, jfinConvention) = tuple
		val exampleName = s"jfin.$jfinConvention and scalafin-datemath.$scalafinDateMathCalculator must compute the same value "
		val startDate:DateMidnight = -2422054408000L
		val endDate:DateMidnight = -3600000L

		val paymentPeriod = new PaymentPeriod[DateMidnight] {

			override def reference: Option[Interval[DateMidnight]] = None

			override def actual: Interval[DateMidnight] =  (DefaultIntervalBuilder apply(startDate, endDate)).toOption.get
		}
		exampleName ! (tuple must computeIdenticalDayCountFor(paymentPeriod))

	}


}



