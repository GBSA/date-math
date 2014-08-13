package com.gottexbrokers

import org.joda.time._
import scalaz.Validation
import com.gottexbrokers.datemath.scheduler.Schedule

package object datemath {

  type DateInterval = Period[ReadableDateTime]

  type ScheduleResult = Validation[SchedulingImpossibleException, Schedule]

}

