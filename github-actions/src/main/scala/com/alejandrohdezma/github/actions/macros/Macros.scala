package com.alejandrohdezma.github.actions.macros

import scala.reflect.ClassTag
import scala.reflect.classTag
import scala.reflect.macros.blackbox

import com.alejandrohdezma.github.actions.base._

private[actions] class Macros(val c: blackbox.Context) {
  import c.universe._

  def notEmptyString(value: c.Expr[String]) =
    value.tree match {
      case Literal(Constant(literal: String)) =>
        NonEmptyString
          .from(literal)
          .fold(
            c.abort(c.enclosingPosition, _),
            _ => reify(NonEmptyString.unsafe(value.splice))
          )
      case Apply(Select(Apply(_, Literals(literals)), _), _) if literals.filter(_.nonEmpty).nonEmpty =>
        reify(NonEmptyString.unsafe(value.splice))
      case _ =>
        c.abort(
          c.enclosingPosition,
          "compile-time refinement only works on String literals or interpolations with non-empty strings"
        )
    }

  object Literals {

    def unapply(list: List[Tree]): Option[List[String]] =
      Some(list.map {
        case Literal(Constant(s: String)) => s
        case a =>
          c.abort(c.enclosingPosition, s"Only string values allowed, found $a")
      })

  }

  def id(value: c.Expr[String]) =
    value.as(Id.from, reify(Id.unsafe(value.splice)))

  def volume(value: c.Expr[String]) =
    value.as(Volume.from, reify(Volume.unsafe(value.splice)))

  def cron(value: c.Expr[String]) =
    value.as(Cron.from, reify(Cron.unsafe(value.splice)))

  def fileName(value: c.Expr[String]) =
    value.as(FileName.from, reify(FileName.unsafe(value.splice)))

  def url(value: c.Expr[String]) =
    value.as(Url.from, reify(Url.unsafe(value.splice)))

  def hour(value: c.Expr[Int]) =
    value.as(Hour.from, reify(Hour.unsafe(value.splice)))

  def portNumber(value: c.Expr[Int]) =
    value.as(PortNumber.from, reify(PortNumber.unsafe(value.splice)))

  implicit private class RefinementOps[A: ClassTag](value: c.Expr[A]) {

    def as[B](validator: A => Either[String, B], constructor: => c.Expr[B]) =
      value.tree match {
        case Literal(Constant(literal: A)) =>
          validator(literal).fold(
            c.abort(c.enclosingPosition, _),
            _ => constructor
          )
        case _ => c.abort(c.enclosingPosition, refinementError[A])
      }

  }

  private def refinementError[A: ClassTag] =
    s"compile-time refinement only works on ${classTag[A].runtimeClass.getSimpleName()} literals"

}
