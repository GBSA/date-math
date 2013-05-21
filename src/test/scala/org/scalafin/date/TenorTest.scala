package org.scalafin.date

import org.junit.runner.RunWith
import org.specs2.mutable._
import java.util.Calendar
import org.specs2.runner.JUnitRunner
import scalaz.Success
import org.specs2.matcher.ThrownMessages

@RunWith(classOf[JUnitRunner])
class TenorTest extends Specification with ThrownMessages {

  "Constants" should {
    "be well set" in {
      assertTenor("SN", 0, Calendar.DAY_OF_YEAR) must beTrue
      assertTenor("S/N", 0, Calendar.DAY_OF_YEAR) must beTrue
      assertTenor("ON", 0, Calendar.DAY_OF_YEAR) must beTrue
      assertTenor("O/N", 0, Calendar.DAY_OF_YEAR) must beTrue
      assertTenor("TN", 1, Calendar.DAY_OF_YEAR) must beTrue
      assertTenor("T/N", 1, Calendar.DAY_OF_YEAR) must beTrue
      assertTenor("3D", 3, Calendar.DAY_OF_YEAR) must beTrue
      assertTenor("10D", 10, Calendar.DAY_OF_YEAR) must beTrue
      assertTenor("1W", 7, Calendar.DAY_OF_YEAR) must beTrue
      assertTenor("2W", 14, Calendar.DAY_OF_YEAR) must beTrue
      assertTenor("20W", 140, Calendar.DAY_OF_YEAR) must beTrue
      assertTenor("1M", 1, Calendar.MONTH) must beTrue
      assertTenor("2M", 2, Calendar.MONTH) must beTrue
      assertTenor("20M", 20, Calendar.MONTH) must beTrue
      assertTenor("1Y", 1, Calendar.YEAR) must beTrue
      assertTenor("2Y", 2, Calendar.YEAR) must beTrue
      assertTenor("20Y", 20, Calendar.YEAR) must beTrue
    }

  }

  def assertTenor(toParse: String, periodAmount: Int, periodUnit: Int): Boolean = {
    val tenor = Tenor(toParse) match {
      case Success(x) => x
      case _ => fail("Can't create Tenor with " + toParse)
    }
    (tenor.amount == periodAmount) && (tenor.calendarUnit == periodUnit)
  }

}