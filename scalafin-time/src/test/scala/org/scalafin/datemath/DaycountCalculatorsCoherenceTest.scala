package org.scalafin.datemath

import com.mbc.jfin.daycount.impl.{DaycountCalculator => JFinDaycountCalculator}
import com.mbc.jfin.daycount.impl.calculator._
import org.scalafin.datemath.DayCountCalculators._
import org.specs2.{ScalaCheck, Specification}
import org.scalafin.datemath.test._
import org.specs2.specification.Fragments
import org.scalacheck.Prop
import org.scalacheck.Arbitrary._
import java.util.Date

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 19/02/14
 * Time: 15:54
 *
 */
class DaycountCalculatorsCoherenceTest {

}

abstract class StaticDayCountCalculatorCoherenceTest  extends Specification
                                                     with ScalaCheck
                                                     with FragmentBuildingTools
                                                     with CalendarsGenerators
                                                     with ScalafinDateMathTestInstances
                                                     with BusinessDayConventionTupleMatchers {

	val tuple1 = new Actual360DaycountCalculator() -> Actual360DayCountCalculator

	val tuple2 = new Actual365FixedDaycountCalculator() -> Actual365FixedDayCountCalculator

	val tuple3 = new Actual366DaycountCalculator() -> Actual366DayCountCalculator

	val tuple4 = new AFBActualActualDaycountCalculator() -> AFBActualActualDayCountCalculator

	val tuple5 = new AFBActualActualDaycountCalculator() -> AFBActualActualDayCountCalculator

	val tuple6 = new EU30360DaycountCalculator() -> EU30360DayCountCalculator

	val tuple7 = new IT30360DaycountCalculator() -> IT30360DayCountCalculator

	val tuple8 = new IT30360DaycountCalculator() -> IT30360DayCountCalculator

	val tuples = Seq(tuple1,tuple2,tuple3,tuple4,tuple5,tuple6,tuple7,tuple8)

//	override def is: Fragments = testChunk(tuples, "The day count calculator should be equivalent between Jfin and scalafin-datemath", testTuple)
//
//	def testTuple(tuple: (DayCountCalculator, JFinDaycountCalculator)) = {
//		val (scalafinDateMathCalculator, jfinConvention) = tuple
//		s"jfin.$jfinConvention and scalafin-datemath.$scalafinDateMathCalculator must compute the same value " ! Prop.forAll(mbcHolidayCalendarGen, arbitrary[Date]) {
//			(calendar, date) => {
//				scalafinDateMathCalculator.apply() must _==
//				implicit val context = AdjustmentContext(calendar,
//					service)
//				tuple must produceIdenticalAdjustmentOn(date)
//
//
//			}
//		}
//	}

}
