package org.scalafin.date

import org.joda.time.DateMidnight
import scala.swing.Orientable

case class SimplePeriod (dateRange: DateRange, originalDateRange: Option[DateRange] = None) extends DatePeriod

object SimplePeriod