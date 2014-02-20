package org.scalafin.utils

import org.scalacheck.{Arbitrary, Gen}
import scalaz.Validation

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 20/02/14
 * Time: 10:47
 *
 */
trait IntervalGenerators {

	def intervalArbitrary[A,B[_]<:Interval[_]](implicit ordering:Ordering[A], gen:Gen[A], intervalBuilder:IntervalBuilder[B]):Arbitrary[Validation[InvalidIntervalException,B[A]]] = {
		import Ordering.Implicits._
		Arbitrary[Validation[InvalidIntervalException,B[A]]] (
			for {
				start <-gen
				end <- gen.filter {_ >= start}
			} yield intervalBuilder.apply(start,end)

		)
	}





}
