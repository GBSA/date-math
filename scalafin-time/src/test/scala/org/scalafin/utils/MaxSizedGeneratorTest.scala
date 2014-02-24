package org.scalafin.utils

import org.specs2.{ScalaCheck, Specification}
import org.scalacheck.{Prop, Gen, Arbitrary}
import org.joda.time.{DateTime, DateMidnight}
import org.scalafin.datemath.test.{FromAmericanDiscoveryToJupiter, JodaTimeGenerators}
import org.scalafin.datemath.utils.OrderingImplicits
import org.specs2.matcher.Parameters
import org.scalacheck.util.Pretty

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 24/02/14
 * Time: 09:34
 *
 */
class MaxSizedGeneratorTest extends Specification
                                    with IntervalGenerators
                                    with ScalaCheck
                                    with JodaTimeGenerators
                                    with FromAmericanDiscoveryToJupiter
                                    with OrderingImplicits {

	import MaxSizedGenerator._

	implicit val params =  Parameters(workers=4)

	override val defaultPrettyParams = Pretty.Params(2)

	implicit val intervalBuilder = DefaultIntervalBuilder

		def is = s2"""
									The Max Sized Interval Arbitrary works correctly $e1
							"""
		def e1 = {

			Prop.forAll(Arbitrary.arbitrary[Long]){
				(l:Long) => {
					implicit val dateTimeArbitraryArbitrary = Arbitrary.arbitrary[DateTime]
					implicit val longGen = Gen.choose(0L,l)
					implicit val adder  = (dateTime:DateTime,millis:Long) => dateTime plus millis
					implicit val arb = maxSizedIntervalArbitrary[DateTime,Long]
					Prop.forAll{
						(interval:Interval[DateTime]) =>  (interval.end.getMillis - interval.start.getMillis ) must beLessThanOrEqualTo(l)
					}
				}
			}

		}






}
