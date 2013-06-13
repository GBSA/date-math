package org.scalafin.convention

/**
 * <p>
 * Enumeration of the various stub types, currently supports:
 * </p>
 *
 * <ul>
 * <li> Undajusted
 * <li> Preceding
 * <li> Modified Preceding
 * <li> Following
 * <li> Modified Following
 * <li> Month End Reference
 * </ul>
 * 
 * @author jeremy.nguyenxuan
 *
 */
sealed abstract class StubType(val longStub: Boolean, val shortStub: Boolean)

/**
 * The short stub period is at the start of the schedule, e.g.:
 *
 * X.X...X...X...X...X...X...X
 */
case object SHORT_FIRST extends StubType(false, true)

/**
 * The short stub period is at the end of the schedule
 *
 * X...X...X...X...X...X...X.X
 */
case object SHORT_LAST extends StubType(false, true)

/**
 * The long stub period is at the start of the schedule, e.g.:
 *
 * X.....X...X...X...X...X...X
 */
case object LONG_FIRST extends StubType(true, false)

/**
 * The long stub period is at the end of the schedule
 *
 * X...X...X...X...X...X.....X
 */
case object LONG_LAST extends StubType(true, false)

/**
 * There should be no stub period, if the frequency doesn't exactly fit the
 * start/end date then an exception will be thrown
 *
 * X...X...X...X...X...X...X
 */
case object NONE extends StubType(false, false)