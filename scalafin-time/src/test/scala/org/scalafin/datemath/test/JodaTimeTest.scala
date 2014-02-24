package org.scalafin.datemath.test

import org.specs2.{ScalaCheck, Specification}
import org.specs2.matcher.MatchResult
import org.joda.time.LocalDate
import org.scalacheck.Prop
import org.specs2.specification.Example

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 24/02/14
 * Time: 13:15
 *
 */
class JodaTimeTest extends Specification with ScalaCheck with LongToJodaTimeConversions with LongGeneratorWithNoOverflow
{



	def testMillis(long:Long):MatchResult[Any] = {
		val localDate = new LocalDate(long)
		val midnight = localDate.toDateTimeAtStartOfDay
		val minute = midnight.getMinuteOfHour
		val second = midnight.getSecondOfMinute
		val millisecond = midnight.getMillisOfSecond
		(minute must_==0) and (second must_==0) and (millisecond must_==0)

	}

	def is = e1^ p ^ e2

	def e1:Example = "A known case on JodaTime would success" ! testMillis(-2422054408000L)

	def e2:Example = "Random generated data with ScalaCheck would success" ! Prop.forAll(
		(long:Long) => testMillis(long)
	)

}
