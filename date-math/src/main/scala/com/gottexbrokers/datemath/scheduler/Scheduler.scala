package com.gottexbrokers.datemath.scheduler

import com.gottexbrokers.datemath._
import org.joda.time.{Days, Period, ReadableDateTime}
import scala.annotation.tailrec
import com.gottexbrokers.datemath.utils.OrderingImplicits
import scalaz.{Success, Failure, Validation}
import scalaz.Failure
import scala.Some
import scalaz.Success
import scala.collection.immutable.Stream.cons


/**
 * Created with IntelliJ IDEA.
 * Author: Edmondo Porcu
 * Date: 24/02/14
 * Time: 13:46
 *
 */

trait SchedulerSkeleton extends Scheduler with OrderingImplicits{


	override def schedule(frequency: Frequency, start: ReadableDateTime, end: ReadableDateTime): ScheduleResult  = {
		if( (end compareTo start)<0)
			Failure(new SchedulingImpossibleException(s"Impossible to perform the schedule since $end occurs after the $start"))
		else
			scheduleInternal(frequency,start,end)
	}

	protected def scheduleInternal(frequency: Frequency, start: ReadableDateTime, end: ReadableDateTime):ScheduleResult
}

sealed trait StubDetector {

	def isStubRequiredAt(scheduleStart:ReadableDateTime, scheduleEnd:ReadableDateTime, periodLimitDate:ReadableDateTime):Boolean

}

trait StubFirstDetector extends StubDetector {

	self:BackwardScheduler =>

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

	override protected def scheduleInternal(frequency: Frequency, start: ReadableDateTime, end: ReadableDateTime): ScheduleResult = {
		@tailrec
		def toStream(current:ReadableDateTime, index:Int, previousPeriods:Stream[TimePeriod[ReadableDateTime]]):Stream[TimePeriod[ReadableDateTime]] = {
			// This is necessary because adding 2 months to 30 jan is different to add a month to 30 jan and then add a second month
			val nextDate = frequency.divide(index) subtractFrom end
			if(isStubRequiredAt(start,end,nextDate))
				createStub(start,nextDate,current,previousPeriods)
			else{
				val period = TimePeriod(nextDate, current)
				val newStream = Stream cons (period, previousPeriods)
				if((nextDate compareTo start)==0)
					newStream
				else
				toStream(nextDate, index+1,newStream)
			}
		}
		Success(Schedule(toStream(end,1,Stream.empty[TimePeriod[ReadableDateTime]]),start,end))
	}

	def createStub(start:ReadableDateTime, nextPeriodTheoricExtreme:ReadableDateTime, current:ReadableDateTime,
		previousPeriods:Stream[TimePeriod[ReadableDateTime]]):Stream[TimePeriod[ReadableDateTime]]

}

//TODO: waiting answer on Scala ML

trait ForwardScheduler extends StubbingScheduler{

	self: StubDetector =>

	override protected def scheduleInternal(frequency: Frequency, start: ReadableDateTime, end: ReadableDateTime): ScheduleResult = {

		def toStream(current:ReadableDateTime, currentIndex:Int):Stream[TimePeriod[ReadableDateTime]] = {
			// We need double look ahead
			val nextDate = frequency.divide(currentIndex) addTo start
			val nextNextDate = frequency.divide(currentIndex+1)  addTo start
			if(isStubRequiredAt(start,end,nextNextDate))
				createStub(end,current,nextDate,nextNextDate)
			else{
				val period = TimePeriod(current,nextDate)
				Stream.cons(period, toStream(nextDate,currentIndex+1) )
			}
		}
		Success(Schedule(toStream(start,1),start,end))
	}

	def createStub(end:ReadableDateTime, current:ReadableDateTime, nextDate:ReadableDateTime, nextNextDate:ReadableDateTime):Stream[TimePeriod[ReadableDateTime]]




}

trait ShortStubFirstScheduler extends BackwardScheduler
                                      with StubFirstDetector {


	def createStub(start:ReadableDateTime, nextPeriodTheoricExtreme:ReadableDateTime, current:ReadableDateTime,
		previousPeriods:Stream[TimePeriod[ReadableDateTime]]):Stream[TimePeriod[ReadableDateTime]] =  {
		val period = TimePeriod (start, current)
		Stream cons (period, previousPeriods)

	}

}


trait LongStubFirstScheduler extends BackwardScheduler
                                     with StubFirstDetector {

	def createStub(start:ReadableDateTime, nextPeriodTheoricExtreme:ReadableDateTime, current:ReadableDateTime,
		previousPeriods:Stream[TimePeriod[ReadableDateTime]]):Stream[TimePeriod[ReadableDateTime]] = {

		if(previousPeriods.isEmpty)
			previousPeriods
		else{
			val previousHead = previousPeriods.head
			val period = TimePeriod (start, previousHead.end)
			Stream cons (period, previousPeriods.tail)
		}

	}

}

trait ShortStubLastScheduler extends ForwardScheduler
																			with StubLastDetector {


	override def createStub(end: ReadableDateTime, current: ReadableDateTime, nextDate: ReadableDateTime, nextNextDate: ReadableDateTime): Stream[TimePeriod[ReadableDateTime]] = {
		if((nextDate compareTo end)>0){
			// Extreme degenerate case, 1 period only
			val stubbedPeriod = TimePeriod(current,end)
			Stream cons (stubbedPeriod , Stream.empty)
		}
		else {
			val nonStubbedPeriod = TimePeriod(current,nextDate)
			val stubbedPeriod = TimePeriod(nextDate,end)
			Stream cons (nonStubbedPeriod , Stream cons (stubbedPeriod, Stream.empty[TimePeriod[ReadableDateTime]] ))
		}

	}



}

trait LongStubLastScheduler extends ForwardScheduler
                                    with StubLastDetector {

	override def createStub(end: ReadableDateTime, current: ReadableDateTime, nextDate: ReadableDateTime, nextNextDate: ReadableDateTime): Stream[TimePeriod[ReadableDateTime]] = {
		val stubbedPeriod =TimePeriod(current,end)
		Stream cons (stubbedPeriod, Stream.empty[TimePeriod[ReadableDateTime]] )
	}


}

trait NoStubScheduler extends SchedulerSkeleton {


	private def buildPeriod(periodStart:ReadableDateTime,periodEnd:ReadableDateTime):TimePeriod[ReadableDateTime] = TimePeriod(periodStart,periodEnd)




	 /*
	    There is no way this can be lazy, because the exception will occur at the end. We can try to generates the dates
	    without creating the data structures, but this will need a double traversal

	  */

	override def scheduleInternal(frequency: Frequency, start: ReadableDateTime, end: ReadableDateTime): ScheduleResult = {

		def toList(current:ReadableDateTime,currentIndex:Int, previousItems:List[TimePeriod[ReadableDateTime]]):Validation[SchedulingImpossibleException,List[TimePeriod[ReadableDateTime]]] = {
			// We need double look ahead
			val nextDate = frequency.divide(currentIndex) addTo start
			if((nextDate compareTo end) > 0)
				Failure(new SchedulingImpossibleException(s"The next scheduled date $nextDate is after the $end of the scheduling interval"))
			else {
				if((nextDate compareTo end) == 0){
					Success( previousItems :+ buildPeriod(current,nextDate)  )
				}
				else{
					val newPeriod = buildPeriod(current,nextDate)
					toList(nextDate,currentIndex+1, previousItems :+ newPeriod )
				}
			}


		}
		toList(start, 1, List.empty).map( success => Schedule(success.toStream,start,end))

	}

}

