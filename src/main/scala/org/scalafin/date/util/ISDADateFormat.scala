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
    var month = dt.monthOfYear.getAsString
    var day = dt.dayOfMonth.getAsString
    if (dt.monthOfYear.get < 10) {
      month = 0 + month
    }

    if (dt.dayOfMonth.get < 10) {
      day = 0 + day
    }

    dt.yearOfEra.getAsString + delimiter + month + delimiter + day
  }

  /**
   * Parses a String of the format YYYY/MM/DD into a DateMidnight
   *
   * @param in
   *            The String to parse
   * @return The DateMidnight for the given string
   * @throws ParseException
   */
  def parse(stringDate: String): DateMidnight = {
    // TODO this is ugly and unsafe
    val splitString = stringDate.split("/")
    // TODO get rid of the time?
    DateTime.now.withDate(splitString.apply(0).toInt, splitString.apply(1).toInt, splitString.apply(2).toInt).toDateMidnight
  }

}
