package com.gottexbrokers.datemath

import org.specs2.Specification
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith
import org.specs2.runner.SpecificationsFinder._

@RunWith(classOf[JUnitRunner])
class TestIndex extends Specification {

  def is =
    examplesLinks("Common Specs")

  def examplesLinks(t: String) = specifications(pattern = ".*Test", basePath = "src/test/scala").foldLeft(t.title) {
    (res, cur) => res ^ link(cur)
  }

}
