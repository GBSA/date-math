package com.gottexbrokers.datemath

import org.joda.time.{Years, DateTime, ReadableDateTime, DateTimeConstants}
import scala.annotation.tailrec
import com.gottexbrokers.datemath.utils.{OrderingImplicits, DateAdjustmentTools}


/**
 *
 * <p>'''Standard / common implementation of daycount conventions'''</p>
 * <p>Disclaimer: there is no central agency responsible for defining, standardizing, and documenting day count conventions.
 * There are many day count conventions, but there is no standard terminology or notation. Not only are multiple terms used for the same convention,
 * but the same term may be used for different conventions. The definitions of certain agencies are also vague at best.</p>
 *
 *
 * <table>
 *  <thead>
 *    <tr>
 *     <th style="padding:5px;border-top:2px solid black; border-bottom: 2px solid black; border-right: 1px solid black;border-left: 2px solid black;">Short form</th>
 *     <th style="padding:5px;border-top:2px solid black; border-bottom: 2px solid black; border-right: 1px solid black;border-left: 1px solid black;">Class name</th>
 *     <th style="padding:5px;border-top:2px solid black; border-bottom: 2px solid black; border-right: 1px solid black;border-left: 1px solid black;">ICMA</th>
 *     <th style="padding:5px;border-top:2px solid black; border-bottom: 2px solid black; border-right: 1px solid black;border-left: 1px solid black;">ISDA 2006</th>
 *     <th style="padding:5px;border-top:2px solid black; border-bottom: 2px solid black; border-right: 1px solid black;border-left: 1px solid black;">ISDA 2000</th>
 *     <th style="padding:5px;border-top:2px solid black; border-bottom: 2px solid black; border-right: 1px solid black;border-left: 1px solid black;">SIFMA</th>
 *     <th style="padding:5px;border-top:2px solid black; border-bottom: 2px solid black; border-right: 1px solid black;border-left: 1px solid black;">SIA</th>
 *     <th style="padding:5px;border-top:2px solid black; border-bottom: 2px solid black; border-right: 2px solid black;border-left: 1px solid black;">SWX AI</th>
 *    </tr>
 *  </thead>
 *  <tbody>
 *    <tr>
 *      <td style="padding:5px;border-right:1px solid black;border-left:2px solid black;border-bottom:2px solid black;text-align:center;">30U/360</td>
 *      <td style="padding:5px;border-right:1px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;"><a href="DayCountCalculators$$US30360DayCountCalculator$.html">US30360DayCountCalculator</a></td>
 *      <td style="padding:5px;border-right:1px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;">&nbsp;</td>
 *      <td style="padding:5px;border-right:1px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;">&nbsp;</td>
 *      <td style="padding:5px;border-right:1px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;">&nbsp;</td>
 *      <td style="padding:5px;border-right:1px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;">30/360</td>
 *      <td style="padding:5px;border-right:1px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;">&nbsp;</td>
 *      <td style="padding:5px;border-right:2px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;">US</td>
 *    </tr>
 *    <tr>
 *      <td  style="padding:5px;border-right:1px solid black;border-left:2px solid black;border-bottom:2px solid black;text-align:center;">30E/360</td>
 *      <td style="padding:5px;border-right:1px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;"><a href="DayCountCalculators$$EU30360DayCountCalculator$.html">EU30360DayCountCalculator</a></td>
 *      <td style="padding:5px;border-right:1px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;">251.1(ii), 251.2</td>
 *      <td style="padding:5px;border-right:1px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;">4.16(g)</td>
 *      <td style="padding:5px;border-right:1px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;">&nbsp;</td>
 *      <td style="padding:5px;border-right:1px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;">&nbsp;</td>
 *      <td style="padding:5px;border-right:1px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;">&nbsp;</td>
 *      <td style="padding:5px;border-right:2px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;">Special German</td>
 *    </tr>
 *    <tr>
 *      <td  style="padding:5px;border-right:1px solid black;border-left:2px solid black;border-bottom:2px solid black;text-align:center;">30E/360 ISDA</td>
 *      <td style="padding:5px;border-right:1px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;"><a href="DayCountCalculators$$EU30360ISDADayCountCalculator$.html">EU30360ISDADayCountCalculator</a></td>
 *      <td style="padding:5px;border-right:1px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;">&nbsp;</td>
 *      <td style="padding:5px;border-right:1px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;">4.16(h)</td>
 *      <td style="padding:5px;border-right:1px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;">4.16(f)</td>
 *      <td style="padding:5px;border-right:1px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;">&nbsp;</td>
 *      <td style="padding:5px;border-right:1px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;">&nbsp;</td>
 *      <td style="padding:5px;border-right:2px solid black;border-left:1px solid black;border-bottom:2px solid black;text-align:center;">German</td>
 *    </tr>
 *
 *
 *   </tbody>
 * </table>
 *
 *
 */
