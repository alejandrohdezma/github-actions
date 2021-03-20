package com.alejandrohdezma.github.actions

import scala.util.control.NoStackTrace

final case class InvalidRefinement(val message: String) extends NoStackTrace {

  override def getMessage(): String = message

}

object InvalidRefinement {

  def raise(message: String) = throw InvalidRefinement(message) // scalafix:ok

}
