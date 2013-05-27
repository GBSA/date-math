package org.scalafin.date.daycount

//http://eclipsesoftware.biz/DayCountConventions.html
import org.scalafin.date.daycount.DaycountCalculators._
import org.scalafin.date.holiday.HolidayCalendar
import org.scalafin.date.ExactFitInYear
import org.scalafin.date.Frequency

trait SimpleCalculator {
  def calculator: DaycountCalculator
}

trait ParametrizedCalculator[T] {
  def calculator(t: T): DaycountCalculator
}

sealed trait DaycountConvention

case object Actual360 extends DaycountConvention with SimpleCalculator {
  val calculator = Actual360DaycountCalculator
}

case object Actual365Fixed extends DaycountConvention with SimpleCalculator {
  val calculator = Actual365FixedDaycountCalculator
}
case object Actual366 extends DaycountConvention with SimpleCalculator {
  val calculator = Actual366DaycountCalculator
}

case object AFBActualActual extends DaycountConvention with SimpleCalculator {
  val calculator = AFBActualActualDaycountCalculator
}
case object ISMAYear extends DaycountConvention with SimpleCalculator {
  val calculator = AFBActualActualDaycountCalculator
}

case object EU30360 extends DaycountConvention with SimpleCalculator {
  val calculator = EU30360DaycountCalculator
}

case object ISDAActualActual extends DaycountConvention with SimpleCalculator {
  val calculator = ISDAActualActualDaycountCalculator
}

case object IT30360 extends DaycountConvention with SimpleCalculator {
  val calculator = IT30360DaycountCalculator
}

case object US30360 extends DaycountConvention with SimpleCalculator {
  val calculator = US30360DaycountCalculator
}

case class ISMAActualActual[T <: Frequency with ExactFitInYear] extends DaycountConvention with ParametrizedCalculator[T] {
  def calculator(frequency: T) = new ISMAActualActualDaycountCalculator(frequency)
}
case class ICMAActualActual[T <: Frequency with ExactFitInYear] extends DaycountConvention with ParametrizedCalculator[T] {
  def calculator(frequency: T) = new ISMAActualActualDaycountCalculator(frequency)
}

case class Business252[T <: HolidayCalendar] extends DaycountConvention with ParametrizedCalculator[T] {
  def calculator(hc: T) = new Business252DaycountCalculator(hc)
}