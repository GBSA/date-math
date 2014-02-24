package org.scalafin.datemath.test

import org.specs2.Specification
import org.specs2.ScalaCheck
import org.scalacheck.Prop
import org.joda.time.{DateTime, LocalDate}
import com.mbc.jfin.util.DateUtils
import org.specs2.matcher.{MatchResult, Parameters}
import org.scalacheck.util.Pretty
import org.specs2.specification.Example


/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 24/02/14
 * Time: 12:54
 *
 */
class JavaToJodaTimeConversionsTest extends Specification with ScalaCheck with LongGeneratorWithNoOverflow with LongToJodaTimeConversions{

	implicit val params =  Parameters(workers=4,minTestsOk = 5000,maxSize = 5000)

	override val defaultPrettyParams = Pretty Params 2


	import org.scalafin.datemath.utils.RichJodaTimeExtensions._
	def e1 = "Implicit conversions should yield coherent result between random generate Longs used to build LocalDate and DateTime" ! Prop.forAll(
		(date1:Long, date2:Long) => {
		  testLong(date1,date2)
		}

	)

//	def is = e1 ^ p  ^ e2

	def is = e2

	def e2:Example = {
		val startDate = -2422054408000L
		val endDate = -3600000L
		"For a specific , known case, the equivalence yields " ! testLong(startDate,endDate)
	}

	def testLong(date1:Long, date2:Long):MatchResult[Any] = {
		val localDate1 = toJodaLocalDate(date1)
		val localDate2 = toJodaLocalDate(date2)
		val dateTime1 = toJodaDateTime(date1)
		val dateTime2 = toJodaDateTime(date2)
		val jfinResult = DateUtils daysBetween (localDate1,localDate2)
		val scalafinResult = dateTime1 daysTo dateTime2
		jfinResult must_== scalafinResult
	}



}
