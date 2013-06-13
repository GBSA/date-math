package org.scalafin.accrual.impl


sealed abstract class FixingAdjustmentType
case object CALENDAR_DAYS extends FixingAdjustmentType
case object BUSINESS_DAYS extends FixingAdjustmentType