package org.scalafin.accrual

import scalaz.Failure
import scalaz.Success
import scala.collection.mutable.ArrayBuffer
import org.joda.time.DateMidnight
import org.scalafin.convention.Iso4217Currency
import org.scalafin.date.DatePeriodSplitter._
import org.scalafin.date.DatePeriodSplitter
import org.scalafin.scheduler.Schedule

// FIXME review this class
class AccrualSchedule[T <: AccrualPeriod, U <: Payment](paymentPrototype: U)(implicit datePeriodSplitter: DatePeriodSplitter[T]) {
  var accrualPeriods: List[T] = List()

  def getNettedPayments(notionalSchedule: NotionalSchedule): List[U] = netPayments(getPayments(notionalSchedule))

  def netPayments(payments: List[U]): List[U] = {

    def netPayments_(remainingList: List[U], alreadyProcessed: List[(DateMidnight, Iso4217Currency)]): List[U] = {
      remainingList match {
        case x :: xs => {
          if (alreadyProcessed.contains((x.paymentDate, x.currency))) {
            netPayments_(xs, alreadyProcessed)
          } else {
            val a = x.addSimilarPaymentsTogether(xs)
            if (a._1) {
              x.copy(amount = a._2).asInstanceOf[U] :: netPayments_(xs, (x.paymentDate, x.currency) :: alreadyProcessed)
            } else {
              x :: netPayments_(xs, alreadyProcessed)
            }
          }
        }

        case _ => Nil
      }
    }

    netPayments_(payments, List[(DateMidnight, Iso4217Currency)]())
  }

  // FIXME not so Scala-style...
  def getPayments(notionalSchedule: NotionalSchedule): List[U] = {
    val dateRange = accrualPeriods.apply(0).dateRange.copyAndModify(newEnd = Some(getEndDate))

    val notionalPeriods = dateRange map (notionalSchedule.getNotionalPeriodsBetween(_))

    val accrualScheduleCutter = Schedule(accrualPeriods)
    val notionalScheduleCutter = notionalPeriods flatMap (Schedule(_))

    val accrualScheduleCut = for {
      accrual <- accrualScheduleCutter
      np <- notionalPeriods
    } yield accrual.cutSchedule(np)

    val notionalScheduleCut = for {
      notional <- notionalScheduleCutter
      np <- notionalPeriods
    } yield notional.cutSchedule(accrualPeriods)

    if (accrualScheduleCut.map(_.periods) != notionalScheduleCut.map(_.periods)) {
      // FIXME error return validation?
      null
    } else {
      val arrbuf = new ArrayBuffer[U]()
      for (i <- 0 until accrualScheduleCut.toOption.get.periods.length) {
        val acc = accrualScheduleCut.toOption.get.periods.apply(i)
        val not = notionalScheduleCut.toOption.get.periods.apply(i)
        val paymentAmount = acc.paymentAmount(not.amount)
        arrbuf += (new Payment(paymentAmount, not.currency, acc.paymentDate)).asInstanceOf[U]
      }
      arrbuf.toList
    }
  }

  def getStartDate = accrualPeriods.apply(0).dateRange.startDate
  def getEndDate = accrualPeriods.apply(accrualPeriods.length - 1).dateRange.endDate
}
