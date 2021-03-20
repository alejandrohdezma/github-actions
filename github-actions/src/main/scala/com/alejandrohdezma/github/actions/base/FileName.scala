package com.alejandrohdezma.github.actions.base

import com.alejandrohdezma.github.actions.macros.Macros

/** Represents a valid workflow filename that can used when providing a workflow to the SBT plugin's `workflows`
  * setting.
  *
  * You can create a filename directly from a valid string (those that matches ^[_a-zA-Z0-9\\-]+$).
  *
  * @example {{{workflowFile("build") { workflow .on(pullRequest) ...}}} }
  */
final case class FileName private (val value: String) extends AnyVal {

  /** Returns `true` if the provided filename matches the current one; otherwise, returns `false`. */
  def is(name: FileName): Boolean = value.contentEquals(name.value)

}

object FileName {

  /** Returns a [[FileName]] if the provided string is a valid filename; otherwise, returns a string containing the
    * failure's cause.
    */
  def from(value: String): Either[String, FileName] =
    if (value.matches(regex)) Right(FileName(value))
    else Left(s"not a valid file-name (should match `$regex`)")

  /** Unsafely creates a [[FileName]]. Take into account that this method will throw an exception if the supplied value
    * is not a valid filename. If you are not sure if the value is valid use [[from]] instead.
    *
    * @param value the value to convert to a [[FileName]]
    * @throws InvalidRefinement if `value` is not valid
    * @return a [[FileName]] value
    */
  def unsafe(value: String): FileName =
    from(value) match {
      case Left(message) => throw InvalidRefinement(message) // scalafix:ok
      case Right(value)  => value
    }

  /** Allows creating [[FileName]] values from a valid string literal in compile time. */
  implicit def autoRefineFileName(value: String): FileName =
    macro Macros.fileName

  private val regex = "^[_a-zA-Z0-9\\-]+$"

}
