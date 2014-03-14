package com.gottexbrokers

import org.joda.time._
import scalaz.Validation
import com.gottexbrokers.datemath.scheduler.Schedule


/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 12/02/14
 * Time: 11:27
 *
 */
package object datemath {

  type DateInterval = Period[ReadableDateTime]

	type ScheduleResult[T] = Validation[SchedulingImpossibleException, Schedule[ReadableDateTime]]


}

