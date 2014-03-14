package com.gottexbrokers.datemath.scheduler

import com.gottexbrokers.datemath.TimePeriod
import org.joda.time.ReadableDateTime


case class Schedule[A<:ReadableDateTime](periods:Stream[TimePeriod[A]], start:A, end:A)



