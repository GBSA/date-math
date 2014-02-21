package org.scalafin.datemath.utils

import scalaz.Show
import org.joda.time.DateTime
import org.scalafin.utils.Interval
import org.scalafin.datemath.PaymentPeriod
import org.joda.time.base.BaseDateTime

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 21/02/14
 * Time: 11:30
 *
 */

trait MillisShows {

	implicit def baseDateTimeShow[A <: BaseDateTime] = new Show[A] {
		override def shows(f: A): String = s"millis: ${f.getMillis}"
	}

}


object MillisShows extends MillisShows


trait IntervalShows {

	implicit def intervalShow[A](implicit aShow: Show[A]): Show[Interval[A]] = new Show[Interval[A]] {
		override def shows(f: Interval[A]): String = s"Start ${aShow shows f.start} End:  ${aShow shows f.end} "
	}

}

object IntervalShows extends IntervalShows


trait PaymentPeriodShows {

	implicit def paymentPeriodShow[A](implicit intervalShow: Show[Interval[A]]): Show[PaymentPeriod[A]] = new Show[PaymentPeriod[A]] {
		override def shows(f: PaymentPeriod[A]) = s"Actual ${intervalShow shows f.actual} reference ${f.reference map intervalShow.shows}"

	}

}

object PaymentPeriodShows extends PaymentPeriodShows

