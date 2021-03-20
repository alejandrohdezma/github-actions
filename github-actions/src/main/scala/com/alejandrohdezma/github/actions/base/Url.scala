package com.alejandrohdezma.github.actions.base

import java.net.URI

import scala.util.Try

import com.alejandrohdezma.github.actions.macros.Macros
import com.alejandrohdezma.github.actions.yaml._

/** Represents a valid URL. It is mainly used to indicate an [[Environment.url]].
  *
  * You can create a url directly from a valid string.
  *
  * @example {{{job.environment("staging", "https://example.com")}}}
  */
final case class Url private (val value: String) extends AnyVal

object Url {

  implicit val UrlEncoder: Encoder[Url] = _.value.asYaml

  /** Returns a [[Url]] if the provided string is a valid URL; otherwise, returns a string containing the failure's
    * cause.
    */
  def from(value: String): Either[String, Url] =
    if (Try(new URI(value)).isSuccess) Right(Url(value))
    else Left("not a valid URL")

  /** Unsafely creates a [[Url]]. Take into account that this method will throw an exception if the supplied value is
    * not a valid URL. If you are not sure if the value is valid use [[from]] instead.
    *
    * @param value the value to convert to an [[Url]]
    * @throws InvalidRefinement if `value` is not valid
    * @return an [[Url]] value
    */
  def unsafe(value: String): Url =
    from(value) match {
      case Left(message) => throw InvalidRefinement(message) // scalafix:ok
      case Right(value)  => value
    }

  /** Allows creating [[Url]] values from a valid string literal in compile time. */
  implicit def autoRefineUrl(value: String): Url = macro Macros.url

}
