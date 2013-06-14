package org.scalafin.date


import org.joda.time.DateMidnight

import DatePeriodOpsFunc._
import DatePeriodSplitter._
import org.specs2.mutable.Specification

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 13/06/13
 * Time: 16:57
 *
 */
class DatePeriodOpsFuncTest  extends Specification{

  "Wrong behaviour" should {
    "buuu" in {
      val dateRange = DateRange(DateMidnight.now(), DateMidnight.now().plusMonths(6))
      val dates = Seq(DateMidnight.now().plusMonths(2),DateMidnight.now().plusMonths(4))
      val period = dateRange map { new SimplePeriod(_) }
      val result = period map {
        _.cut(dates)
      }
      result foreach { println }
      success
    }
  }


}
