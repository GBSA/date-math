package com.gottexbrokers.datemath.math

import org.scalacheck.{Arbitrary, Gen}
import scalaz.Validation
import com.gottexbrokers.datemath.Period

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 20/02/14
 * Time: 10:47
 *
 */
trait PeriodGenerators {

	implicit def intervalArbitrary1[A](implicit ordering:Ordering[A], startGen:Arbitrary[A], endGen: A => Gen[A]):Arbitrary[Period[A]] = {
		import Ordering.Implicits._
		Arbitrary[Period[A]](
			for {
				start1 <- Arbitrary.arbitrary[A]
				end1 <- endGen(start1).filter {_ >= start1}
			} yield new Period[A]{

					override val end: A = end1

					override val start: A = start1

					override val toString = s"Period starting at $start1 and finishing at $end1"
				}

		)
	}

	object IndepedendentExtremesIntervalGenerator {

		implicit  def intervalArbitrary[A](implicit ordering:Ordering[A], gen:Arbitrary[A]):Arbitrary[Period[A]] = {
			implicit val secondGen = (a:A) => gen.arbitrary
			Arbitrary ( intervalArbitrary1[A].arbitrary )

		}

	}

	object MaxSizedGenerator{

		implicit def maxSizedIntervalArbitrary[A,B](implicit ordering:Ordering[A], genStart:Arbitrary[A], genEnd:Gen[B],
			adder: (A,B) => A):Arbitrary[Period[A]] = {
			implicit val secondGen: A => Gen[A] = (a:A) => genEnd.map { b => adder(a,b)   }
			Arbitrary ( intervalArbitrary1[A].arbitrary )

		}

	}


}
