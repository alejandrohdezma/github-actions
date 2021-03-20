package com.alejandrohdezma.github.actions

import scala.reflect.ClassTag
import scala.reflect.classTag
import scala.reflect.macros.blackbox

private[actions] class Macros(val c: blackbox.Context) {
  import c.universe._

  def notEmptyString(value: c.Expr[String]) = value.as(NotEmptyString.from, reify(NotEmptyString.unsafe(value.splice)))
  def id(value: c.Expr[String])             = value.as(Id.from, reify(Id.unsafe(value.splice)))
  def volume(value: c.Expr[String])         = value.as(Volume.from, reify(Volume.unsafe(value.splice)))
  def envName(value: c.Expr[String])        = value.as(EnvName.from, reify(EnvName.unsafe(value.splice)))
  def cron(value: c.Expr[String])           = value.as(Cron.from, reify(Cron.unsafe(value.splice)))
  def fileName(value: c.Expr[String])       = value.as(FileName.from, reify(FileName.unsafe(value.splice)))
  def url(value: c.Expr[String])            = value.as(Url.from, reify(Url.unsafe(value.splice)))
  def hour(value: c.Expr[Int])              = value.as(Hour.from, reify(Hour.unsafe(value.splice)))
  def portNumber(value: c.Expr[Int])        = value.as(PortNumber.from, reify(PortNumber.unsafe(value.splice)))

  def expInterpolator(args: Any*): c.Expr[Expression] = {
    if (args.nonEmpty)
      c.abort(c.enclosingPosition, "exp interpolator should only be used on string literals")

    c.prefix.tree match {
      case interpolate(s) if isExpression(s) =>
        c.Expr[Expression](q"_root_.com.alejandrohdezma.github.actions.Expression.unsafe($s)")
      case interpolate(_) => c.abort(c.enclosingPosition, f"should not use `$${{ }}`")
      case _              => c.abort(c.enclosingPosition, "should only be used on String literals")
    }
  }

  implicit private class RefinementOps[A: ClassTag](value: c.Expr[A]) {

    def as[B](validator: A => Either[String, B], constructor: => c.Expr[B]) =
      value.tree match {
        case Literal(Constant(literal: A)) =>
          validator(literal).fold(c.abort(c.enclosingPosition, _), _ => constructor)
        case _ => c.abort(c.enclosingPosition, refinementError[A])
      }

  }

  private def isExpression(s: String): Boolean = !s.startsWith("${{") && s.trim.nonEmpty && !s.endsWith("}}")

  private object interpolate {

    def unapply(t: c.Tree): Option[String] =
      t match {
        case Apply(_, List(Apply(_, List(Literal(Constant(s: String)))))) => Some(s)
        case _                                                            => None
      }

  }

  private def refinementError[A: ClassTag] =
    s"compile-time refinement only works on ${classTag[A].runtimeClass.getSimpleName()} literals"

}
