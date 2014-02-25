package org.scalafin.datemath

import org.joda.time._
import org.scalafin.date._


object Frequencies {




  val values = Seq(DAILY,WEEKLY,LUNAR_MONTHLY,MONTHLY,QUARTERLY,SEMI_ANNUALLY,ANNUALLY)

  /**
   * Repeats every day
   */
  case object DAILY extends Frequency{

	  override protected def asPeriod: ReadablePeriod = Days days 1

  }

  /**
   * Repeats every day
   */
  case object WEEKLY extends Frequency {

	  override protected def asPeriod: ReadablePeriod = Days days 7

  }

  /**
   * Repeats every 28 days
   */
  case object LUNAR_MONTHLY extends Frequency{

	  override protected def asPeriod: ReadablePeriod = Days days 28

  }
	
	 class MonthMultipleFrequency private[Frequencies](val months:Int) extends Frequency with ExactFitInYear {

		 override protected def asPeriod: ReadablePeriod = Months months months

		val periodsPerYear =  12 / months

	}

  /**
   * Repeats on the same day of every month
   */
  case object MONTHLY extends MonthMultipleFrequency(1)
  /**
   * Repeats on the same day every three months
   */
  case object QUARTERLY extends MonthMultipleFrequency(3)

  /**
   * Repeats on the same day every six months
   */
  case object SEMI_ANNUALLY extends MonthMultipleFrequency(6)

  /**
   * Repeats on the same day every year
   */
  case object ANNUALLY extends MonthMultipleFrequency(12)

	/**
	 * A generic frequency determined straight from a Joda ReadablePeriod
	 * @param period the inverse of the frequency
	 */
	case class ArbitraryFrequency(private val period:ReadablePeriod) extends Frequency {

		override protected def asPeriod: ReadablePeriod = period

	}







}

