package org.scalafin.date.util

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.mutable._
import java.util.Calendar
import java.util.GregorianCalendar
import org.joda.time.DateMidnight


@RunWith(classOf[JUnitRunner])
class ISDADateFormatTest extends Specification {

  val rightNow = DateMidnight.now
  val processedDateOutputPattern = """\d{4}/\d{1,2}/\d{1,2}""".r
  val customDelimiter = "&"
  val processedDateOutputPatternWithDelimiter = "\\d{4}" + customDelimiter + "\\d{1,2}" + customDelimiter + "\\d{1,2}"

  "The returned format" should {
    "be like YYYY/M/D" in {
      { processedDateOutputPattern findFirstIn ISDADateFormat.format(rightNow) match { case Some(_) => true case _ => false } } must beTrue
      ISDADateFormat.format(new DateMidnight(new GregorianCalendar(2005, 0, 1))) must beMatching("2005/1/1")
      ISDADateFormat.format(new DateMidnight(new GregorianCalendar(2005, 9, 1))) must beMatching("2005/10/1")
      ISDADateFormat.format(new DateMidnight(new GregorianCalendar(2005, 5, 10))) must beMatching("2005/6/10")
      ISDADateFormat.format(new DateMidnight(new GregorianCalendar(2005, 10, 20))) must beMatching("2005/11/20")
    }
  }

  "The returned formatFixedLength" should {
    "contains 10 characters" in {
      ISDADateFormat.formatFixedLength(rightNow) must be size 10
      ISDADateFormat.formatFixedLength(new DateMidnight(new GregorianCalendar(2005, 0, 1))) must beMatching("2005/01/01")
      ISDADateFormat.formatFixedLength(new DateMidnight(new GregorianCalendar(2005, 9, 1))) must beMatching("2005/10/01")
      ISDADateFormat.formatFixedLength(new DateMidnight(new GregorianCalendar(2005, 5, 10))) must beMatching("2005/06/10")
      ISDADateFormat.formatFixedLength(new DateMidnight(new GregorianCalendar(2005, 10, 20))) must beMatching("2005/11/20")
    }
  }

  "The returned formatCompact" should {
    "contains 8 characters" in {
      ISDADateFormat.formatCompact(rightNow) must be size 8
    }
  }

  "The returned formatFixedLength with delimiter" should {
    s"be like YYYY${customDelimiter}M${customDelimiter}D" in {
      { processedDateOutputPatternWithDelimiter.r findFirstIn ISDADateFormat.formatFixedLength(rightNow, customDelimiter) match { case Some(_) => true case _ => false } } must beTrue
    }
  }
  //TODO fix
//  "The null calendar" should {
//    "be null" in {
//      ISDADateFormat.format(null) must beNull
//      ISDADateFormat.formatFixedLength(null) must beNull
//    }
//  }

}
