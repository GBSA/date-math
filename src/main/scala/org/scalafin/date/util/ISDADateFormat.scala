package org.scalafin.date.util

import com.github.nscala_time.time.Imports._
import org.joda.time.DateMidnight

/**
 *
 * Utility class for formatting DateMidnights using the standard ISDA date format.
 *
 * @author jeremy.nguyenxuan
 *
 */
object ISDADateFormat {
  /**
   *
   * Efficient method to return a DateMidnight formatted as YYYY/M/D
   * @param DateMidnight
   * @return A string representing the date in the DateMidnight
   */
  def format(dt: DateMidnight): String = {
    dt.yearOfEra.getAsString + "/" + dt.monthOfYear.getAsString + "/" + dt.dayOfMonth.getAsString
  }

  /**
   * Efficient method to return a DateMidnight formatted as YYYY/MM/DD; will
   * always return a 10 character String
   *
   * @param DateMidnight
   * @return A string representing the date in the DateMidnight
   */
  def formatFixedLength(dt: DateMidnight): String = {
    formatFixedLength(dt, "/")
  }

  /**
   * Efficient method to return a DateMidnight formatted as YYYYMMDD; will always
   * return an 8 character String
   *
   * @param DateMidnight
   * @return A string representing the date in the DateMidnight
   */
  def formatCompact(dt: DateMidnight): String = {
    formatFixedLength(dt, "")
  }

  /**
   * Efficient method to return a DateMidnight formatted as
   * YYYYdelimeterMMdelimeterDD
   *
   * @param DateMidnight
   * @return A string representing the date in the DateMidnight
   */
  def formatFixedLength(dt: DateMidnight, delimiter: String): String = {
    val month = if (dt.getMonthOfYear < 10) "0" + dt.monthOfYear.getAsString else dt.monthOfYear.getAsString
    val day = if (dt.getDayOfMonth < 10) "0" + dt.dayOfMonth.getAsString else dt.dayOfMonth.getAsString

    dt.yearOfEra.getAsString + delimiter + month + delimiter + day
  }

  /**
   * Parses a String of the format YYYY/MM/DD into a DateMidnight
   *
   * @param in
   *            The String to parse
   * @return The DateMidnight for the given string
   */
  // FIXME return validation in case of bad input?
  def parse(stringDate: String): DateMidnight = {
    val pattern = "YYYY/MM/dd"
    DateMidnight.parse(stringDate, DateTimeFormat.forPattern(pattern))
  }

}
