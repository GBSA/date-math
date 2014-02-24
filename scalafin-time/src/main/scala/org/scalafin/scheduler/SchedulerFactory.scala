package org.scalafin.scheduler

import org.scalafin.datemath.StubType

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 24/02/14
 * Time: 13:34
 *
 */
trait SchedulerFactory {

	def apply(stubType:StubType)

}
