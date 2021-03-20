package com.alejandrohdezma.github.actions

import com.alejandrohdezma.github.actions.yaml._
final case class Volume private (val value: String) extends AnyVal

object Volume {

  implicit val VolumeEncoder: Encoder[Volume] = _.value.asYaml

  def from(value: String): Either[String, Volume] =
    if (value.matches(regex)) Right(Volume(value))
    else Left(s"not a valid volume (should match `$regex`)")

  def unsafe(value: String): Volume = from(value).fold(InvalidRefinement.raise, identity)

  implicit def autoRefineVolume(value: String): Volume = macro Macros.volume

  private val regex = "^[^:]+:[^:]+$"

}
