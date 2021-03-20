package com.alejandrohdezma.github.actions.base

import com.alejandrohdezma.github.actions.macros.Macros
import com.alejandrohdezma.github.actions.yaml._

/** Represents Docker container volume.
  *
  * You can create a volume directly from a valid string.
  *
  * @example {{{container.exposeVolume("/home/alex/mount:/dev/mount")}}}
  */
final case class Volume private (val value: String) extends AnyVal

/** Contains constructors and implicit instances for the [[Volume]] class. */
object Volume {

  /** Allows converting a [[Volume]] value into [[yaml.Yaml yaml]]. */
  /** Allows converting a [[Volume]] value into [[yaml.Yaml yaml]]. */
  implicit val VolumeEncoder: Encoder[Volume] = _.value.asYaml

  /** Returns a [[Volume]] if the provided string is a valid volume match; otherwise, returns a string containing the
    * failure's cause.
    */
  def from(value: String): Either[String, Volume] =
    if (value.matches(regex)) Right(Volume(value))
    else Left(s"not a valid volume (should match `$regex`)")

  /** Unsafely creates a [[Volume]]. Take into account that this method will throw an exception if the supplied value is
    * not a valid volume match. If you are not sure if the value is valid use [[from]] instead.
    *
    * @param value The value to convert to a [[Volume]].
    * @throws InvalidRefinement If `value` is not valid.
    * @return A [[Volume]] value.
    */
  def unsafe(value: String): Volume =
    from(value) match {
      case Left(message) => throw InvalidRefinement(message) // scalafix:ok
      case Right(value)  => value
    }

  /** Allows creating [[Volume]] values from a valid string literal in compile time. */
  implicit def autoRefineVolume(value: String): Volume = macro Macros.volume

  private val regex = "^[^:]+:[^:]+$"

}
