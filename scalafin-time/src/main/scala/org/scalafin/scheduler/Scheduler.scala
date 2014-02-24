package org.scalafin.scheduler

import org.joda.time.{ReadableDateTime, DateTime, ReadableInstant}
import org.scalafin.datemath.Frequency

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 24/02/14
 * Time: 13:34
 *
 */
trait Scheduler {

	def schedule(frequency:Frequency, start: ReadableDateTime, end: ReadableDateTime):Schedule[ReadableDateTime]

}
