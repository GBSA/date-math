package org.scalafin.date

import org.joda.time.DateMidnight

trait DateOrdering {
  implicit val ordering: Ordering[DateMidnight] = new Ordering[DateMidnight] {
    def compare(x: DateMidnight, y: DateMidnight): Int = x compareTo y
  }
}

object DateOrdering extends DateOrdering
