package com.alejandrohdezma.github.actions.base

import java.net.URI

import scala.util.Try

import com.alejandrohdezma.github.actions.macros.Macros
import com.alejandrohdezma.github.actions.yaml._

final case class Url private (val value: String) extends AnyVal

object Url {

  implicit val UrlEncoder: Encoder[Url] = _.value.asYaml

  def from(value: String): Either[String, Url] =
    if (Try(new URI(value)).isSuccess) Right(Url(value))
    else Left("not a valid URL")

  def unsafe(value: String): Url = from(value).fold(InvalidRefinement.raise, identity)

  implicit def autoRefineUrl(value: String): Url = macro Macros.url

}
