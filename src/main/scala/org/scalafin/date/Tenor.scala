package org.scalafin.date

import java.text.DecimalFormat
import java.util.Calendar
import scalaz.Validation
import scalaz.Success
import scalaz.Failure

class Tenor(var amount: Int, var calendarUnit: Int)

object Tenor {
  private val spotNodeSynonyms = Array("SN", "S/N", "ON", "O/N")
  private val tomorrowSynonyms = Array("TN", "T/N")
  private val dayFormat = new DecimalFormat("#D")
  private val weekFormat = new DecimalFormat("#W")
  private val monthFormat = new DecimalFormat("#M")
  private val yearFormat = new DecimalFormat("#Y")

  // Use regexp instead?
  def apply(toParse: String): Validation[TenorParseException, Tenor] = {
    // Deal with spot node synonyms
    if (spotNodeSynonyms contains toParse) {
      Success(new Tenor(0, Calendar.DAY_OF_YEAR))
    } else // Deal with tomorrow synonyms
    if (tomorrowSynonyms contains toParse) {
      Success(new Tenor(1, Calendar.DAY_OF_YEAR))
    } else {
      val toParseUpperCase = toParse.toUpperCase
      if (toParse.contains("D")) {
        Success(new Tenor(dayFormat.parse(toParseUpperCase).intValue(), Calendar.DAY_OF_YEAR))
      } else if (toParse.contains("W")) {
        val amount = weekFormat.parse(toParseUpperCase).intValue() * 7
        Success(new Tenor(amount, Calendar.DAY_OF_YEAR))
      } else if (toParse.contains("M")) {
        Success(new Tenor(monthFormat.parse(toParseUpperCase).intValue(), Calendar.MONTH))
      } else if (toParse.contains("Y")) {
        Success(new Tenor(yearFormat.parse(toParseUpperCase).intValue(), Calendar.YEAR))
      } else {
        Failure(new TenorParseException(toParse, "Cannot parse string"))
      }
    }
  }
}

class TenorParseException(val stringToParse: String, val message: String) extends Exception(message)