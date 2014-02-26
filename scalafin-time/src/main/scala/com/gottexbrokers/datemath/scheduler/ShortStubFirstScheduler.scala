package com.gottexbrokers.datemath.scheduler

import com.gottexbrokers.datemath._
import org.joda.time.{Days, Period, ReadableDateTime}
import scala.annotation.tailrec
import com.gottexbrokers.datemath.utils.OrderingImplicits
import scalaz.{Success, Failure, Validation}
import scalaz.Failure
import scala.Some
import scalaz.Success
import com.gottexbrokers.datemath.math.{IntervalBuilder, Interval}

/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 24/02/14
 * Time: 13:46
 *
 */

trait SchedulerSkeleton extends Scheduler with OrderingImplicits{

	def intervalBuilder:IntervalBuilder[Interval]

	def paymentPeriodBuilder:PaymentPeriodBuilder

	override def schedule(frequency: Frequency, start: ReadableDateTime, end: ReadableDateTime): ScheduleResult[ReadableDateTime]  = {
		if( (end compareTo start)<0)
			Failure(new SchedulingImpossibleException(s"Impossible to perform the schedule since $end occurs after the $start"))
		else
			scheduleInternal(frequency,start,end)
	}

	protected def scheduleInternal(frequency: Frequency, start: ReadableDateTime, end: ReadableDateTime):ScheduleResult[ReadableDateTime]
}

sealed trait StubDetector {

	def isStubRequiredAt(scheduleStart:ReadableDateTime, scheduleEnd:ReadableDateTime, periodLimitDate:ReadableDateTime):Boolean

}

trait StubFirstDetector extends StubDetector {

	self:BackwardScheduler =>

	/**
	 *
	 * @param scheduleStart
	 * @param scheduleEnd
	 * @param periodLimitDate
	 * @return
	 */
	override def isStubRequiredAt(scheduleStart: ReadableDateTime, scheduleEnd: ReadableDateTime, periodLimitDate: ReadableDateTime): Boolean = {
		(periodLimitDate compareTo scheduleStart)<0
	}

}

trait StubLastDetector extends StubDetector{

	self:ForwardScheduler =>

	override def isStubRequiredAt(scheduleStart: ReadableDateTime, scheduleEnd: ReadableDateTime, nextDate: ReadableDateTime): Boolean = {
		(nextDate compareTo scheduleEnd)>0
	}

}






sealed trait StubbingScheduler extends SchedulerSkeleton  {

	self: StubDetector =>

}




trait BackwardScheduler extends StubbingScheduler{

	self: StubDetector =>

	override protected def scheduleInternal(frequency: Frequency, start: ReadableDateTime, end: ReadableDateTime): ScheduleResult[ReadableDateTime] = {
		@tailrec
		def toStream(current:ReadableDateTime, previousPeriods:Stream[PaymentPeriod[ReadableDateTime]]):Stream[PaymentPeriod[ReadableDateTime]] = {
			val nextDate = frequency subtractFrom current
			if(isStubRequiredAt(start,end,nextDate))
				createStub(start,nextDate,current,previousPeriods)
			else{
				val newActual = intervalBuilder unsafe (nextDate, current)
				val period = paymentPeriodBuilder build (newActual,None)
				toStream(nextDate, 		Stream cons (period, previousPeriods) )
			}
		}
		Success(Schedule(toStream(end,Stream.empty[PaymentPeriod[ReadableDateTime]])))
	}

	def createStub(start:ReadableDateTime, nextPeriodTheoricExtreme:ReadableDateTime, current:ReadableDateTime,
		previousPeriods:Stream[PaymentPeriod[ReadableDateTime]]):Stream[PaymentPeriod[ReadableDateTime]]

}

//TODO: waiting answer on Scala ML

trait ForwardScheduler extends StubbingScheduler{

	self: StubDetector =>

	override protected def scheduleInternal(frequency: Frequency, start: ReadableDateTime, end: ReadableDateTime): ScheduleResult[ReadableDateTime] = {

		def toStream(current:ReadableDateTime):Stream[PaymentPeriod[ReadableDateTime]] = {
			// We need double look ahead
			val nextDate = frequency addTo current
			val nextNextDate = frequency addTo nextDate
			if(isStubRequiredAt(start,end,nextNextDate))
				createStub(end,current,nextDate,nextNextDate)
			else{
				val newActual = intervalBuilder unsafe (nextDate, current)
				val period = paymentPeriodBuilder build (newActual,None)
				Stream.cons(period, toStream(nextDate) )
			}
		}
		Success(Schedule(toStream(start)))
	}

