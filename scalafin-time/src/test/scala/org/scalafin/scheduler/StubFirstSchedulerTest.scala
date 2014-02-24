package org.scalafin.scheduler

import org.specs2.Specification
import org.specs2.specification.Example
import org.scalafin.utils.{DefaultIntervalBuilder, Interval, IntervalBuilder}
import org.scalafin.datemath.{SimplePaymentPeriodBuilder, PaymentPeriodBuilder}
import org.joda.time._
import org.scalafin.datemath.Frequencies.MONTHLY
import org.specs2.matcher.{MatchResult, Expectable, Matcher}
import org.scalafin.datemath.test.{ComparableMatchers, PeriodMatchers}
import com.mbc.jfin.schedule.impl.LongFirstStubScheduleGenerator


/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 24/02/14
 * Time: 15:13
 *
 */
class StubFirstSchedulerTest extends Specification
                                          with PeriodMatchers
                                          with ComparableMatchers{




	def is = s2"""
						    The XXXX "first" stub scheduler
						        Correctly generates the correct periods when the STUB is  Short $e1
										Correctly generates the correct periods when the STUB is  Long $e2
						"""


	override def beTheSameInstantAs(instant: ReadableInstant): Matcher[ReadableDateTime] = beEqualAccordingToCompare[ReadableInstant,ReadableDateTime](instant)


	def e1:Example = {
		val scheduler = new ShortStubFirstScheduler{

			override implicit def paymentPeriodBuilder: PaymentPeriodBuilder = new SimplePaymentPeriodBuilder{}

			override implicit def intervalBuilder: IntervalBuilder[Interval] = DefaultIntervalBuilder
		}
		val start = new DateTime(2010,1,15,0,0,0)
		val firstCouponEnd = new DateTime(2010,2,1,0,0,0)
		val referenceStart = new DateTime(2010,1,1,0,0,0)
		val end = new DateTime(2010,10,1,0,0,0)
		val schedule = scheduler schedule(MONTHLY, start,end)
		val rightSize = schedule.periods must haveSize(9)
		val firstPeriod = schedule.periods.head
		val actualMatch = firstPeriod.actual must be(start,firstCouponEnd)
		val referenceMatch = firstPeriod.reference must beSome.like {
			case reference => reference must be(referenceStart,firstCouponEnd)
		}
		"The schedule must have the right size and have the right short stub at the beginning" ! (rightSize and actualMatch and referenceMatch)
	}

	def e2:Example = {
		val scheduler = new LongStubFirstScheduler {

			override def paymentPeriodBuilder: PaymentPeriodBuilder = new SimplePaymentPeriodBuilder{}

			override def intervalBuilder: IntervalBuilder[Interval] = DefaultIntervalBuilder

		}
		val start = new DateTime(2010,1,15,0,0,0)
		val firstCouponEnd = new DateTime(2010,3,1,0,0,0)
		val referenceStart = new DateTime(2010,2,1,0,0,0)
		val end = new DateTime(2010,10,1,0,0,0)
		val schedule = scheduler schedule(MONTHLY, start,end)
		val rightSize = schedule.periods must haveSize(8)
		val firstPeriod = schedule.periods.head
		val actualMatch = firstPeriod.actual must be(start,firstCouponEnd)
		val referenceMatch = firstPeriod.reference must beSome.like {
			case reference => reference must be(referenceStart,firstCouponEnd)
		}
		"The schedule must have the right size and have the right long stub at the beginning" ! (rightSize and actualMatch and referenceMatch)
	}

}


