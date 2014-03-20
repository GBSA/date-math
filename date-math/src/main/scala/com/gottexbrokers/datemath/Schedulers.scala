package com.gottexbrokers.datemath

import com.gottexbrokers.datemath.scheduler._

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 20/03/14
 * Time: 09:27
 *
 */
object Schedulers {

	private val defaultShortFirstStubScheduler = new ShortStubFirstScheduler {
		override val toString = "Default date-math short stub first scheduler"
	}

	private val defaultShortLastStubScheduler = new ShortStubLastScheduler {
		override val toString = "Default date-math short stub last scheduler"
	}

	private val defaultLongFirstStubScheduler = new LongStubFirstScheduler {
		override val toString = "Default date-math long stub first scheduler"
	}

	private val defaultLongLastStubScheduler = new LongStubLastScheduler {
		override val toString = "Default date-math long stub last scheduler"
	}

	private val defaultNoStubScheduler = new NoStubScheduler {
		override val toString = "Default date-math no stub scheduler"
	}

	private val map = Map[StubType,Scheduler] (
	  StubTypes.SHORT_FIRST -> defaultShortFirstStubScheduler,
		StubTypes.SHORT_LAST -> defaultShortLastStubScheduler,
	  StubTypes.LONG_FIRST -> defaultLongFirstStubScheduler,
	  StubTypes.LONG_LAST -> defaultLongLastStubScheduler,
		StubTypes.NONE -> defaultNoStubScheduler
	)

	def apply(stubType:StubType):Option[Scheduler] = map get stubType

}
