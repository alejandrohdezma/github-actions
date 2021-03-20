package com.alejandrohdezma.github.actions.base

import com.alejandrohdezma.github.actions.macros.Macros
import com.alejandrohdezma.github.actions.yaml._

/** Represents a valid port number. It is mainly used to indicate the exposed port numbers in a [[Container]].
  *
  * You can create a port number directly from a valid int (0 <= port <= 65535).
  *
  * @example {{{container.exposePort(8080)}}}
  */
final case class PortNumber private (val value: Int) extends AnyVal

/** Contains constructors and implicit instances for the [[PortNumber]] class. */
object PortNumber {

  /** Allows converting a [[PortNumber]] value into [[yaml.Yaml yaml]]. */
  /** Allows converting a [[PortNumber]] value into [[yaml.Yaml yaml]]. */
  implicit val PortNumberEncoder: Encoder[PortNumber] = _.value.asYaml

  /** Returns a [[PortNumber]] if the provided int is a valid port; otherwise, returns a string containing the failure's
    * cause.
    */
  def from(value: Int): Either[String, PortNumber] =
    if (value >= 0 && value <= 65535) Right(PortNumber(value))
    else Left("not a valid port number (0 <= port <= 65535")

  /** Unsafely creates a [[PortNumber]]. Take into account that this method will throw an exception if the supplied
    * value is not a valid port. If you are not sure if the value is valid use [[from]] instead.
    *
    * @param value The value to convert to a [[PortNumber]].
    * @throws InvalidRefinement If `value` is not valid.
    * @return A [[PortNumber]] value.
    */
  def unsafe(value: Int): PortNumber =
    from(value) match {
      case Left(message) => throw InvalidRefinement(message) // scalafix:ok
      case Right(value)  => value
    }

  /** Allows creating [[PortNumber]] values from a valid int literal in compile time. */
  implicit def autoRefinePortNumber(value: Int): PortNumber =
    macro Macros.portNumber

}
