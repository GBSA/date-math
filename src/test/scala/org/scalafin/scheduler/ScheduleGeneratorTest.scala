package org.scalafin.scheduler

import org.junit.runner.RunWith
import org.specs2.mutable._
import org.scalafin.util.ISDADateFormat
import org.specs2.runner.JUnitRunner
import java.util.Calendar
import scalaz.Success
import scalaz.Validation
import scalaz.Failure
import org.scalafin.convention.SHORT_LAST
import org.scalafin.convention.SHORT_FIRST
import org.scalafin.convention.NONE
import org.scalafin.convention.LONG_LAST
import org.scalafin.convention.LONG_FIRST
import org.scalafin.convention.MONTHLY
import org.scalafin.convention.ANNUALLY
import org.scalafin.date.DateRange

// TODO finish all the testcases

@RunWith(classOf[JUnitRunner])
class ScheduleGeneratorTest extends Specification {
  "ScheduleGenerator for valid dates" should {
    "work for NONE stub" in {
      val startDate = ISDADateFormat.parse("2006/1/1")
      val endDate = ISDADateFormat.parse("2007/1/1")

      val dateRange = DateRange(startDate, endDate)
      val periods = dateRange map (ScheduleGenerator.generateSchedule(_, NONE, MONTHLY))

      periods map println

      periods must beLike { case Success(cutPeriods) => cutPeriods must haveLength(12) }

    }

    "work for SHORT_LAST stub" in {
      val startDate = ISDADateFormat.parse("2006/3/28")
      val endDate = ISDADateFormat.parse("2011/3/28")

      val dateRange = DateRange(startDate, endDate)
      val periods = dateRange map (ScheduleGenerator.generateSchedule(_, SHORT_LAST, ANNUALLY))

      periods map println
      periods must beLike { case Success(cutPeriods) => cutPeriods must haveLength(5) }
    }

    "work for SHORT_FIRST stub" in {
      val startDate = ISDADateFormat.parse("2006/3/28")
      val endDate = ISDADateFormat.parse("2011/3/28")

      val dateRange = DateRange(startDate, endDate)
      val periods = dateRange map (ScheduleGenerator.generateSchedule(_, SHORT_FIRST, ANNUALLY))

      periods map println
      periods must beLike { case Success(cutPeriods) => cutPeriods must haveLength(5) }
    }

    "work for LONG_LAST stub" in {
      val startDate = ISDADateFormat.parse("2006/3/28")
      val endDate = ISDADateFormat.parse("2011/3/28")

      val dateRange = DateRange(startDate, endDate)
      val periods = dateRange map (ScheduleGenerator.generateSchedule(_, LONG_LAST, ANNUALLY))

      periods map println
      periods must beLike { case Success(cutPeriods) => cutPeriods must haveLength(5) }
    }

    "work for LONG_FIRST stub" in {
      val startDate = ISDADateFormat.parse("2006/3/28")
      val endDate = ISDADateFormat.parse("2011/3/28")

      val dateRange = DateRange(startDate, endDate)
      val periods = dateRange map (ScheduleGenerator.generateSchedule(_, LONG_FIRST, ANNUALLY))

      periods map println
      periods must beLike { case Success(cutPeriods) => cutPeriods must haveLength(5) }
    }
  }

  // TODO manage exception due to reached periods limit
  //  "ScheduleGenerator periods limit" should {
  //    "throw an exception" in {
  //      val effectiveDate = ISDADateFormat.parse("2006/3/28")
  //      val maturityDate = ISDADateFormat.parse("2011/3/28")
  //      val frequency = ANNUALLY
  //      val stubType = SHORT_LAST
  //      val periods = ScheduleGenerator.generateSchedule(effectiveDate, maturityDate, frequency.periodAmount, frequency, stubType, null, 1)
  //      
  //      periods must be size 2
  //    }
  //  }
  //  	public void testExceedMaxPeriodsStubTypeShortLast() throws ParseException {
  //		Calendar effectiveDate = ISDADateFormat.parse("2006/3/28");
  //		Calendar maturityDate = ISDADateFormat.parse("2011/3/28");
  //		Frequency frequency = Frequency.ANNUALLY;
  //		StubType stubType = StubType.SHORT_LAST;
  //		try
  //		{
  //			ScheduleGenerator.generateSchedule(effectiveDate, maturityDate, frequency, stubType,4);
  //		} catch (ScheduleException e)
  //		{
  //			return;
  //		}
  //		fail("Schedule generator should have thrown an exception producing a schedule with 5 periods when maxPeriods was set to 4.");
  //	}
  //
  //
  //	public void testExceedMaxPeriodsStubTypeShortFirst() throws ParseException {
  //		Calendar effectiveDate = ISDADateFormat.parse("2006/3/28");
  //		Calendar maturityDate = ISDADateFormat.parse("2011/3/28");
  //		Frequency frequency = Frequency.ANNUALLY;
  //		StubType stubType = StubType.SHORT_FIRST;
  //		try
  //		{
  //			ScheduleGenerator.generateSchedule(effectiveDate, maturityDate, frequency, stubType,4);
  //		} catch (ScheduleException e)
  //		{
  //			return;
  //		}
  //		fail("Schedule generator should have thrown an exception producing a schedule with 5 periods when maxPeriods was set to 4.");
  //	}
  //
  //	public void testExceedMaxPeriodsStubTypeLongLast() throws ParseException {
  //		Calendar effectiveDate = ISDADateFormat.parse("2006/3/28");
  //		Calendar maturityDate = ISDADateFormat.parse("2011/3/28");
  //		Frequency frequency = Frequency.ANNUALLY;
  //		StubType stubType = StubType.LONG_LAST;
  //		try
  //		{
  //			ScheduleGenerator.generateSchedule(effectiveDate, maturityDate, frequency, stubType,4);
  //		} catch (ScheduleException e)
  //		{
  //			return;
  //		}
  //		fail("Schedule generator should have thrown an exception producing a schedule with 5 periods when maxPeriods was set to 4.");
  //	}
  //
  //	public void testExceedMaxPeriodsStubTypeLongFirst() throws ParseException {
  //		Calendar effectiveDate = ISDADateFormat.parse("2006/3/28");
  //		Calendar maturityDate = ISDADateFormat.parse("2011/3/28");
  //		Frequency frequency = Frequency.ANNUALLY;
  //		StubType stubType = StubType.LONG_FIRST;
  //		try
  //		{
  //			ScheduleGenerator.generateSchedule(effectiveDate, maturityDate, frequency, stubType,4);
  //		} catch (ScheduleException e)
  //		{
  //			return;
  //		}
  //		fail("Schedule generator should have thrown an exception producing a schedule with 5 periods when maxPeriods was set to 4.");
  //	}

  // TODO do we want to port all the different constructors..?
  //    "Schedule with Maturity" should {
  //    "succeed" in {
  //      val effectiveDate = ISDADateFormat.parse("2006/3/28")
  //      val frequency = ANNUALLY
  //      val stubType = NONE
  //      val maturity = "5Y"
  //
  //      ScheduleGenerator.generateSchedule(effectiveDate, maturity, frequency.periodAmount, frequency, null, stubType) must be size 5
  //      ScheduleGenerator.generateSchedule(effectiveDate, maturity, frequency, stubType, 5) must be size 5
  //    }
  //  }
}