package org.scalafin.utils

import org.specs2.{ScalaCheck, Specification}
import org.scalacheck.{Prop, Arbitrary}
import java.math.BigInteger
import scalaz.{Validation, Success}
import Arbitrary._
import org.specs2.matcher.Parameters
import org.scalacheck.util.Pretty


/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 20/02/14
 * Time: 10:46
 *
 */
class DefaultIntervalBuilderTest extends Specification with IntervalGenerators with ScalaCheck{

	implicit val parameters = Parameters(minTestsOk = 500)

	override val defaultPrettyParams = Pretty.Params(2)


	def is = s2"""
							The Interval generators
										will never fail for Int ${example[Int]}
									  will never fail for Long ${example[Long]}
										will never fail for Double ${example[Double]}
										will never fail for Float ${example[Float]}
										will never fail for BigDecimal ${example[BigDecimal]}
										will never fail for BigInteger ${example[BigInt]}
										will never fail for Byte ${example[Byte]}
						                                                            """

	implicit val intervalBuilder = DefaultIntervalBuilder

	def example[T](implicit ordering:Ordering[T], arbitrary:Arbitrary[T]) = {
		implicit val singleGenn = arbitrary.arbitrary
		implicit val intervalGenerators = intervalArbitrary[T,Interval]
		Prop.forAll{
			(t:Validation[InvalidIntervalException,Interval[T]]) => t must beLike{
				case Success(_) => ok
			}
		}

	}


}
