package com.gottexbrokers.datemath.math

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

	implicit def intervalArbitrary1[A](implicit ordering:Ordering[A], startGen:Arbitrary[A], endGen: A => Gen[A],intervalBuilder:IntervalBuilder[Interval]):Arbitrary[Validation[InvalidIntervalException,Interval[A]]] = {
		import Ordering.Implicits._
		Arbitrary[Validation[InvalidIntervalException,Interval[A]]] (
			for {
				start <- Arbitrary.arbitrary[A]
				end <- endGen(start).filter {_ >= start}
			} yield intervalBuilder apply (start,end)

		)
	}

	object IndepedendentExtremesIntervalGenerator {

		implicit  def intervalArbitrary[A](implicit ordering:Ordering[A], gen:Arbitrary[A], intervalBuilder:IntervalBuilder[Interval]):Arbitrary[Interval[A]] = {
			implicit val secondGen = (a:A) => gen.arbitrary
			Arbitrary ( intervalArbitrary1[A].arbitrary.map( _.toOption.get ) )

		}

	}

	object MaxSizedGenerator{

		implicit def maxSizedIntervalArbitrary[A,B](implicit ordering:Ordering[A], genStart:Arbitrary[A], genEnd:Gen[B],
			adder: (A,B) => A , intervalBuilder:IntervalBuilder[Interval]):Arbitrary[Interval[A]] = {

			implicit val secondGen: A => Gen[A] = (a:A) => genEnd.map { b => adder(a,b)   }
			Arbitrary ( intervalArbitrary1[A].arbitrary.map( _.toOption.get ) )

		}

	}


}
