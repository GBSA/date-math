package org.scalafin.accrual;
//package org.scalafin.date.accrual
//
//import org.specs2.mutable._
//import org.junit.runner.RunWith
//import org.specs2.runner.JUnitRunner
//import org.scalafin.common.Iso4217Currency
//import org.scalafin.common.EUR
//import org.scalafin.date.util.ISDADateFormat
//import org.scalafin.common.GBP
//import org.scalafin.date.accrual.impl.FixedAccrualPeriod
//import org.scalafin.date.UNADJUSTED
//import org.scalafin.date.daycount.DaycountCalculator
//
//@RunWith(classOf[JUnitRunner])
//class AccrualScheduleTest extends Specification {
//  "Payments" should {
//    "succeed with net payments" in {
//      val payments = List(
//        new Payment(100, EUR, ISDADateFormat.parse("2006/01/01")),
//        new Payment(150, EUR, ISDADateFormat.parse("2006/01/01")),
//        new Payment(200, EUR, ISDADateFormat.parse("2006/01/02")),
//        new Payment(300, GBP, ISDADateFormat.parse("2006/01/02")))
//
//      val accrualSchedule = new AccrualSchedule[AccrualPeriod, Payment](null)
//      val nettedPayments = accrualSchedule.netPayments(payments)
//
//      nettedPayments must be size 3
//
//      println("nettedPayments: " + nettedPayments)
//      for (payment <- nettedPayments) {
//        if (payment.currency == EUR && ISDADateFormat.formatFixedLength(payment.paymentDate).equals("2006/01/01")) {
//          payment.amount === 250d
//        } else if (payment.currency == GBP) {
//          payment.amount === 300d
//        } else {
//          payment.amount === 200d
//        }
//      }
//
//    }
//
//    //    "succeed to return payments" in {
//    //      val accrualSchedule = new AccrualSchedule(new Payment(0, null, null))
//    //      val daycountCalculator = new DaycountCalculator()
//    //      
//    //      val accrualPeriod = new FixedAccrualPeriod(
//    //        ISDADateFormat.parse("2006/01/01"),
//    //        ISDADateFormat.parse("2006/04/01"),
//    //        null,
//    //        UNADJUSTED,
//    //        UNADJUSTED,
//    //        null,
//    //        null,
//    //        daycountCalculator,
//    //        0.1d)
//    //    }
//
//  }
//
//}