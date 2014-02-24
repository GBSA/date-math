package org.scalafin.datemath.utils

import org.specs2.Specification
import org.scalafin.datemath.utils.DatePeriodOrderings.{EndPointOrdering, MidPointOrdering, StartPointOrdering}
import org.joda.time.{DateMidnight, DateTime}
import org.scalafin.utils.{DefaultIntervalBuilder, IntervalBuilder}
import org.specs2.ScalaCheck
import org.scalacheck.Arbitrary._
import java.util.Date
import org.scalacheck.Prop
import org.specs2.matcher.Parameters
import org.scalacheck.util.Pretty
import org.scalafin.datemath.test.{LongGeneratorWithNoOverflow, JodaTimeGenerators}


/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 10/02/14
 * Time: 12:37
 *
 */
class DatePeriodOrderingsTest extends Specification with ScalaCheck with JodaTimeGenerators with LongGeneratorWithNoOverflow{

  import org.scalafin.datemath.utils.OrderingImplicits._

  implicit val parameters = Parameters(minTestsOk = 500)

  override val defaultPrettyParams = Pretty.Params(2)



	override  def is = s2"""

	Start Point ordering
			should work correctly on a known example        $e1
		  should work correctly on random generated data  $e2

	Mid point ordering
			"should work correctly on a known example       $e3

	End point ordering
		should work correctly on a known example          $e4
		should work correctly on random generated data    $e5

	"""

	def e1 = {
		import StartPointOrdering._
		val start1 = new DateMidnight(2010,1,3)
		val end1 = new DateMidnight(2010,3,3)
		val start2 = new DateMidnight(2010,1,4)
		val end2 = new DateMidnight(2010,1,5)
		val start3 = new DateMidnight(2010,1,5)
		val end3 = new DateMidnight(2010,1,5)
		val interval = DefaultIntervalBuilder(start1,end1).toOption.get
		val interval2 = DefaultIntervalBuilder(start2,end2).toOption.get
		val interval3 = DefaultIntervalBuilder(start3,end3).toOption.get
		interval must <(interval2)
		interval must <(interval3)
		interval3 must >(interval2)
	}

	def e2 = {
		import StartPointOrdering._
		Prop.forAll{(date1:DateTime, date2:DateTime) => ((date1 compareTo date2) > 0) ==> {
			val end1 = date1 plusDays 4
			val end2  = date2 plusDays 4
			val interval = DefaultIntervalBuilder(date1,end1).toOption.get
			val interval2 = DefaultIntervalBuilder(date2,end2).toOption.get
			interval must >(interval2)
		} }
	}


  def e3 = {
      import MidPointOrdering._
      val start1 = new DateMidnight(2010,1,3)
      val end1 = new DateMidnight(2010,3,3)
      val start2 = new DateMidnight(2010,1,4)
      val end2 = new DateMidnight(2010,3,4)
      val start3 = new DateMidnight(2010,1,5)
      val end3 = new DateMidnight(2010,1,5)
      val interval = DefaultIntervalBuilder(start1,end1).toOption.get
      val interval2 = DefaultIntervalBuilder(start2,end2).toOption.get
      val interval3 = DefaultIntervalBuilder(start3,end3).toOption.get
      interval must <(interval2)
      interval must <(interval3)
      interval3 must >(interval2)
    }

	def e4 = {
		import EndPointOrdering._
		val start1 = new DateMidnight(2010,1,3)
		val end1 = new DateMidnight(2010,3,6)
		val start2 = new DateMidnight(2010,1,4)
		val end2 = new DateMidnight(2010,1,5)
		val start3 = new DateMidnight(2010,1,5)
		val end3 = new DateMidnight(2010,1,5)
		val interval = DefaultIntervalBuilder(start1,end1).toOption.get
		val interval2 = DefaultIntervalBuilder(start2,end2).toOption.get
		val interval3 = DefaultIntervalBuilder(start3,end3).toOption.get
		interval must >(interval2)
		interval must >(interval3)
		(interval3 must beGreaterThanOrEqualTo(interval2)) and (interval2 must beGreaterThanOrEqualTo(interval3))
	}

	def e5 = {
		import EndPointOrdering._
		Prop.forAll{(end1:DateTime, end2:DateTime) => ((end1 compareTo end2) > 0) ==> {
			val start1 = end1 minusDays  4
			val start2 = end2 minusDays 4
			val interval = DefaultIntervalBuilder(start1,end1).toOption.get
			val interval2 = DefaultIntervalBuilder(start2,end2).toOption.get
			interval must >(interval2)
		}}
	}




}
