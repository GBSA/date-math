package org.scalafin.date

import java.util.Calendar
import org.joda.time.DateMidnight

sealed abstract class Frequency(val tenorDescript: String) {
  def add(amount: Int, dateMidnight: DateMidnight): DateMidnight
}
/**
 * Repeats every day
 */
case object DAILY extends Frequency("1D") {
  def add(amount: Int, dateMidnight: DateMidnight): DateMidnight = dateMidnight.plusDays(1 * amount)
}

/**
 * Repeats every day
 */
case object WEEKLY extends Frequency("1W") {
  def add(amount: Int, dateMidnight: DateMidnight): DateMidnight = dateMidnight.plusWeeks(1 * amount)
}

/**
 * Repeats every 28 days
 */
case object LUNAR_MONTHLY extends Frequency("1L") {
  def add(amount: Int, dateMidnight: DateMidnight): DateMidnight = dateMidnight.plusDays(28 * amount)
}

/**
 * Repeats on the same day of every month
 */
case object MONTHLY extends Frequency("1M") {
  def add(amount: Int, dateMidnight: DateMidnight): DateMidnight = dateMidnight.plusMonths(1 * amount)
}

/**
 * Repeats on the same day every three months
 */
case object QUARTERLY extends Frequency("3M") {
  def add(amount: Int, dateMidnight: DateMidnight): DateMidnight = dateMidnight.plusMonths(3 * amount)
}

/**
 * Repeats on the same day every six months
 */
case object SEMI_ANNUALLY extends Frequency("6M") {
  def add(amount: Int, dateMidnight: DateMidnight): DateMidnight = dateMidnight.plusMonths(6 * amount)
}

/**
 * Repeats on the same day every year
 */
case object ANNUALLY extends Frequency("1Y") {
  def add(amount: Int, dateMidnight: DateMidnight): DateMidnight = dateMidnight.plusYears(1 * amount)
}
