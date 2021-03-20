package com.alejandrohdezma.github.actions.base

import com.alejandrohdezma.github.actions.macros.Macros
import com.alejandrohdezma.github.actions.yaml._

/** Represents a valid string cron expression that can used when creating a [[events.Schedule schedule]] event.
  *
  * You can create a cron directly from a valid cron string, or you can use one of the [[dsl.Crons predefined ones]].
  *
  * @example {{{ workflow.on(schedule(yearly, everydayAt(7), "* * * 0 *")) }}}
  *
  * @see [[https://stackoverflow.com/a/57639657/4044345]]
  * @see [[https://docs.github.com/en/actions/reference/events-that-trigger-workflows#schedule]]
  */
final case class Cron private (val value: String) extends AnyVal

/** Contains constructors and implicit instances for the [[Cron]] class. */
object Cron {

  /** Allows converting a [[Cron]] value into [[yaml.Yaml yaml]]. */
  /** Allows converting a [[Cron]] value into [[yaml.Yaml yaml]]. */
  implicit val CronEncoder: Encoder[Cron] = _.value.asYaml

  /** Returns a [[Cron]] if the provided string is a valid cron value; otherwise, returns a string containing the
    * failure's cause.
    */
  def from(value: String): Either[String, Cron] =
    if (value.matches(regex)) Right(Cron(value))
    else Left(s"not a valid cron (should match `$regex`)")

  /** Unsafely creates a [[Cron]]. Take into account that this method will throw an exception if the supplied value is
    * not a valid cron. If you are not sure if the value is valid use [[from]] instead.
    *
    * @param value The value to convert to a [[Cron]]
    * @throws InvalidRefinement If `value` is not valid.
    * @return A [[Cron]] value.
    */
  def unsafe(value: String): Cron =
    from(value) match {
      case Left(message) => throw InvalidRefinement(message) // scalafix:ok
      case Right(value)  => value
    }

  /** Allows creating [[Cron]] values from valid string literals in compile time. */
  implicit def autoRefineCron(value: String): Cron = macro Macros.cron

  private val regex =
    """^(((\d+,)+\d+|((\d+|\*)\/\d+|JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)|(\d+-\d+)|\d+|\*|MON|TUE|WED|THU|FRI|SAT|SUN) ?){5,7}$"""

}
