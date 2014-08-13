package com.gottexbrokers.datemath.utils

import org.specs2.{ ScalaCheck, Specification }
import org.scalacheck.{ Arbitrary, Prop, Gen }
import com.gottexbrokers.datemath._
import org.specs2.specification.Example
import com.gottexbrokers.datemath.test._
import org.joda.time.{ ReadableInstant, ReadableDateTime, DateTime }
import com.gottexbrokers.datemath.scheduler.Schedule
import scalaz.Success
import org.specs2.matcher.{ Parameters, MatchResult, Expectable, Matcher }
import org.scalacheck.util.Pretty

class DateAdjustmentToolsTest extends Specification with DateAdjustmentTools
  with ScalaCheck
  with JodaTimeGenerators
  with LongGeneratorWithNoOverflow
  with DateMathTestInstances
  with CalendarsGenerators
  with ComparableMatchers {

  implicit val params = Parameters(workers = 4, minTestsOk = 1000, maxSize = 10000)

  val schedulers = Seq[StubType](StubTypes.LONG_FIRST, StubTypes.LONG_LAST, StubTypes.SHORT_FIRST, StubTypes.SHORT_LAST, StubTypes.NONE) map Schedulers.apply

  override val defaultPrettyParams = Pretty.Params(2)

  implicit val schedulerGen = Arbitrary { Gen oneOf schedulers.flatten }

  implicit val businessDayConventionArbitrary = Arbitrary { Gen oneOf BusinessDayConventions.values }

  def is = e1

  implicit val arbitraryPeriodCount = Arbitrary {
    Gen.choose(1, 30)
  }

  implicit val frequencyArbitrary = Arbitrary {
    Gen.oneOf(Frequencies.values) map {
      x => x: Frequency
    }
  }

  def e1: Example = "The scheduler adjustment should always return coherent schedule" ! Prop.forAll(
    (calendar: SimpleMbcHolidayCalendar, businessDayConvention: BusinessDayConvention, start: DateTime, scheduler: Scheduler, scheduleFrequency: Frequency, maxPeriods: Int) => {
      Prop.collect(businessDayConvention, scheduler) {
        test(calendar, businessDayConvention, start, scheduler, scheduleFrequency, maxPeriods)
      }
    })

  def test(calendar: SimpleMbcHolidayCalendar, businessDayConvention: BusinessDayConvention, start: DateTime, scheduler: Scheduler, scheduleFrequency: Frequency, maxPeriods: Int) = {
    val end = start plus scheduleFrequency.divide(maxPeriods).period
    implicit val holidayCalendar = calendar.toDateMathCalendar
    val schedule = scheduler.schedule(scheduleFrequency, start, end)
    schedule must beLike {
      case Success(x) =>
        val adjusted = adjust(x, businessDayConvention)
        (adjusted must respectScheduleInvariant) and (adjusted must beEqualToAdjusted(x, businessDayConvention))
    }
  }

  def beTheSameInstantAs(instant: ReadableInstant): Matcher[ReadableDateTime] = beEqualAccordingToCompare[ReadableInstant, ReadableDateTime](instant)

  def beEqualToAdjusted(unadjusted: Schedule, businessDayConvention: BusinessDayConvention)(implicit hc: HolidayCalendar): Matcher[Schedule] = new Matcher[Schedule] {
    override def apply[S <: Schedule](t: Expectable[S]): MatchResult[S] = {
      val adjusted = t.value
      val startMatches = adjusted.start must beTheSameInstantAs(businessDayConvention.adjust(unadjusted.start))
      val endMatches = adjusted.end must beTheSameInstantAs(businessDayConvention.adjust(unadjusted.end))
      val periodsMatches = forall(adjusted.periods zip unadjusted.periods) {
        case (adjustedPeriod, unadjustedPeriod) =>
          val startMatch = adjustedPeriod.start must beTheSameInstantAs(businessDayConvention.adjust(unadjustedPeriod.start))
          val endMatch = adjustedPeriod.end must beTheSameInstantAs(businessDayConvention.adjust(unadjustedPeriod.end))
          startMatch and endMatch
      }
      result(startMatches and endMatches and periodsMatches, t)
    }
  }

  def respectScheduleInvariant: Matcher[Schedule] = new Matcher[Schedule] {
    override def apply[S <: Schedule](t: Expectable[S]): MatchResult[S] = {
      val value = t.value
      val noDatesExistBeforeStart = value.periods must beginAfter(value.start).forall
      val noDatesExistAfterEnd = value.periods must endBefore(value.end).forall
      val startsAtFirstPeriodStart = value.periods.head aka "The first period" must beLike { case x => x.start must beTheSameInstantAs(value.start) }
      val endsAtLastPeridEnd = value.periods.last aka "The last period" must beLike { case x => x.end must beTheSameInstantAs(value.end) }
      val res = noDatesExistBeforeStart and noDatesExistAfterEnd and startsAtFirstPeriodStart and endsAtLastPeridEnd
      result(res, t)
    }
  }

  def beginAfter(readableDateTime: ReadableDateTime): Matcher[TimePeriod[ReadableDateTime]] = (period: TimePeriod[ReadableDateTime]) => {
    val message = s"The start and the end of $period are after $readableDateTime"
    (((period.start compareTo readableDateTime) >= 0) && ((period.end compareTo readableDateTime) >= 0), message, negateSentence(message))
  }
  def endBefore(readableDateTime: ReadableDateTime): Matcher[TimePeriod[ReadableDateTime]] = (period: TimePeriod[ReadableDateTime]) => {
    val message = s"The start and the end of $period are before $readableDateTime"
    (((period.start compareTo readableDateTime) <= 0) && ((period.end compareTo readableDateTime) <= 0), message, negateSentence(message))
  }
}
