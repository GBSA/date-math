package org.scalafin.date.accrual

import org.specs2.mutable._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.scalafin.date.accrual.impl.FixedAccrualPeriod
import org.scalafin.date.UNADJUSTED
import org.scalafin.date.util.ISDADateFormat
import org.scalafin.date.DateRange

@RunWith(classOf[JUnitRunner])
class AccrualPeriodTest extends Specification {
  "UNADJUSTED date" should {
    "work for valid dates" in {
      val dateRange = DateRange(ISDADateFormat.parse("2007/11/10"), ISDADateFormat.parse("2008/11/10"))
      val paymentPlanningStrategyConvention = PlanningStrategyConvention(UNADJUSTED, null)
      val accrualPeriod = new FixedAccrualPeriod(dateRange.toOption.get, paymentPlanningStrategyConvention, null, null, 0, IN_ADVANCE, None)
      ISDADateFormat.format(accrualPeriod.getAdjustedPaymentDate) must beMatching("2007/11/10")
    }
  }
}