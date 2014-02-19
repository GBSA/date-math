package org.scalafin.datemath

import org.joda.time.ReadableDateTime
import org.scalafin.date._


object Frequencies {

  import org.scalafin.datemath.utils.DateDsl._


  val values = Seq(DAILY,WEEKLY,LUNAR_MONTHLY,MONTHLY,QUARTERLY,SEMI_ANNUALLY,ANNUALLY)

  /**
   * Repeats every day
   */
  case object DAILY extends Frequency{

    def add(amount: Int, date: ReadableDateTime): ReadableDateTime = date plusDays (1 * amount)

  }

  /**
   * Repeats every day
   */
  case object WEEKLY extends Frequency {
    def add(amount: Int, date: ReadableDateTime): ReadableDateTime = date plusWeeks (1 * amount)
  }

  /**
   * Repeats every 28 days
   */
  case object LUNAR_MONTHLY extends Frequency{
    def add(amount: Int, date: ReadableDateTime): ReadableDateTime = date plusDays (28 * amount)
  }

  /**
   * Repeats on the same day of every month
   */
  case object MONTHLY extends Frequency with ExactFitInYear  {
    def add(amount: Int, date: ReadableDateTime): ReadableDateTime = date plusMonths (1 * amount)
    val periodsPerYear = 12
  }

  /**
   * Repeats on the same day every three months
   */
  case object QUARTERLY extends Frequency with ExactFitInYear {
    def add(amount: Int, date: ReadableDateTime): ReadableDateTime = date plusMonths(3 * amount)
    val periodsPerYear = 4
  }

  /**
   * Repeats on the same day every six months
   */
  case object SEMI_ANNUALLY extends Frequency with ExactFitInYear {
    def add(amount: Int, date: ReadableDateTime): ReadableDateTime = date plusMonths(6 * amount)
    val periodsPerYear = 2
  }

  /**
   * Repeats on the same day every year
   */
  case object ANNUALLY extends Frequency with ExactFitInYear {
    def add(amount: Int, date: ReadableDateTime): ReadableDateTime = date plusYears(1 * amount)
    val periodsPerYear = 1
  }





}

