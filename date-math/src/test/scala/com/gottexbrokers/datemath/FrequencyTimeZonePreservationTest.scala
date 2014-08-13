package com.gottexbrokers.datemath

import org.specs2.mutable.Specification
import org.specs2.ScalaCheck
import org.scalacheck.{ Prop, Gen }
import org.specs2.matcher.Parameters
import org.scalacheck.util.Pretty
import java.util.Date
import org.scalacheck.Arbitrary._
import org.joda.time.{ ReadableDateTime, DateTime }
import org.joda.time.tz.FixedDateTimeZone

class FrequencyTimeZonePreservationTest extends Specification with ScalaCheck {

  implicit val parameters = Parameters(minTestsOk = 500)

  override val defaultPrettyParams = Pretty.Params(2)

  val frequencyGen = Gen.oneOf(Frequencies.values.toList)

  val timeZoneGen = for {
    timeZoneId <- arbitrary[String]
    timeZoneName <- arbitrary[String]
    timeZoneOffset <- arbitrary[Int]
    standardOffset <- arbitrary[Int]
  } yield new FixedDateTimeZone(timeZoneId, timeZoneName, timeZoneOffset, standardOffset)

  val dateGen = for {
    javaDateTime <- arbitrary[Date]
    timeZone <- timeZoneGen
  } yield new DateTime(javaDateTime.getTime, timeZone)

  "The frequencies " should {
    "Preserve the timezone" in {
      Prop.forAll(frequencyGen, dateGen) {
        (frequency: Frequency, date: ReadableDateTime) =>
          Prop.collect(frequency) {
            frequency.addTo(date).getZone must_== date.getZone
          }
      }
    }
  }

}