object DayCountCalculators {

	import com.gottexbrokers.datemath.utils.RichJodaTimeExtensions._

	private implicit def toDateTime(readableDateTime: ReadableDateTime) = readableDateTime.toDateTime

	private [datemath] def isLastDayOfFebruary(date: ReadableDateTime): Boolean = {
		date.getDayOfMonth == date.dayOfMonth.maxValue && date.getMonthOfYear == DateTimeConstants.FEBRUARY
	}

	private [datemath] def isAfter27OfFebruary(date: ReadableDateTime): Boolean = {
		date.getDayOfMonth > 27 && date.getMonthOfYear == DateTimeConstants.FEBRUARY
	}

	case class YearMonthDayTuple(days:Int, months:Int, years:Int){

		def as30360 = days + months * 30 + years * 360

		def withAtMost30Days = copy(days=Math.min(days,30))
	}


	object YearMonthDayTuple{

		def apply(readableDateTime:ReadableDateTime):YearMonthDayTuple = YearMonthDayTuple(readableDateTime.getDayOfMonth,readableDateTime.getMonthOfYear,readableDateTime.getYear)

	}

	trait Simple360DayCountCalculator  extends DayCountCalculator {

		protected def readableDateTimeToTuples(start:ReadableDateTime,end:ReadableDateTime): (YearMonthDayTuple,YearMonthDayTuple)

		override def calculateDayCountFraction(start: ReadableDateTime, end: ReadableDateTime): Double = {
			val (tuple1,tuple2) = readableDateTimeToTuples(start,end)
			(tuple2.as30360 - tuple1.as30360)/360d
		}

	}

	case class US30360DayCountCalculator(eom:Boolean) extends Simple360DayCountCalculator {


		def readableDateTimeToTuples(start:ReadableDateTime,end:ReadableDateTime): (YearMonthDayTuple,YearMonthDayTuple) = {
			val tuple2 = {
				val temp = YearMonthDayTuple(end)
				if(isLastDayOfFebruary(start) && isLastDayOfFebruary(end))
					temp.copy(days = 30)
				else
					temp
			}
			val tuple1 = {
				val temp = YearMonthDayTuple(start)
				if(isLastDayOfFebruary(start))
					temp.copy(days = 30)
				else
					temp

			}
			if(tuple1.days>29)
				(tuple1.withAtMost30Days,tuple2.withAtMost30Days)
			else
				(tuple1.withAtMost30Days,tuple2)
		}

	}

	/**
	 * '''30E/360'''

	 * Date adjustment rules:
	 *  - If D1 is 31, then change D1 to 30.
	 *  - If D2 is 31, then change D2 to 30.
	 *
	 * Other names:
	 *  - 30/360 ICMA
	 *  - 30S/360
	 *  - Eurobond basis (ISDA 2006)
	 *  - Special German
	 *  - ISMA-30/360 (SWX AI /SIX naming convention)
	 *
	 * Sources:
	 *  - ICMA Rule 251.1(ii), 251.2
	 *  - ISDA 2006 Section 4.16(g)

	 */
	case object EU30360DayCountCalculator extends Simple360DayCountCalculator {


		override protected def readableDateTimeToTuples(start: ReadableDateTime, end: ReadableDateTime): (YearMonthDayTuple, YearMonthDayTuple) = {
			(YearMonthDayTuple(start).withAtMost30Days, YearMonthDayTuple(end).withAtMost30Days)
		}

	}


	/**
	 * '''30E/360 ISDA'''
	 *
	 * Date adjustment rules:
	 *   - If D1 is the last day of the month, then change D1 to 30.
	 *   - If D2 is the last day of the month (unless Date2 is the maturity date and M2 is February), then change D2 to 30.
	 *
	 * Other names:
	 *  - 30E/360 ISDA
	 *  - Eurobond basis (ISDA 2000)
	 *  - German
	 *
	 * Sources:
	 *  - ISDA 2006 Section 4.16(h)
	 *
	 */
	case object EU30360ISDADayCountCalculator extends Simple360DayCountCalculator {


		override protected def readableDateTimeToTuples(start: ReadableDateTime, end: ReadableDateTime): (YearMonthDayTuple, YearMonthDayTuple) = {
			val tuple2 = {
				val temp = YearMonthDayTuple(end)
				if(isLastDayOfFebruary(end))
					temp
				else
					temp.withAtMost30Days
			}
			(YearMonthDayTuple(start).withAtMost30Days,tuple2)
		}

	}

