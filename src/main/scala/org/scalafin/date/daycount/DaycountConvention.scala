package org.scalafin.date.daycount

import org.scalafin.date.daycount.DaycountCalculators.ConventionCalculatorAssociation
import org.scalafin.date.daycount.DaycountCalculators._

sealed abstract class DaycountConvention

case class Actual360 extends DaycountConvention

case class Actual365Fixed extends DaycountConvention

case class Actual366 extends DaycountConvention

case class AFBActualActual extends DaycountConvention

case class Business252 extends DaycountConvention

case class EU30360 extends DaycountConvention

case class ISDAActualActual extends DaycountConvention

case class ISMAActualActual extends DaycountConvention

case class IT30360 extends DaycountConvention

case class US30360 extends DaycountConvention