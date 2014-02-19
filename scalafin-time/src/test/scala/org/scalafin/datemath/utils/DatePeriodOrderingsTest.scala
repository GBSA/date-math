package org.scalafin.datemath.utils

import org.specs2.mutable.Specification
import org.scalafin.datemath.utils.DatePeriodOrderings.{MidPointOrdering, StartPointOrdering}
import org.joda.time.{DateMidnight, DateTime}
import org.scalafin.utils.IntervalBuilder
import org.specs2.ScalaCheck
import org.scalacheck.Arbitrary._
import java.util.Date
import org.scalacheck.Prop
import org.specs2.matcher.Parameters
import org.scalacheck.util.Pretty


/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 10/02/14
 * Time: 12:37
 *
 */
class DatePeriodOrderingsTest extends Specification with ScalaCheck {

  import org.scalafin.datemath.utils.OrderingImplicits._

  implicit val parameters = Parameters(minTestsOk = 500)

  override val defaultPrettyParams = Pretty.Params(2)

  implicit val dateGen = arbitrary[Date].map{ javaDate => new DateTime(javaDate.getTime)}

  "Ordering  " >> {

    "should work correctly when based on interval start" >> {
      import StartPointOrdering._
      "Two dates one after the other and a fixed length interval" >> {
        Prop.forAll(dateGen,dateGen){(date1:DateTime, date2:DateTime) => ((date1 compareTo date2) > 0) ==> {
          val end1 = date1 plusDays 4
          val end2  = date1 plusDays 4
          val interval = IntervalBuilder(date1,end1).toOption.get
          val interval2 = IntervalBuilder(date2,end2).toOption.get
          interval must <(interval2)
        }}

        "a known example " >>  {

          val start1 = new DateMidnight(2010,1,3)
          val end1 = new DateMidnight(2010,3,3)
          val start2 = new DateMidnight(2010,1,4)
          val end2 = new DateMidnight(2010,1,5)
          val start3 = new DateMidnight(2010,1,5)
          val end3 = new DateMidnight(2010,1,5)
          val interval = IntervalBuilder(start1,end1).toOption.get
          val interval2 = IntervalBuilder(start2,end2).toOption.get
          val interval3 = IntervalBuilder(start3,end3).toOption.get
          interval must <(interval2)
          interval must <(interval3)
          interval3 must >(interval2)
        }
      }
    }

    "should work correctly when based on interval mid" >> {
      import MidPointOrdering._
      val start1 = new DateMidnight(2010,1,3)
      val end1 = new DateMidnight(2010,3,3)
      val start2 = new DateMidnight(2010,1,4)
      val end2 = new DateMidnight(2010,3,4)
      val start3 = new DateMidnight(2010,1,5)
      val end3 = new DateMidnight(2010,1,5)
      val interval = IntervalBuilder(start1,end1).toOption.get
      val interval2 = IntervalBuilder(start2,end2).toOption.get
      val interval3 = IntervalBuilder(start3,end3).toOption.get
      interval must <(interval2)
      interval must <(interval3)
      interval3 must >(interval2)
    }

    "should work correctly when based on interval end" >> {
      import org.scalafin.datemath.utils.DatePeriodOrderings.EndPointOrdering._
      "Two dates one after the other and a fixed length interval" >> {
        Prop.forAll(dateGen,dateGen){(end1:DateTime, end2:DateTime) => ((end1 compareTo end2) > 0) ==> {
          val start1 = end1 minusDays  4
          val start2 = end2 minusDays 4
          val interval = IntervalBuilder(start1,end1).toOption.get
          val interval2 = IntervalBuilder(start2,end2).toOption.get
          interval must >(interval2)
        }}
      }

      "a known example " >>  {

        val start1 = new DateMidnight(2010,1,3)
        val end1 = new DateMidnight(2010,3,6)
        val start2 = new DateMidnight(2010,1,4)
        val end2 = new DateMidnight(2010,1,5)
        val start3 = new DateMidnight(2010,1,5)
        val end3 = new DateMidnight(2010,1,5)
        val interval = IntervalBuilder(start1,end1).toOption.get
        val interval2 = IntervalBuilder(start2,end2).toOption.get
        val interval3 = IntervalBuilder(start3,end3).toOption.get
        interval must >(interval2)
        interval must >(interval3)
        (interval3 must beGreaterThanOrEqualTo(interval2)) and (interval2 must beGreaterThanOrEqualTo(interval3))
      }
    }
  }



}
