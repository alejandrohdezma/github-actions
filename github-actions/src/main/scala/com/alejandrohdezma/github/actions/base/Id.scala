package com.alejandrohdezma.github.actions.base

import com.alejandrohdezma.github.actions.macros.Macros
import com.alejandrohdezma.github.actions.yaml._

final case class Id private (val value: String) extends AnyVal {

  def is(id: Id): Boolean = value.contentEquals(id.value)

}

object Id {

  implicit val IdEncoder: Encoder[Id] = _.value.asYaml

  def from(value: String): Either[String, Id] =
    if (value.matches(regex)) Right(Id(value))
    else Left(s"not a valid id (should match `$regex`)")

  def unsafe(value: String): Id = from(value).fold(InvalidRefinement.raise, identity)

  implicit def autoRefineId(value: String): Id = macro Macros.id

  private val regex = "^[_a-zA-Z][a-zA-Z0-9_-]*$"

}
