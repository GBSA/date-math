package org.scalafin.date

import org.joda.time.DateMidnight
import org.scalafin.util.ISDADateFormat

import scala.math.Ordering.Implicits._
import com.github.nscala_time.time.Imports._

/**
 * Class representing a period between two dates. Instances of this class may
 * also contain 'notional' start and end dates which are required to accurately
 * calculate the day count fractions of irregular length periods within a longer
 * schedule.
 */
trait DatePeriod {

  def dateRange: DateRange

  def contains(date: DateMidnight): Boolean = dateRange.startDate < date && dateRange.endDate > date

  /**
   * Creates a human readable String representing this period, e.g: 2005/1/1 - 2006/12/30
   */
  override lazy val toString: String = {
    "%s - %s".format(
      ISDADateFormat.format(dateRange.startDate),
      ISDADateFormat.format(dateRange.endDate))
  }

}