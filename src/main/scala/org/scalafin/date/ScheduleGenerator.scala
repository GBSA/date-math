package org.scalafin.date

import org.joda.time.DateMidnight
import com.github.nscala_time.time.Imports.RichReadableInstant
import scalaz.Validation

/**
 * Utility class for creating an UNADJUSTED schedule between two dates using a
 * given stub type and frequency.
 */
sealed trait ScheduleGenerator {
  def generateSchedule(dateRange: DateRange, frequency: Frequency, frequencyAmount: Int, maxPeriods: Int = Integer.MAX_VALUE, prototype: Option[SimplePeriod] = None): List[SimplePeriod]
}

case object ShortStubFirstScheduleGenerator extends ScheduleGenerator {
  def generateSchedule(dateRange: DateRange, frequency: Frequency, frequencyAmount: Int, maxPeriods: Int = Integer.MAX_VALUE, prototype: Option[SimplePeriod] = None): List[SimplePeriod] = {

    def generateNextDate(holdDate: DateMidnight, count: Int): List[SimplePeriod] = {
      val nextDate = frequency.add(-1 * frequencyAmount * count, dateRange.endDate)
      if (nextDate <= dateRange.startDate) {
        val simplePeriod = for {
          d1 <- dateRange.copyAndModify(newEnd = Some(holdDate))
          d2 <- d1.copyAndModify(newStart = Some(nextDate))
        } yield SimplePeriod(d1, Some(d2))
        simplePeriod.toList
      } else {
        val currentPeriod = for {
          d1 <- dateRange.copyAndModify(Some(nextDate), Some(holdDate))
        } yield SimplePeriod(d1, Some(d1))
        generateNextDate(nextDate, count + 1) ::: currentPeriod.toList
      }
    }

    generateNextDate(dateRange.endDate, 1)

  }
}

case object ShortStubLastScheduleGenerator extends ScheduleGenerator {
  def generateSchedule(dateRange: DateRange, frequency: Frequency, frequencyAmount: Int, maxPeriods: Int = Integer.MAX_VALUE, prototype: Option[SimplePeriod] = None): List[SimplePeriod] = {

    def generateNextDate(holdDate: DateMidnight, count: Int): List[SimplePeriod] = {
      val nextDate = frequency.add(frequencyAmount * count, dateRange.startDate)
      if (nextDate >= dateRange.endDate) {
        val simplePeriod = for {
          d1 <- dateRange.copyAndModify(newStart = Some(holdDate))
          d2 <- d1.copyAndModify(newEnd = Some(nextDate))
        } yield SimplePeriod(d1, Some(d2))
        simplePeriod.toList
      } else {
        val currentPeriod = for {
          d1 <- dateRange.copyAndModify(Some(holdDate), Some(nextDate))
        } yield SimplePeriod(d1, Some(d1))
        currentPeriod.toList ::: generateNextDate(nextDate, count + 1)
      }
    }

    generateNextDate(dateRange.startDate, 1)
  }
}

case object LongStubFirstScheduleGenerator extends ScheduleGenerator {
  def generateSchedule(dateRange: DateRange, frequency: Frequency, frequencyAmount: Int, maxPeriods: Int = Integer.MAX_VALUE, prototype: Option[SimplePeriod] = None): List[SimplePeriod] = {

    def generateNextDate(holdDate: DateMidnight, count: Int): List[SimplePeriod] = {
      val nextDate = frequency.add(-1 * frequencyAmount * count, dateRange.endDate)
      val nextNextDate = frequency.add(-1 * frequencyAmount * (count + 1), dateRange.endDate)
      if (nextNextDate < dateRange.startDate) {
        val simplePeriod = for {
          d1 <- dateRange.copyAndModify(newEnd = Some(holdDate))
          d2 <- d1.copyAndModify(newStart = Some(nextNextDate))
        } yield SimplePeriod(d1, Some(d2))
        simplePeriod.toList
      } else {
        val currentPeriod = for {
          d1 <- dateRange.copyAndModify(Some(nextDate), Some(holdDate))
        } yield SimplePeriod(d1, Some(d1))
        generateNextDate(nextDate, count + 1) ::: currentPeriod.toList
      }
    }
    generateNextDate(dateRange.endDate, 1)
  }
}

case object LongStubLastScheduleGenerator extends ScheduleGenerator {
  def generateSchedule(dateRange: DateRange, frequency: Frequency, frequencyAmount: Int, maxPeriods: Int = Integer.MAX_VALUE, prototype: Option[SimplePeriod] = None): List[SimplePeriod] = {

    def generateNextDate(holdDate: DateMidnight, count: Int): List[SimplePeriod] = {
      val nextDate = frequency.add(frequencyAmount * count, dateRange.startDate)
      val nextNextDate = frequency.add(frequencyAmount * count + 1, dateRange.startDate)
      if (nextNextDate > dateRange.endDate) {
        val simplePeriod = for {
          d1 <- dateRange.copyAndModify(newStart = Some(holdDate))
          d2 <- d1.copyAndModify(newEnd = Some(nextNextDate))
        } yield SimplePeriod(d1, Some(d2))
        simplePeriod.toList
      } else {
        val currentPeriod = for {
          d1 <- dateRange.copyAndModify(Some(holdDate), Some(nextDate))
        } yield SimplePeriod(d1, Some(d1))
        currentPeriod.toList ::: generateNextDate(nextDate, count + 1)
      }
    }
    generateNextDate(dateRange.startDate, 1)
  }
}

case object NoScheduleGenerator extends ScheduleGenerator {
  def generateSchedule(dateRange: DateRange, frequency: Frequency, frequencyAmount: Int, maxPeriods: Int = Integer.MAX_VALUE, prototype: Option[SimplePeriod] = None): List[SimplePeriod] = {

    def generateNextDate(holdDate: DateMidnight, count: Int): List[SimplePeriod] = {
      if (holdDate >= dateRange.endDate) {
        Nil
      } else {
        val nextDate = frequency.add(frequencyAmount * count, dateRange.startDate)
        val currentPeriod = for {
          d1 <- dateRange.copyAndModify(Some(holdDate), Some(nextDate))
        } yield SimplePeriod(d1)
        currentPeriod.toList ::: generateNextDate(nextDate, count + 1)
      }
    }
    generateNextDate(dateRange.startDate, 1)
  }

}

object ScheduleGenerator {
  def forStubType(stubType: StubType): ScheduleGenerator = {
    stubType match {
      case SHORT_FIRST => ShortStubFirstScheduleGenerator
      case SHORT_LAST => ShortStubLastScheduleGenerator
      case LONG_FIRST => LongStubFirstScheduleGenerator
      case LONG_LAST => LongStubLastScheduleGenerator
      case NONE => NoScheduleGenerator
    }
  }

  /**
   * Used to generate a List containing a schedule ordered by period dates.
   * The periods will be adjacent to each other, e.g: d1,d2 d2,d3 d3,d4
   *
   * @param startDate
   * @param endDate
   * @param frequencyAmount
   * @param frequency
   * @param stubType
   * @param maxPeriods
   * @return A List<Period> containing the schedule
   * @throws ScheduleException
   */
  def generateSchedule(dateRange: DateRange, stubType: StubType, frequency: Frequency, frequencyAmount: Int = 1, maxPeriods: Int = Integer.MAX_VALUE, prototype: Option[SimplePeriod] = None): List[SimplePeriod] = {
    val scheduleGenerator = forStubType(stubType)
    scheduleGenerator.generateSchedule(dateRange, frequency, frequencyAmount, maxPeriods, prototype)
  }

}