	case object IT30360DayCountCalculator extends Simple360DayCountCalculator {

		private def readableDateTimeToTuple(dateTime:ReadableDateTime):YearMonthDayTuple = {
			val temp = YearMonthDayTuple(dateTime)
			if (isAfter27OfFebruary(dateTime)) temp.copy(days=30) else temp
		}


		override protected def readableDateTimeToTuples(start: ReadableDateTime, end: ReadableDateTime): (YearMonthDayTuple, YearMonthDayTuple) = {
			val endTuple = readableDateTimeToTuple(end).withAtMost30Days
			val startTuple = readableDateTimeToTuple(start)
			(startTuple.copy(months = startTuple.months+1, days = -Math.max(0,30-startTuple.days)),endTuple)
		}

	}






	trait NormalizedActualDayCountCalculator extends DayCountCalculator {

		def normalizationFactor: Double

		override def calculateDayCountFraction(start: ReadableDateTime, end: ReadableDateTime): Double = {
			(start daysTo end) / normalizationFactor
		}

	}

	case object Actual360DayCountCalculator extends NormalizedActualDayCountCalculator {
		override val normalizationFactor: Double = 360d
	}


	case object Actual365FixedDayCountCalculator extends NormalizedActualDayCountCalculator {
		override val normalizationFactor: Double = 365d
	}

	case object Actual366DayCountCalculator extends NormalizedActualDayCountCalculator {

		override val normalizationFactor: Double = 366d

	}

	case object AFBActualActualDayCountCalculator extends DayCountCalculator {

		import OrderingImplicits._


		override def calculateDayCountFraction(start: ReadableDateTime, end: ReadableDateTime): Double = {
			val timePeriod = TimePeriod(start,end,start,end)
			val splittedPeriods = timePeriod splitInArrear previousDateBuilder
			(splittedPeriods map {
				period => tau(period.start,period.end)
			}).sum
		}

		private def previousDateBuilder(readableDateTime: ReadableDateTime) = {
			val date = readableDateTime.toDateTime
			date minusDays date.dayOfYear.maxValue
		}

		private def february29InYear(year: Int): Option[DateTime] = {
			val dateTime = new DateTime(year, 1, 1, 0, 0)
			if (dateTime.dayOfYear.maxValue == 366)
				Some(dateTime.withMonthOfYear(2).withDayOfMonth(29))
			else
				None
		}

		private def diy(start:ReadableDateTime,end:ReadableDateTime): Double = {
			val dates = Seq(start,end)
			val februaries29 = for {
				date <- dates
			 february29 <- february29InYear(date.getYear) if(february29 >= start && february29 <= end)
			} yield february29
			if (februaries29.isEmpty)
				365.0
			else
				366.0
		}

		private def tau(start:ReadableDateTime,end:ReadableDateTime): Double = (start daysTo end) / diy(start,end)

	}




	/**
	 * '''Actual/Actual ISDA'''
	 *
	 * Other names are:
   *  - Actual/Actual
   *  - Act/Act
   *  - Actual/365
   *  - Act/365
	 *
	 * Sources:
	 *  - ISDA 2006 Section 4.16(b)
	 */
	case object ISDAActualActualDayCountCalculator extends DayCountCalculator {


		override def calculateDayCountFraction(start: ReadableDateTime, end: ReadableDateTime): Double = {
			val timePeriod = TimePeriod(start,end,start,end)
			val splittedPeriods = timePeriod splitInArrear  previousDateBuilder
			(splittedPeriods map tau).sum
		}

		private def previousDateBuilder(readableDateTime: ReadableDateTime) = {
			val date = readableDateTime.toDateTime
			date minusDays date.dayOfYear.maxValue
		}

		private def tau(dateRange: TimePeriod[ReadableDateTime]): Double = {
			val maxDaysInFirstYear = dateRange.start.toDateTime.dayOfYear().maxValue
			(dateRange.start daysTo dateRange.end) / maxDaysInFirstYear
		}


	}


	case class Business252DayCountCalculator(hc: HolidayCalendar) extends DayCountCalculator {

			import OrderingImplicits._

			import DateAdjustmentTools._

			@tailrec
			private def getBusinessDaysBetween(start:ReadableDateTime,end:ReadableDateTime, counter:Int):Long ={
				if (start >= end) {
					counter + 1
				} else {
					val advancedResult = advance(start,hc, 1,UnadjustedBusinessDayConvention)
					getBusinessDaysBetween(advancedResult, end, counter+1)
				}
			}
			// In an interval the start never follows the end, so we are sure the API call is correct

		override def calculateDayCountFraction(start: ReadableDateTime, end: ReadableDateTime): Double = 			getBusinessDaysBetween(start,end,0)/252.0

	}


	case class ISMAActualActualDayCountCalculator(frequency: Frequency with ExactFitInYear) extends DayCountCalculator {


		override def calculateDayCountFraction(start:ReadableDateTime,end:ReadableDateTime): Double = {

			val firstCouponDate = frequency addTo start
			val daysInPeriod = start daysTo firstCouponDate
			val daysInRange: Double = start daysTo end
			daysInRange / (frequency.periodsPerYear * daysInPeriod)
		}


	}

}

