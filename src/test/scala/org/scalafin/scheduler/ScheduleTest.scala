package org.scalafin.scheduler

import org.junit.runner.RunWith
import org.scalafin.util.ISDADateFormat
import org.specs2.runner.JUnitRunner
import scalaz.Success
import org.specs2.matcher.ThrownMessages
import org.specs2.mutable._
import org.scalafin.date.DateRange
import org.scalafin.date.SimplePeriod
import org.scalafin.date.DatePeriodSplitter._

@RunWith(classOf[JUnitRunner])
class ScheduleTest extends Specification with ThrownMessages {
  "cutSinglePeriodByDate" should {
    "not fail" in {
      val startDate = ISDADateFormat parse "2006/01/01"
      val endDate = ISDADateFormat parse "2006/03/01"
      val cutDate = ISDADateFormat parse "2006/02/01"

      var cutDates = Seq(cutDate)

      val dateRange = DateRange(startDate, endDate).toOption.get
      val period = SimplePeriod(dateRange)

      val schedule = Schedule(Seq(period))

      val scheduleIsSuccesful = schedule fold (
        failure => false,
        success => true)
      scheduleIsSuccesful must beTrue

      val resultSchedule = schedule map (_.cutScheduleByDates(cutDates))

      val resultPeriods = resultSchedule match {
        case Success(x) => x.periods
        case _ => fail("Can't cut schedule")
      }
      resultPeriods must be size 2

      val cutPeriod1 = resultPeriods apply 0
      val cutPeriod2 = resultPeriods apply 1

      ISDADateFormat.formatFixedLength(cutPeriod1.dateRange.startDate) must beMatching("2006/01/01")
      ISDADateFormat.formatFixedLength(cutPeriod1.dateRange.endDate) must beMatching("2006/02/01")

      ISDADateFormat.formatFixedLength(cutPeriod2.dateRange.startDate) must beMatching("2006/02/01")
      ISDADateFormat.formatFixedLength(cutPeriod2.dateRange.endDate) must beMatching("2006/03/01")
    }
  }

}
