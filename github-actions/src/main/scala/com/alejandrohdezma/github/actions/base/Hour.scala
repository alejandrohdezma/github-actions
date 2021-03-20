package com.alejandrohdezma.github.actions.base

import com.alejandrohdezma.github.actions.macros.Macros
import com.alejandrohdezma.github.actions.yaml._

/** Represents an hour of the day that can used when creating [[Cron crons]] using [[dsl.Crons.everydayAt everydayAt]].
  *
  * You can create an hour directly from a valid int (0 <= hour <= 23).
  *
  * @example {{{schedule(everydayAt(7))}}}
  */
final case class Hour private (val value: Int) extends AnyVal

object Hour {

  implicit val HourEncoder: Encoder[Hour] = _.value.asYaml

  /** Returns an [[Hour]] if the provided int is a valid hour; otherwise, returns a string containing the
    * failure'scause.
    */
  def from(value: Int): Either[String, Hour] =
    if (value >= 0 && value <= 23) Right(Hour(value))
    else Left("not a valid hour (0 <= hour <= 23")

  /** Unsafely creates an [[Hour]]. Take into account that this method will throw an exception if the supplied value is
    * not a valid hour. If you are not sure if the value is valid use [[from]] instead.
    *
    * @param value the value to convert to an [[Hour]]
    * @throws InvalidRefinement if `value` is not valid
    * @return an [[Hour]] value
    */
  def unsafe(value: Int): Hour =
    from(value) match {
      case Left(message) => throw InvalidRefinement(message) // scalafix:ok
      case Right(value)  => value
    }

  /** Allows creating [[Hour]] values from a valid int literal in compile time. */
  implicit def autoRefineHour(value: Int): Hour = macro Macros.hour

}
