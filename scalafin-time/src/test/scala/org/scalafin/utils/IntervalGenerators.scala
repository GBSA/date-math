package org.scalafin.utils

import org.scalacheck.{Arbitrary, Gen}
import scalaz.Validation
import Arbitrary._

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 20/02/14
 * Time: 10:47
 *
 */
trait IntervalGenerators {

	implicit def intervalArbitrary1[A](implicit ordering:Ordering[A], gen:Arbitrary[A], intervalBuilder:IntervalBuilder[Interval]):Arbitrary[Validation[InvalidIntervalException,Interval[A]]] = {
		import Ordering.Implicits._
		Arbitrary[Validation[InvalidIntervalException,Interval[A]]] (
			for {
				start <- arbitrary[A]
				end <- arbitrary[A].filter {_ >= start}
			} yield intervalBuilder apply (start,end)

		)
	}


	implicit  def intervalArbitrary[A](implicit ordering:Ordering[A], gen:Arbitrary[A], intervalBuilder:IntervalBuilder[Interval]):Arbitrary[Interval[A]] = {
		Arbitrary ( intervalArbitrary1[A].arbitrary.map( _.toOption.get ) )

	}





}
