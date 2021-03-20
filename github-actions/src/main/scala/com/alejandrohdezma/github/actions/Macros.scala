package com.alejandrohdezma.github.actions

import scala.reflect.macros.blackbox

import eu.timepit.refined.W
import eu.timepit.refined.boolean._
import eu.timepit.refined.collection._
import eu.timepit.refined.refineV
import eu.timepit.refined.string._

private[actions] class Macros(val c: blackbox.Context) {
  import c.universe._

  def expInterpolator(args: Any*): c.Expr[Expression] = {
    if (args.nonEmpty)
      c.abort(c.enclosingPosition, "exp interpolator should only be used on string literals")

    c.prefix.tree match {
      case interpolate(s) if isExpression(s) =>
        c.Expr[Expression](q"_root_.com.alejandrohdezma.github.actions.Expression(Refined.unsafeApply($s))")
      case interpolate(_) =>
        c.abort(c.enclosingPosition, f"literals used with this interpolator should not use `$${{ }}`")
      case _ => c.abort(c.enclosingPosition, "should only be used on literals")
    }
  }

  private def isExpression(s: String): Boolean =
    refineV[NonEmpty And Not[StartsWith[W.`"${{"`.T]] And Not[EndsWith[W.`"}}"`.T]]](s).isRight

  private object interpolate {

    def unapply(t: c.Tree): Option[String] =
      t match {
        case Apply(_, List(Apply(_, List(Literal(Constant(s: String)))))) => Some(s)
        case _                                                            => None
      }

  }

}
