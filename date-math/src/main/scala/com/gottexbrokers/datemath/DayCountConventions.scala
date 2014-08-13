package com.gottexbrokers.datemath

//http://eclipsesoftware.biz/DayCountConventions.html

import DayCountCalculators._

object DayCountConventions {

  case object Actual360 extends DayCountConvention {
    val calculator = Actual360DayCountCalculator
  }

  case object Actual365Fixed extends DayCountConvention {
    val calculator = Actual365FixedDayCountCalculator
  }
  case object Actual366 extends DayCountConvention {
    val calculator = Actual366DayCountCalculator
  }

  case object AFBActualActual extends DayCountConvention {
    val calculator = AFBActualActualDayCountCalculator
  }
  case object ISMAYear extends DayCountConvention {
    val calculator = AFBActualActualDayCountCalculator
  }

  case object EU30360 extends DayCountConvention {
    val calculator = EU30360DayCountCalculator
  }

  case object EU30360ISDA extends DayCountConvention {
    val calculator = EU30360ISDADayCountCalculator
  }

  case object ISDAActualActual extends DayCountConvention {
    val calculator = ISDAActualActualDayCountCalculator
  }

  case object IT30360 extends DayCountConvention {
    val calculator = IT30360DayCountCalculator
  }

  case object US30360Eom extends DayCountConvention {
    val calculator = new US30360DayCountCalculator(true)
  }

  case object US30360NotEom extends DayCountConvention {
    val calculator = new US30360DayCountCalculator(false)
  }

  case object ISMAActualActual extends DayCountConvention {
    val calculator = ISMAActualActualDayCountCalculator
  }
  case object ICMAActualActual extends DayCountConvention {
    val calculator = ISMAActualActualDayCountCalculator
  }

  case class Business252(hc: HolidayCalendar) extends DayCountConvention {
    val calculator = new Business252DayCountCalculator(hc)
  }
}
