package org.scalafin.date.util

import org.joda.time.DateMidnight


trait DateMidnightOrdering {
	implicit val ordering : Ordering[DateMidnight] = new Ordering[DateMidnight]{
	  def compare(x:DateMidnight,y:DateMidnight):Int = x.compareTo(y)
	}
}

object DateMidnightOrdering extends DateMidnightOrdering
