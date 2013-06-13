package org.scalafin.convention

/**
 * <p>
 * Enumeration of the various business day conventions:
 * </p>
 *
 * <ul>
 * <li> Unadjusted
 * <li> Preceding
 * <li> Modified Preceding
 * <li> Following
 * <li> Modified Following
 * <li> Month End Reference
 * </ul>
 */
sealed abstract class BusinessDayConvention

/**
 * Do not adjust.
 */
case object UNADJUSTED extends BusinessDayConvention

/**
 * Choose the first business day before the given holiday.
 */
case object PRECEDING extends BusinessDayConvention

/**
 * Choose the first business day before the given holiday unless it belongs
 * to a different month, in which case choose the first business day after
 * the holiday.
 */
case object MODIFIED_PRECEDING extends BusinessDayConvention

/**
 * Choose the first business day after the given holiday.
 */
case object FOLLOWING extends BusinessDayConvention

/**
 * Choose the first business day after the given holiday unless it belongs
 * to a different month, in which case choose the first business day before
 * the holiday.
 */
case object MODIFIED_FOLLOWING extends BusinessDayConvention

/**
 * Choose the first business day after the given holiday, if the original
 * date falls on last business day of month result reverts to first business
 * day before month-end
 */
case object MONTH_END_REFERENCE extends BusinessDayConvention
