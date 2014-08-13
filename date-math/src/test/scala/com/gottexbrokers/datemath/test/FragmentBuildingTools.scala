package com.gottexbrokers.datemath.test

import org.specs2.Specification
import org.specs2.specification.Fragments
import scala.collection.GenTraversableOnce

trait FragmentBuildingTools {
  self: Specification =>

  def testChunk[A](traversable: GenTraversableOnce[A], description: String, testOne: A => Fragments): Fragments =
    {
      traversable.foldLeft(p ^ description ^ p) {
        (res, a) =>
          res ^ br ^ testOne(a)
      } ^ endp
    }

}
