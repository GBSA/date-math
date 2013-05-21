package org.scalafin.date

import org.junit.runner.RunWith
import org.scalafin.date.daycount.impl.ISMAActualActual
import org.scalafin.date.util.ISDADateFormat
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import java.util.GregorianCalendar
import org.joda.time.DateTime
import org.joda.time.DateMidnight

@RunWith(classOf[JUnitRunner])
class ScheduleGeneratorSanityCheckTest extends Specification {

  "daysBetween" should {
    "return the right number of days between 2 dates" in {
      val start = new DateMidnight(new GregorianCalendar(2005, 0, 1))
      val end = new DateMidnight(new GregorianCalendar(2010, 1, 1))
      val calculator = new ISMAActualActual()
      calculator.daysBetween(start, end) === 1857
    }
  }

  "ScheduleGenerator with no stubs" should {
    "create even periods" in {
      val start = new DateMidnight(new GregorianCalendar(2005, 0, 1))
      val end = new DateMidnight(new GregorianCalendar(2010, 1, 1))
      val frequency = QUARTERLY
      val stubType = NONE

      val dateRange = DateRange(start, end).toOption.get
      val periods = ScheduleGenerator.generateSchedule(dateRange, stubType, frequency)

      val calculator = new ISMAActualActual

      periods map { period =>
        calculator.calculateDaycountFraction(period).toOption.get === 0.25d
      }
    }

    "again even periods" in {
      val start = ISDADateFormat.parse("2006/3/23")
      val end = ISDADateFormat.parse("2011/3/23")
      val frequency = QUARTERLY
      val stubType = NONE

      
      val dateRange = DateRange(start, end).toOption.get
      val periods = ScheduleGenerator.generateSchedule(dateRange, stubType, frequency)

      val calculator = new ISMAActualActual()

      periods map { period =>
        calculator.calculateDaycountFraction(period).toOption.get === 0.25d
      }
    }
  }

  "ScheduleGenerator with Short first" should {
    "have a shorter first period" in {
      val start = new DateMidnight(new GregorianCalendar(2005, 0, 1))
      val end = new DateMidnight(new GregorianCalendar(2010, 1, 1))
      val frequency = QUARTERLY
      val stubType = SHORT_FIRST

      val dateRange = DateRange(start, end).toOption.get
      val periods = ScheduleGenerator.generateSchedule(dateRange, stubType, frequency)

      val calculator = new ISMAActualActual()

      periods map { period =>
        calculator.calculateDaycountFraction(period).toOption.get === 0.25d
      }
    }
  }
}