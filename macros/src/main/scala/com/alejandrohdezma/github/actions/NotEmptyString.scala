package com.alejandrohdezma.github.actions

import com.alejandrohdezma.github.actions.yaml._

final case class NotEmptyString private (val value: String) extends AnyVal {

  def stripMargin = NotEmptyString.unsafe(value.stripMargin)

}

object NotEmptyString {

  implicit val NotEmptyStringEncoder: Encoder[NotEmptyString] = _.value.asYaml

  def from(value: String): Either[String, NotEmptyString] =
    if (value.trim().nonEmpty) Right(NotEmptyString(value))
    else Left("empty string")

  def unsafe(value: String): NotEmptyString = from(value).fold(InvalidRefinement.raise, identity)

  implicit def autoRefineNotEmptyString(value: String): NotEmptyString = macro Macros.notEmptyString

}
