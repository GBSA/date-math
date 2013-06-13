package org.scalafin.convention
import org.joda.time.DateMidnight

trait ExactFitInYear {
  def periodsPerYear: Int
}
sealed abstract class Frequency(val tenorDescript: String) {
  def add(amount: Int, date: DateMidnight): DateMidnight
}
/**
 * Repeats every day
 */
case object DAILY extends Frequency("1D") {
  def add(amount: Int, date: DateMidnight): DateMidnight = date.plusDays(1 * amount)
}

/**
 * Repeats every day
 */
case object WEEKLY extends Frequency("1W") {
  def add(amount: Int, date: DateMidnight): DateMidnight = date.plusWeeks(1 * amount)
}

/**
 * Repeats every 28 days
 */
case object LUNAR_MONTHLY extends Frequency("1L") {
  def add(amount: Int, date: DateMidnight): DateMidnight = date.plusDays(28 * amount)
}

/**
 * Repeats on the same day of every month
 */
case object MONTHLY extends Frequency("1M") with ExactFitInYear {
  def add(amount: Int, date: DateMidnight): DateMidnight = date.plusMonths(1 * amount)
  val periodsPerYear = 12
}

/**
 * Repeats on the same day every three months
 */
case object QUARTERLY extends Frequency("3M") with ExactFitInYear {
  def add(amount: Int, date: DateMidnight): DateMidnight = date.plusMonths(3 * amount)
  val periodsPerYear = 4
}

/**
 * Repeats on the same day every six months
 */
case object SEMI_ANNUALLY extends Frequency("6M") with ExactFitInYear {
  def add(amount: Int, date: DateMidnight): DateMidnight = date.plusMonths(6 * amount)
  val periodsPerYear = 2
}

/**
 * Repeats on the same day every year
 */
case object ANNUALLY extends Frequency("1Y") with ExactFitInYear {
  def add(amount: Int, date: DateMidnight): DateMidnight = date.plusYears(1 * amount)
  val periodsPerYear = 1
}
