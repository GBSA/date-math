package org.scalafin.accrual

sealed abstract class PaymentType
/**
 * Payment IN_ARREARS is where the payment occurs at the end of the
 * period
 */
case object IN_ARREARS extends PaymentType

/**
 * Payment IN_ADVANCE is where the payment occurs at the start of
 * the period
 */
case object IN_ADVANCE extends PaymentType