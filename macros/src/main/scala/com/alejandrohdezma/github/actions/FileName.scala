package com.alejandrohdezma.github.actions

final case class FileName private (val value: String) extends AnyVal {

  def is(name: FileName): Boolean = value.contentEquals(name.value)

}

object FileName {

  def from(value: String): Either[String, FileName] =
    if (value.matches(regex)) Right(FileName(value))
    else Left(s"not a valid file-name (should match `$regex`)")

  def unsafe(value: String): FileName = from(value).fold(InvalidRefinement.raise, identity)

  implicit def autoRefineFileName(value: String): FileName = macro Macros.fileName

  private val regex = "^[_a-zA-Z0-9\\-]+$"

}
