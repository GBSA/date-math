package com.gottexbrokers.datemath.utils

import org.specs2.Specification
import com.gottexbrokers.datemath.utils.DatePeriodOrderings.{ EndPointOrdering, MidPointOrdering, StartPointOrdering }
import org.joda.time.DateTime
import org.specs2.ScalaCheck
import org.scalacheck.Arbitrary._
import java.util.Date
import org.scalacheck.Prop
import org.specs2.matcher.Parameters
import org.scalacheck.util.Pretty
import com.gottexbrokers.datemath.test.{ LongGeneratorWithNoOverflow, JodaTimeGenerators }

import com.gottexbrokers.datemath.{ Period, TimePeriod }

class DatePeriodOrderingsTest extends Specification with ScalaCheck with JodaTimeGenerators with LongGeneratorWithNoOverflow {

  import com.gottexbrokers.datemath.utils.OrderingImplicits._

  implicit val parameters = Parameters(minTestsOk = 500)

  override val defaultPrettyParams = Pretty.Params(2)

  override def is = s2"""

	Start Point ordering
			should work correctly on a known example        $e1
		    should work correctly on random generated data  $e2

	Mid point ordering
			should work correctly on a known example       $e3

	End point ordering
		    should work correctly on a known example          $e4
		    should work correctly on random generated data    $e5
	"""

  // For variance reason
  def buildPeriod(start: DateTime, end: DateTime): Period[DateTime] = new TimePeriod[DateTime](start, end, start, end)

  def e1 = {
    import StartPointOrdering._
    val start1 = new DateTime(2010, 1, 3, 0, 0)
    val end1 = new DateTime(2010, 3, 3, 0, 0)
    val start2 = new DateTime(2010, 1, 4, 0, 0)
    val end2 = new DateTime(2010, 1, 5, 0, 0)
    val start3 = new DateTime(2010, 1, 5, 0, 0)
    val end3 = new DateTime(2010, 1, 5, 0, 0)
    val interval = buildPeriod(start1, end1)
    val interval2 = buildPeriod(start2, end2)
    val interval3 = buildPeriod(start3, end3)
    interval must <(interval2)
    interval must <(interval3)
    interval3 must >(interval2)
  }

  def e2 = {
    import StartPointOrdering._
    Prop.forAll { (date1: DateTime, date2: DateTime) =>
      ((date1 compareTo date2) > 0) ==> {
        val end1 = date1 plusDays 4
        val end2 = date2 plusDays 4
        val interval = buildPeriod(date1, end1)
        val interval2 = buildPeriod(date2, end2)
        interval must >(interval2)
      }
    }
  }

  def e3 = {
    import MidPointOrdering._
    val start1 = new DateTime(2010, 1, 3, 0, 0)
    val end1 = new DateTime(2010, 3, 3, 0, 0)
    val start2 = new DateTime(2010, 1, 4, 0, 0)
    val end2 = new DateTime(2010, 3, 4, 0, 0)
    val start3 = new DateTime(2010, 1, 5, 0, 0)
    val end3 = new DateTime(2010, 1, 5, 0, 0)
    val interval = buildPeriod(start1, end1)
    val interval2 = buildPeriod(start2, end2)
    val interval3 = buildPeriod(start3, end3)
    interval must <(interval2)
    interval must <(interval3)
    interval3 must >(interval2)
  }

  def e4 = {
    import EndPointOrdering._
    val start1 = new DateTime(2010, 1, 3, 0, 0)
    val end1 = new DateTime(2010, 3, 6, 0, 0)
    val start2 = new DateTime(2010, 1, 4, 0, 0)
    val end2 = new DateTime(2010, 1, 5, 0, 0)
    val start3 = new DateTime(2010, 1, 5, 0, 0)
    val end3 = new DateTime(2010, 1, 5, 0, 0)
    val interval = buildPeriod(start1, end1)
    val interval2 = buildPeriod(start2, end2)
    val interval3 = buildPeriod(start3, end3)
    interval must >(interval2)
    interval must >(interval3)
    (interval3 must beGreaterThanOrEqualTo(interval2)) and (interval2 must beGreaterThanOrEqualTo(interval3))
  }

  def e5 = {
    import EndPointOrdering._
    Prop.forAll { (end1: DateTime, end2: DateTime) =>
      ((end1 compareTo end2) > 0) ==> {
        val start1 = end1 minusDays 4
        val start2 = end2 minusDays 4
        val interval = buildPeriod(start1, end1)
        val interval2 = buildPeriod(start2, end2)
        interval must >(interval2)
      }
    }
  }

}
