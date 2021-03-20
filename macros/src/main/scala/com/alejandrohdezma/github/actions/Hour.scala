package com.alejandrohdezma.github.actions

import com.alejandrohdezma.github.actions.yaml._
final case class Hour private (val value: Int) extends AnyVal

object Hour {

  implicit val HourEncoder: Encoder[Hour] = _.value.asYaml

  def from(value: Int): Either[String, Hour] =
    if (value >= 0 && value <= 23) Right(Hour(value))
    else Left("not a valid hour (0 <= hour <= 23")

  def unsafe(value: Int): Hour = from(value).fold(InvalidRefinement.raise, identity)

  implicit def autoRefineHour(value: Int): Hour = macro Macros.hour

}
