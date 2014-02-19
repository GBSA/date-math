package org.scalafin.datemath.test

import org.specs2.text.Sentences
import org.joda.time.{DateTime, LocalDate, ReadablePartial, ReadableDateTime}
import org.specs2.matcher.{MustMatchers, MatchResult, Expectable, Matcher}
import java.util.Date
import org.scalafin.datemath.{HolidayCalendar, BusinessDayConvention}
import com.mbc.jfin.holiday.{BusinessDayConvention => JFinBusinessDayConvention, HolidayCalendar => MbcHolidaycalendar, DateAdjustmentService}


/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 19/02/14
 * Time: 13:29
 *
 */

//TODO: check naming conventions
//TODO: add scalarestyle maven plugin
trait ScalafinDateMathMatchers {



}

trait JodaTimeDateMatchers extends Sentences{

  def beEquivalentTo(readableDateTime: ReadableDateTime): Matcher[ReadablePartial] = new Matcher[ReadablePartial] {
    override def apply[S <: ReadablePartial](t: Expectable[S]): MatchResult[S] = {
      val partial = t.value
      val dateTime = partial toDateTime readableDateTime
      val matchResult = dateTime compareTo readableDateTime
      val message = s"$dateTime is equal to $readableDateTime"
      result(matchResult == 0, message, negateSentence(message), t)


    }
  }

}

trait AdjustmentContext {

  implicit def mbcHolidayCalendar:MbcHolidaycalendar

  implicit def scalaFinDateMathHolidayCalendar:HolidayCalendar

  implicit def adjustmentService:DateAdjustmentService

}

object AdjustmentContext {

  def apply[A<:MbcHolidaycalendar](cal:A, service:DateAdjustmentService)
    (implicit conversion: A => HolidayCalendar):AdjustmentContext = new AdjustmentContext {

    override implicit val adjustmentService: DateAdjustmentService = service

    override implicit val scalaFinDateMathHolidayCalendar: HolidayCalendar = conversion(cal)

    override implicit val mbcHolidayCalendar: MbcHolidaycalendar = cal
  }

}




trait BusinessDayConventionTupleMatchers extends JodaTimeDateMatchers with MustMatchers{



  def produceIdenticalAdjustmentOn(date:Date)(implicit adjustmentContext:AdjustmentContext):Matcher[(BusinessDayConvention, JFinBusinessDayConvention)] = new Matcher[(BusinessDayConvention, JFinBusinessDayConvention)]{

    val localDate = new LocalDate(date.getTime)

    val dateTime = new DateTime(date.getTime)

    import adjustmentContext._

    override def apply[S <: (BusinessDayConvention, JFinBusinessDayConvention)](t: Expectable[S]): MatchResult[S] = {
      val (scalafinDateMathConvention,jfinConvention) = t.value
      val jfinResultedAdjustDate = adjustmentContext.adjustmentService adjust (localDate, jfinConvention, adjustmentContext.mbcHolidayCalendar)
      val scalafinAdjustedDate = scalafinDateMathConvention adjust dateTime
      val matchResult = jfinResultedAdjustDate must beEquivalentTo(scalafinAdjustedDate)
      val message =  s"jfin.$jfinConvention and scalafin-datemath.$scalafinDateMathConvention adjust date $localDate on calendar $mbcHolidayCalendar : ${matchResult.message}"
      result(matchResult.isSuccess,message,negateSentence(message),t)
    }

  }


}