package com.alejandrohdezma.github.actions.base

import com.alejandrohdezma.github.actions.macros.Macros
import com.alejandrohdezma.github.actions.yaml._

/** Represents a non-empty string. This type is widely use across the whole library. */
final case class NonEmptyString private (val value: String) extends AnyVal {

  /** For every line in this string:
    *
    *  Strip a leading prefix consisting of blanks or control characters
    *  followed by `|` from the line.
    */
  def stripMargin = NonEmptyString.unsafe(value.stripMargin)

  override def toString(): String = value // scalafix:ok Disable.toString

}

object NonEmptyString {

  implicit val NonEmptyStringEncoder: Encoder[NonEmptyString] = _.value.asYaml

  /** Returns a [[NonEmptyString]] if the provided string is not empty; otherwise, returns a string containing the
    * failure's cause.
    */
  def from(value: String): Either[String, NonEmptyString] =
    if (value.trim().nonEmpty) Right(NonEmptyString(value))
    else Left("empty string")

  /** Unsafely creates a [[NonEmptyString]]. Take into account that this method will throw an exception if the supplied
    * value is empty. If you are not sure if the value is valid, use [[from]] instead.
    *
    * @param value the value to convert to a [[NonEmptyString]]
    * @throws InvalidRefinement if `value` is not valid
    * @return a [[NonEmptyString]] value
    */
  def unsafe(value: String): NonEmptyString = from(value) match {
    case Left(message) => throw InvalidRefinement(message) // scalafix:ok
    case Right(value)  => value
  }

  /** Allows creating [[NonEmptyString]] values from a valid string literal in compile time. */
  implicit def autoRefineNonEmptyString(value: String): NonEmptyString = macro Macros.notEmptyString

}
