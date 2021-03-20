package com.alejandrohdezma.github.actions.base

import com.alejandrohdezma.github.actions.macros.Macros
import com.alejandrohdezma.github.actions.yaml._

/** Represents a [[Job job]] 's id. It is also use to define [[events.WorkflowDispatch.inputs workflowDispatch's inputs]]
  * names.
  *
  * You can create an id directly from a valid string (those that matches ^[_a-zA-Z][a-zA-Z0-9_-]*$).
  *
  * @example {{{job("build") { ...}}} }
  */
final case class Id private (val value: String) extends AnyVal {

  /** Returns `true` if the provided id matches the current one; otherwise, returns `false`. */
  def is(id: Id): Boolean = value.contentEquals(id.value)

}

/** Contains constructors and implicit instances for the [[Id]] class. */
object Id {

  /** Allows converting an [[Id]] value into [[yaml.Yaml yaml]]. */
  /** Allows converting a [[Id]] value into [[yaml.Yaml yaml]]. */
  implicit val IdEncoder: Encoder[Id] = _.value.asYaml

  /** Returns an [[Id]] if the provided string is a valid id; otherwise, returns a string containing the failure's
    * cause.
    */
  def from(value: String): Either[String, Id] =
    if (value.matches(regex)) Right(Id(value))
    else Left(s"not a valid id (should match `$regex`)")

  /** Unsafely creates an [[Id]]. Take into account that this method will throw an exception if the supplied value is
    * not a valid id. If you are not sure if the value is valid use [[from]] instead.
    *
    * @param value The value to convert to an [[Id]].
    * @throws InvalidRefinement If `value` is not valid.
    * @return An [[Id]] value.
    */
  def unsafe(value: String): Id =
    from(value) match {
      case Left(message) => throw InvalidRefinement(message) // scalafix:ok
      case Right(value)  => value
    }

  /** Allows creating [[Id]] values from a valid string literal in compile time. */
  implicit def autoRefineId(value: String): Id = macro Macros.id

  private val regex = "^[_a-zA-Z][a-zA-Z0-9_-]*$"

}
