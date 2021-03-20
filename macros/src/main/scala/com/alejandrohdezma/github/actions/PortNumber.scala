package com.alejandrohdezma.github.actions

import com.alejandrohdezma.github.actions.yaml._
final case class PortNumber private (val value: Int) extends AnyVal

object PortNumber {

  implicit val PortNumberEncoder: Encoder[PortNumber] = _.value.asYaml

  def from(value: Int): Either[String, PortNumber] =
    if (value >= 0 && value <= 65535) Right(PortNumber(value))
    else Left("not a valid port number (0 <= port <= 65535")

  def unsafe(value: Int): PortNumber = from(value).fold(InvalidRefinement.raise, identity)

  implicit def autoRefinePortNumber(value: Int): PortNumber = macro Macros.portNumber

}
