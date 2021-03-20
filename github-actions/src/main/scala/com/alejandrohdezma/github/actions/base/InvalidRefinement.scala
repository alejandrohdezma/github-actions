package com.alejandrohdezma.github.actions.base

import scala.util.control.NoStackTrace

/** This exception will be thrown by the different `unsafe` methods (such as [[Cron.unsafe]] ) when invoked with an
  * invalid value.
  */
final case class InvalidRefinement(val message: String) extends NoStackTrace {

  override def getMessage(): String = message

}
