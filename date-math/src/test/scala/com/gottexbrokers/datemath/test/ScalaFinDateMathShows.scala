package com.gottexbrokers.datemath.test

import scalaz.Show
import org.joda.time.base.BaseDateTime
import com.gottexbrokers.datemath.Period

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

	implicit def intervalShow[A](implicit aShow: Show[A]): Show[Period[A]] = new Show[Period[A]] {
		override def shows(f: Period[A]): String = s"Start ${aShow shows f.start} End:  ${aShow shows f.end} "
	}

}

object IntervalShows extends IntervalShows




