package com.gottexbrokers.datemath.scheduler

import org.specs2.{ScalaCheck, Specification}
import org.specs2.specification.Example
import com.gottexbrokers.datemath._
import org.joda.time._
import com.gottexbrokers.datemath.Frequencies.{QUARTERLY, MONTHLY}
import org.specs2.matcher.{MatchResult, Parameters, Matcher}
import com.gottexbrokers.datemath.test.{JodaTimeGenerators, FromAmericanDiscoveryToJupiter, ComparableMatchers, PeriodMatchers}
import org.scalacheck.{Prop, Gen, Arbitrary}
import scalaz.Failure
import org.specs2.matcher.Parameters
import scalaz.Success
import scala.annotation.tailrec
import com.gottexbrokers.datemath.math.{DefaultIntervalBuilder, IntervalBuilder}


/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 24/02/14
 * Time: 15:13
 *
 */
class SimpleSchedulerTests extends Specification
                                          with PeriodMatchers
                                          with ComparableMatchers
																					with ScalaCheck
																					with FromAmericanDiscoveryToJupiter
																					with JodaTimeGenerators{

	implicit val params =  Parameters(workers=4,minTestsOk = 1000,maxSize = 5000)



	def is = s2"""
						    The XXXX "first" stub scheduler
						        Correctly generates the correct periods when the STUB is  Short $e1
										Correctly generates the correct periods when the STUB is  Long $e2

								The XXXX "last" stub scheduler
						        Correctly generates the correct periods when the STUB is  Short $e3
										Correctly generates the correct periods when the STUB is  Long $e4

							  The No Stub scheduler
							      Fails if the date are not separated by a multiple of the frequency $e5
										Succees if the date are not separated by a multiple of the frequency $e6

						"""


	override def beTheSameInstantAs(instant: ReadableInstant): Matcher[ReadableDateTime] = beEqualAccordingToCompare[ReadableInstant,ReadableDateTime](instant)


	def e1:Example = {
		val scheduler = new ShortStubFirstScheduler{

			override implicit def paymentPeriodBuilder: PaymentPeriodBuilder = new SimplePaymentPeriodBuilder{}

			override implicit def intervalBuilder: IntervalBuilder[math.Period] = DefaultIntervalBuilder

		}
		val start = new DateTime(2010,1,15,0,0,0)
		val firstCouponEnd = new DateTime(2010,2,1,0,0,0)
		val referenceStart = new DateTime(2010,1,1,0,0,0)
		val end = new DateTime(2010,10,1,0,0,0)
		val maybeSchedule = scheduler schedule(MONTHLY, start,end)
		val result = maybeSchedule must beLike{
			case Success(schedule) =>
			val rightSize = schedule.periods must haveSize(9)
			val firstPeriod = schedule.periods.head
			val actualMatch = firstPeriod.actual must be(start,firstCouponEnd)
			val referenceMatch = firstPeriod.reference must beSome.like {
				case reference => reference must be(referenceStart,firstCouponEnd)
			}
			rightSize and actualMatch and referenceMatch
		}
		"The schedule generated by the short stub first scheduler must have the right size and the right stub at the beginning" ! result
	}

	def e2:Example = {
		val scheduler = new LongStubFirstScheduler {

			override def paymentPeriodBuilder: PaymentPeriodBuilder = new SimplePaymentPeriodBuilder{}

			override def intervalBuilder: IntervalBuilder[math.Period] = DefaultIntervalBuilder

		}
		val start = new DateTime(2010,1,15,0,0,0)
		val firstCouponEnd = new DateTime(2010,3,1,0,0,0)
		val referenceStart = new DateTime(2010,2,1,0,0,0)
		val end = new DateTime(2010,10,1,0,0,0)
		val maybeSchedule = scheduler schedule(MONTHLY, start,end)
		val result = maybeSchedule must beLike{
			case Success(schedule) =>
				val rightSize = schedule.periods must haveSize(8)
				val firstPeriod = schedule.periods.head
				val actualMatch = firstPeriod.actual must be(start,firstCouponEnd)
				val referenceMatch = firstPeriod.reference must beSome.like {
					case reference => reference must be(referenceStart,firstCouponEnd)
				}
				rightSize and actualMatch and referenceMatch

		}

		"The schedule generated by the long stub first scheduler must have the right size and the right stub at the beginning" ! result
	}

	def e3:Example = {
		val scheduler = new ShortStubLastScheduler{

			override implicit def paymentPeriodBuilder: PaymentPeriodBuilder = new SimplePaymentPeriodBuilder{}

			override implicit def intervalBuilder: IntervalBuilder[math.Period] = DefaultIntervalBuilder
		}
		val start = new DateTime(2010,1,15,0,0,0)
		val lastPeriodReferenceEnd = new DateTime(2010,10,15,0,0,0)
		val lastPeriodStart= new DateTime(2010,9,15,0,0)

		val end = new DateTime(2010,10,1,0,0,0)
		val maybeSchedule = scheduler schedule(MONTHLY, start,end)
		val result = maybeSchedule must beLike{
			case Success(schedule) =>
				val rightSize = schedule.periods must haveSize(9)
				val lastPeriod = schedule.periods.last
				val actualMatch = lastPeriod.actual must be(lastPeriodStart,end)
				val referenceMatch = lastPeriod.reference must beSome.like {
					case reference => reference must be(lastPeriodStart,lastPeriodReferenceEnd)
				}
				rightSize and actualMatch and referenceMatch
		}

		"The schedule generated by the short stub last scheduler must have the right size and the right stub at the end" ! result
	}

	def e4:Example = {

		val scheduler = new LongStubLastScheduler{

			override implicit def paymentPeriodBuilder: PaymentPeriodBuilder = new SimplePaymentPeriodBuilder{}

			override implicit def intervalBuilder: IntervalBuilder[math.Period] = DefaultIntervalBuilder

		}

		val start = new DateTime(2010,1,15,0,0,0)
		val lastPeriodReferenceEnd = new DateTime(2010,9,15,0,0,0)
		val lastPeriodStart= new DateTime(2010,8,15,0,0)
		val end = new DateTime(2010,10,1,0,0,0)

		val maybeSchedule = scheduler schedule(MONTHLY, start,end)
		val result = maybeSchedule must beLike{
			case Success(schedule) =>
				val rightSize = schedule.periods must haveSize(8)
				val lastPeriod = schedule.periods.last
				val actualMatch = lastPeriod.actual must be(lastPeriodStart,end)
				val referenceMatch = lastPeriod.reference must beSome.like {
					case reference => reference must be(lastPeriodStart,lastPeriodReferenceEnd)
				}
				rightSize and actualMatch and referenceMatch
		}
		"The schedule generated by the long stub last scheduler must have the right size and the right stub at the end" ! result

	}

	def e5:Example = {

		val scheduler = new NoStubScheduler {

			override implicit def paymentPeriodBuilder: PaymentPeriodBuilder = new SimplePaymentPeriodBuilder{}

			override implicit def intervalBuilder: IntervalBuilder[math.Period] = DefaultIntervalBuilder

		}

		val start = new DateTime(2010,1,15,0,0,0)
		val end = new DateTime(2010,10,1,0,0,0)

		val maybeSchedule1 = scheduler schedule(MONTHLY, start,end)
		val result1 = maybeSchedule1 must beLike{
			case Failure(_) => ok
		}
		val start2 = new DateTime(2010,1,1,0,0,0)
		val end2 = new DateTime(2010,11,1,0,0,0)
		val maybeSchedule2 = scheduler schedule(QUARTERLY, start2,end2)
		val result2 = maybeSchedule2 must beLike{
			case Failure(_) => ok
		}
		"The non stubbed scheduler should fail when trying to generate schedule which needs stubbing" ! (result1 and result2)

	}


	def e6:Example = {

		val scheduler = new NoStubScheduler {

			override implicit def paymentPeriodBuilder: PaymentPeriodBuilder = new SimplePaymentPeriodBuilder{}

			override implicit def intervalBuilder: IntervalBuilder[math.Period] = DefaultIntervalBuilder

		}

		implicit val intArbitrary = Arbitrary { Gen.choose(1,24)}

		implicit val frequencyArbitrary = Arbitrary { Gen.oneOf(Frequencies.values)map{ x => x:Frequency}}

		"The non stubbed scheduler should succeed when no stub is required" !  Prop.forAll{
			(frequency:Frequency, startDate:DateTime, periodMultipler:Int) => testScheduleSuccess(frequency,scheduler,startDate,periodMultipler)
		}


	}

	def testScheduleSuccess(frequency:Frequency, scheduler:Scheduler, startDate:DateTime, periodMultiplier:Int):MatchResult[Any] = {
		@tailrec
		def addPeriod(date:DateTime, period:ReadablePeriod, count:Int):DateTime = {
			if(count==0)
				date
			else addPeriod(date plus period,period,count-1)
		}
		val end = addPeriod(startDate,frequency.period,periodMultiplier)
		val maybeSchedule = scheduler schedule(frequency, startDate,end)
		maybeSchedule must beLike {
			case Success(_) => ok
		}
	}








}


