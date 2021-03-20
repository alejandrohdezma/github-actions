package com.alejandrohdezma.github.actions.dsl

import scala.reflect.ClassTag
import scala.reflect.classTag

import com.alejandrohdezma.github.actions.base.Expression
import com.alejandrohdezma.github.actions.base.NotEmptyString
import com.alejandrohdezma.github.actions.events.Event

@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait Expressions {

  /**
   * Returns a expression that evaluates to `true` if the current event is the one provided.
   */
  def is[E <: Event: ClassTag] = {
    val eventName =
      classTag[E].runtimeClass
        .getSimpleName()
        .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
        .replaceAll("([a-z\\d])([A-Z])", "$1_$2")
        .toLowerCase()

    Expression.Operator.Equals(
      Expression.Constant(NotEmptyString.unsafe("github.event_name")),
      Expression.Constant(NotEmptyString.unsafe(eventName))
    )
  }

  /**
   * Returns true when none of the previous steps have failed or been canceled.
   */
  lazy val success = Expression.Constant(NotEmptyString.unsafe("success()"))

  /**
   * Always returns true, even when canceled. A job or step will not run when a critical failure prevents the task from
   * running. For example, if getting sources failed.
   */
  lazy val always = Expression.Constant(NotEmptyString.unsafe("always()"))

  /**
   * Returns true if the workflow was canceled.
   */
  lazy val cancelled = Expression.Constant(NotEmptyString.unsafe("cancelled()"))

  /**
   * Returns true when any previous step of a job fails.
   */
  lazy val failure = Expression.Constant(NotEmptyString.unsafe("failure()"))

}
