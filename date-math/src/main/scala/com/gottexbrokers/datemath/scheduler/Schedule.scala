package com.gottexbrokers.datemath.scheduler

import com.gottexbrokers.datemath.PaymentPeriod


case class Schedule[A](periods:Stream[PaymentPeriod[A]], start:A, end:A)



