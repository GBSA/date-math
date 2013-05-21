package org.scalafin.date

import org.specs2.mutable.Specification
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.joda.time.DateMidnight
import org.specs2.mutable._

@RunWith(classOf[JUnitRunner])
class SimpleDatePeriodTest extends Specification {
  val date1 = DateMidnight.now.withYear(2013).withMonthOfYear(5).withDayOfMonth(1)
  val date2 = DateMidnight.now.withYear(2013).withMonthOfYear(5).withDayOfMonth(9)

  "A date period" should {
    "Correctly execute toString" in {
      
      
      val dateRange = DateRange(date1, date2).toOption.get
      val datePeriod = SimplePeriod(dateRange)
      println(datePeriod.toString)
      datePeriod.toString must beMatching("2013/5/1 - 2013/5/9 with reference None - None")
    }
  }

//  "A null period" should {
//    "have null fields" in {
//      val datePeriod = SimpleDatePeriod(DateRange(null,null),Some(DateRange(null,null)))
//      datePeriod.startDate must beNull
//      datePeriod.endDate must beNull
//      datePeriod.referenceStartDate must beNull
//      datePeriod.referenceEndDate must beNull
//    }
//  }
//
//  "Having None reference Dates" should {
//    "work with valide dates" in {
//      val datePeriod = new DatePeriod(date1, date2)
//      datePeriod.startDate === date1
//      datePeriod.endDate === date2
//      datePeriod.referenceStartDate must beNone
//      datePeriod.referenceEndDate must beNone
//    }
//  }
//
//  "Comparator Midpoint" should {
//    "work with valid dates" in {
//      val date1 = DateMidnight.now.withYear(2005).withMonthOfYear(1).withDayOfMonth(1)
//      val date2 = DateMidnight.now.withYear(2006).withMonthOfYear(11).withDayOfMonth(30)
//      val datePeriod1 = new DatePeriod(date1, date1)
//      val datePeriod2 = new DatePeriod(date2, date2)
//
//      datePeriod1.compare(datePeriod1) === 0
//      datePeriod1.compare(datePeriod2) must beLessThan(0)
//      datePeriod2.compare(datePeriod1) must beGreaterThan(0)
//      datePeriod2.compare(datePeriod1) must beGreaterThan(0)
//    }
//  }
  

}