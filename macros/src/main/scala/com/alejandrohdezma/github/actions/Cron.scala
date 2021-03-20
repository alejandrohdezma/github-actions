package com.alejandrohdezma.github.actions

import com.alejandrohdezma.github.actions.yaml._
/**
 * @see [[https://stackoverflow.com/a/57639657/4044345]]
 */
final case class Cron private (val value: String) extends AnyVal

object Cron {

  implicit val CronEncoder: Encoder[Cron] = _.value.asYaml

  def from(value: String): Either[String, Cron] =
    if (value.matches(regex)) Right(Cron(value))
    else Left(s"not a valid cron (should match `$regex`)")

  def unsafe(value: String): Cron = from(value).fold(InvalidRefinement.raise, identity)

  implicit def autoRefineCron(value: String): Cron = macro Macros.cron

  private val regex =
    """^(((\d+,)+\d+|((\d+|\*)\/\d+|JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)|(\d+-\d+)|\d+|\*|MON|TUE|WED|THU|FRI|SAT|SUN) ?){5,7}$"""

}
