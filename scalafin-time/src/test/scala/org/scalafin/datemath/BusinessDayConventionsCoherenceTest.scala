package org.scalafin.datemath

import org.specs2.{ScalaCheck, Specification}
import org.scalafin.datemath.test._
import org.specs2.specification.Fragments
import java.util.Date
import com.mbc.jfin.holiday.{BusinessDayConvention => JFinBusinessDayConvention}
import org.scalacheck.Prop
import com.mbc.jfin.holiday.impl.DefaultDateAdjustmentServiceImpl


/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 19/02/14
 * Time: 15:48
 *
 */
class BusinessDayConventionsCoherenceTest extends Specification
                                                  with ScalaCheck
                                                  with FragmentBuildingTools
                                                  with CalendarsGenerators
                                                  with ScalafinDateMathTestInstances
                                                  with BusinessDayConventionTupleMatchers
																									with LongGeneratorWithNoOverflow{

	val tuple1 = BusinessDayConventions.UNADJUSTED -> JFinBusinessDayConvention.UNADJUSTED

	val tuple2 = BusinessDayConventions.FOLLOWING -> JFinBusinessDayConvention.FOLLOWING

	val tuple3 = BusinessDayConventions.MODIFIED_FOLLOWING -> JFinBusinessDayConvention.MODIFIED_FOLLOWING

	val tuple4 = BusinessDayConventions.MODIFIED_PRECEDING -> JFinBusinessDayConvention.MODIFIED_PRECEDING

	val tuple5 = BusinessDayConventions.MONTH_END_REFERENCE -> JFinBusinessDayConvention.MONTH_END_REFERENCE

	val tuple6 = BusinessDayConventions.PRECEDING -> JFinBusinessDayConvention.PRECEDING

	val tuples = Seq(tuple1, tuple2, tuple3, tuple4, tuple5, tuple6)

	val service = new DefaultDateAdjustmentServiceImpl()

	override def is: Fragments = testChunk(tuples, "The business day conventions should be equivalent between Jfin and scalafin-datemath", testTuple)

	def testTuple(tuple: (BusinessDayConvention, JFinBusinessDayConvention)) = {
		val (scalafinDateMathConvention, jfinConvention) = tuple
		val exampleName = s"jfin.$jfinConvention and scalafin-datemath.$scalafinDateMathConvention must adjust dates equally "
		 exampleName ! Prop.forAll{
									(calendar:SimpleMbcHolidayCalendar, dateMillis:Long) =>
								 val date = new Date(dateMillis)
									implicit val context = AdjustmentContext(calendar,service)
									tuple must produceIdenticalAdjustmentOn(date)


		}
	}


}

