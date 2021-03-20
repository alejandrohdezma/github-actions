package com.alejandrohdezma.github.actions.base

import com.alejandrohdezma.github.actions.macros.Macros
import com.alejandrohdezma.github.actions.yaml._

final case class EnvName private (val value: String) extends AnyVal

object EnvName {

  implicit val EnvNameEncoder: Encoder[EnvName] = _.value.asYaml

  def from(value: String): Either[String, EnvName] =
    if (value.matches(regex)) Right(EnvName(value))
    else Left(s"not a valid environment variable name (should match `$regex`)")

  def unsafe(value: String): EnvName = from(value).fold(InvalidRefinement.raise, identity)

  implicit def autoRefineEnvName(value: String): EnvName = macro Macros.envName

  private val regex = "^[a-zA-Z_][a-zA-Z0-9_]*$"

}
