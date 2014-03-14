package com.gottexbrokers.datemath

import org.specs2.{ScalaCheck, Specification}
import org.specs2.specification.Example
import com.gottexbrokers.datemath.scheduler._
import com.gottexbrokers.datemath.math.{DefaultIntervalBuilder, IntervalBuilder}
import com.mbc.jfin.schedule.impl._
import org.scalacheck.{Gen, Arbitrary, Prop}
import org.joda.time.{LocalDate, DateTime}
import org.specs2.matcher.MatchResult
import scalaz.Failure
import com.gottexbrokers.datemath.test.{LongGeneratorWithNoOverflow, JodaTimeGenerators, ScheduleMatchers}
import com.gottexbrokers.datemath.Frequencies._
import scalaz.Failure
import org.specs2.matcher.Parameters
import scalaz.Failure
import org.specs2.matcher.Parameters
import scalaz.Success
import org.scalacheck.util.Pretty
import scalaz.Failure
import org.specs2.matcher.Parameters
import scalaz.Success

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 26/02/14
 * Time: 16:21
 *
 */
class SchedulersCoherenceTest extends Specification
                                      with ScheduleMatchers
                                      with JodaTimeGenerators
                                      with LongGeneratorWithNoOverflow
                                      with ScalaCheck {


	override val defaultPrettyParams = Pretty.Params(2)

	def is = s2"""
	             	  The schedulers are coherent between jfin and datemath

		                  The short stub first scheduler always generate the same schedule in Jfin and DateMath $shortStubFirstCoherent
									    The long stub first scheduler always generate the same schedule in Jfin and DateMath  $longStubFirstCoherent
											The short stub last scheduler always generate the same schedule in Jfin and DateMath  $shortStubLastCoherent
											The long stub last scheduler always generate the same schedule in Jfin and DateMath $longStubLastCoherent
							        The no stub last scheduler always generate the same schedule in Jfin and DateMath $noStubSchedulerCoherent
             """




	implicit val params = Parameters(workers = 16)

	implicit val arbitraryPeriodCount = Arbitrary {
		Gen.choose(1, 100)
	}

	implicit val frequencyArbitrary = Arbitrary {
		Gen.oneOf(Frequencies.values) map {
			x => x: Frequency
		}
	}

	def shortStubFirstCoherent: Prop = {
		val scheduler = new ShortStubFirstScheduler {

			override implicit def paymentPeriodBuilder: PaymentPeriodBuilder = new SimplePaymentPeriodBuilder {}

			override implicit def intervalBuilder: IntervalBuilder[math.Period] = DefaultIntervalBuilder

		}
		testStubScheduler(new ShortFirstStubScheduleGenerator(), scheduler)
	}

	def longStubFirstCoherent: Prop = {
		val scheduler = new LongStubFirstScheduler {

			override implicit def paymentPeriodBuilder: PaymentPeriodBuilder = new SimplePaymentPeriodBuilder {}

			override implicit def intervalBuilder: IntervalBuilder[math.Period] = DefaultIntervalBuilder

		}
		testStubScheduler(new LongFirstStubScheduleGenerator(), scheduler)
	}

	def shortStubLastCoherent: Prop = {
		val scheduler = new ShortStubLastScheduler {

			override implicit def paymentPeriodBuilder: PaymentPeriodBuilder = new SimplePaymentPeriodBuilder {}

			override implicit def intervalBuilder: IntervalBuilder[math.Period] = DefaultIntervalBuilder

		}
		testStubScheduler(new ShortLastStubScheduleGenerator(), scheduler)
	}

	def longStubLastCoherent: Prop = {
		val scheduler = new LongStubLastScheduler {

			override implicit def paymentPeriodBuilder: PaymentPeriodBuilder = new SimplePaymentPeriodBuilder {}

			override implicit def intervalBuilder: IntervalBuilder[math.Period] = DefaultIntervalBuilder

		}
		testStubScheduler(new LongLastStubScheduleGenerator(), scheduler)
	}


	def noStubSchedulerCoherent: Prop = {
		val scheduler = new NoStubScheduler {

			override implicit def paymentPeriodBuilder: PaymentPeriodBuilder = new SimplePaymentPeriodBuilder {}

			override implicit def intervalBuilder: IntervalBuilder[math.Period] = DefaultIntervalBuilder

		}
		val jfinScheduler = new NoStubScheduleGenerator()
		Prop.forAll {
			(start: DateTime, scheduleFrequency: Frequency, maxPeriods: Int) => {
				val end = start plus scheduleFrequency.divide(maxPeriods).period
				testSchedule(start, end, scheduleFrequency, jfinScheduler, scheduler)
			}

		}
	}


	def testStubScheduler(jfinScheduler: ScheduleGenerator, scheduler: Scheduler): Prop = {
		Prop.forAll {
			(start: DateTime, scheduleFrequency: Frequency, endFrequency: Frequency, maxPeriods: Int) => {
				val end = start plus endFrequency.divide(maxPeriods).period
				testSchedule(start, end, scheduleFrequency, jfinScheduler, scheduler)
			}
		}
	}



		def e2 = {
			val scheduler = new NoStubScheduler{

				override implicit def paymentPeriodBuilder: PaymentPeriodBuilder = new SimplePaymentPeriodBuilder{}

				override implicit def intervalBuilder: IntervalBuilder[math.Period] = DefaultIntervalBuilder

			}

			val jfinScheduler = new NoStubScheduleGenerator()
			val start = DateTime.parse("1970-01-01T00:00:00.000+01:00")
			val scheduleFrequency = QUARTERLY
			val endFrequency = QUARTERLY
			val maxPeriods = 2
			val end = start plus endFrequency.divide(maxPeriods).period
			testSchedule(start,end,scheduleFrequency,jfinScheduler,scheduler)


		}


	def testSchedule(start: DateTime, end: DateTime, scheduleFrequency: Frequency, jfinScheduler: ScheduleGenerator, scheduler: Scheduler): MatchResult[Any] = {
		implicit def toLocalDate(date: DateTime) = new LocalDate(date.getMillis)
		val scheduleResult = scheduler schedule(scheduleFrequency, start, end)
		scheduleResult must beLike {
			case Success(scheduledDates) =>
				val jfinSchedule = jfinScheduler.generate(start, end, scheduleFrequency.period)
				scheduledDates must beAScheduleEquivalentTo(jfinSchedule)
			case Failure(x) => jfinScheduler.generate(start, end, scheduleFrequency.period) must throwA[Exception]
		}
	}
}
