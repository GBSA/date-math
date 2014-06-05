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

	case object ShortStubFirstScheduler extends ShortStubFirstScheduler {
		override val toString = "Default date-math short stub first scheduler"
	}

	case object ShortStubLastScheduler extends ShortStubLastScheduler{
		override val toString = "Default date-math short stub last scheduler"
	}

	case object LongStubFirstScheduler extends LongStubFirstScheduler {
		override val toString = "Default date-math long stub first scheduler"
	}

	case object LongStubLastScheduler extends LongStubLastScheduler {
		override val toString = "Default date-math long stub last scheduler"
	}

	case object NoStubScheduler extends NoStubScheduler {
		override val toString = "Default date-math no stub scheduler"
	}

	private val map = Map[StubType,Scheduler] (
	  StubTypes.SHORT_FIRST -> ShortStubFirstScheduler,
		StubTypes.SHORT_LAST -> ShortStubLastScheduler,
	  StubTypes.LONG_FIRST -> LongStubFirstScheduler,
	  StubTypes.LONG_LAST -> LongStubLastScheduler,
		StubTypes.NONE -> NoStubScheduler
	)

	def apply(stubType:StubType):Option[Scheduler] = map get stubType

}