	def createStub(end:ReadableDateTime, current:ReadableDateTime, nextDate:ReadableDateTime, nextNextDate:ReadableDateTime):Stream[PaymentPeriod[ReadableDateTime]]




}

trait ShortStubFirstScheduler extends BackwardScheduler
                                      with StubFirstDetector {


	def createStub(start:ReadableDateTime, nextPeriodTheoricExtreme:ReadableDateTime, current:ReadableDateTime,
		previousPeriods:Stream[PaymentPeriod[ReadableDateTime]]):Stream[PaymentPeriod[ReadableDateTime]] =  {

		val newActual = intervalBuilder unsafe (start, current)
		val newReference = intervalBuilder unsafe (nextPeriodTheoricExtreme,current)
		val period = paymentPeriodBuilder build (newActual,Some(newReference))
		Stream cons (period, previousPeriods)

	}

}


trait LongStubFirstScheduler extends BackwardScheduler
                                     with StubFirstDetector {

	def createStub(start:ReadableDateTime, nextPeriodTheoricExtreme:ReadableDateTime, current:ReadableDateTime,
		previousPeriods:Stream[PaymentPeriod[ReadableDateTime]]):Stream[PaymentPeriod[ReadableDateTime]] = {

		if(previousPeriods.isEmpty)
			previousPeriods
		else{
			val previousHead = previousPeriods.head
			val reference = previousHead.actual
			val newActual = intervalBuilder unsafe (start, reference.end)
			val period = paymentPeriodBuilder build (newActual,Some(reference))
			Stream cons (period, previousPeriods.tail)
		}

	}

}

trait ShortStubLastScheduler extends ForwardScheduler
																			with StubLastDetector {


	override def createStub(end: ReadableDateTime, current: ReadableDateTime, nextDate: ReadableDateTime, nextNextDate: ReadableDateTime): Stream[PaymentPeriod[ReadableDateTime]] = {
		val nonStubbedPeriod = paymentPeriodBuilder build (intervalBuilder unsafe(current,nextDate), None)
		val stubbedPeriod = paymentPeriodBuilder build (intervalBuilder unsafe(nextDate,end), Some(intervalBuilder unsafe(nextDate,nextNextDate)))
		Stream cons (nonStubbedPeriod , Stream cons (stubbedPeriod, Stream.empty[PaymentPeriod[ReadableDateTime]] ))
	}



}

trait LongStubLastScheduler extends ForwardScheduler
                                    with StubLastDetector {

	override def createStub(end: ReadableDateTime, current: ReadableDateTime, nextDate: ReadableDateTime, nextNextDate: ReadableDateTime): Stream[PaymentPeriod[ReadableDateTime]] = {
		val stubbedPeriod = paymentPeriodBuilder build (intervalBuilder unsafe(current,end), Some(intervalBuilder unsafe(current,nextDate)))
		Stream cons (stubbedPeriod, Stream.empty[PaymentPeriod[ReadableDateTime]] )
	}


}

trait NoStubScheduler extends SchedulerSkeleton {


	private def buildPeriod(periodStart:ReadableDateTime,periodEnd:ReadableDateTime):PaymentPeriod[ReadableDateTime] = {
		paymentPeriodBuilder build ( intervalBuilder unsafe (periodStart,periodEnd), None)
	}



	 /*
	    There is no way this can be lazy, because the exception will occur at the end. We can try to generates the dates
	    without creating the data structures, but this will need a double traversal

	  */

	override def scheduleInternal(frequency: Frequency, start: ReadableDateTime, end: ReadableDateTime): ScheduleResult[ReadableDateTime] = {

		def toList(current:ReadableDateTime,previousItems:List[PaymentPeriod[ReadableDateTime]]):Validation[SchedulingImpossibleException,List[PaymentPeriod[ReadableDateTime]]] = {
			// We need double look ahead
			val nextDate = frequency addTo current
			if((nextDate compareTo end) > 0)
				Failure(new SchedulingImpossibleException(s"The next scheduled date $nextDate is after the $end of the scheduling interval"))
			else {
				if((nextDate compareTo end) == 0){
					Success( buildPeriod(current,nextDate) :: previousItems )
				}
				else{
					val newPeriod = buildPeriod(current,nextDate)
					toList(nextDate, newPeriod :: previousItems)
				}
			}


		}
		toList(start, List.empty).map( success => Schedule(success.toStream))

	}

}