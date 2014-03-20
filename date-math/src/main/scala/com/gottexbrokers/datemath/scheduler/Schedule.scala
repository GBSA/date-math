package com.gottexbrokers.datemath.scheduler

import com.gottexbrokers.datemath.TimePeriod
import org.joda.time.ReadableDateTime


case class Schedule(periods:Stream[TimePeriod[ReadableDateTime]], start:ReadableDateTime, end:ReadableDateTime)



