package org.scalafin.datemath

import com.mbc.jfin.daycount.impl.{DaycountCalculator => JFinDaycountCalculator}
import com.mbc.jfin.daycount.impl.calculator.{AFBActualActualDaycountCalculator, Actual366DaycountCalculator, Actual365FixedDaycountCalculator, Actual360DaycountCalculator}
import org.scalafin.datemath.DayCountCalculators._

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 19/02/14
 * Time: 15:54
 *
 */
class DaycountCalculatorsCoherenceTest {

}

class StaticDayCountCalculatorCoherenceTest {

	val tuple1 = new Actual360DaycountCalculator() -> Actual360DayCountCalculator

	val tuple2 = new Actual365FixedDaycountCalculator() -> Actual365FixedDayCountCalculator

	val tuple3 = new Actual366DaycountCalculator() -> Actual366DayCountCalculator

	val tuple4 = new AFBActualActualDaycountCalculator() -> AFBActualActualDayCountCalculator


}
