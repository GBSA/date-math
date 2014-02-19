package org.scalafin.datemath.test

import org.specs2.Specification
import scala.collection.GenTraversableOnce
import org.specs2.specification.Fragments

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 19/02/14
 * Time: 13:24
 *
 */
trait FragmentBuildingTools {
  self:Specification =>

	def testChunk[A](traversable: GenTraversableOnce[A], description: String, testOne: A => Fragments): Fragments =
    {
      traversable.foldLeft(p ^ description ^ p) {
                                                  (res, a) =>
                                                    res ^ br ^ testOne(a)
                                                } ^ endp
    }




}
