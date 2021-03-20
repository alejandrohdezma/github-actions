package com.alejandrohdezma.github.actions

import com.alejandrohdezma.github.actions.yaml._

sealed trait Expression {

  def show(): NotEmptyString = NotEmptyString.unsafe("${{ " + showR() + " }}")

  private def showR(parentheses: Boolean = false /* scalafix:ok */ ): String = this match {
    case Expression.Constant(value)                          => value.value
    case Expression.Operator.Equals(left, right)             => s"${left.showR()} == ${right.showR()}"
    case Expression.Operator.NotEquals(left, right)          => s"${left.showR()} != ${right.showR()}"
    case Expression.Operator.Not(Expression.Constant(value)) => s"!${value.value}"
    case Expression.Operator.Not(operator)                   => s"!(${operator.showR()})"
    case Expression.Operator.Greater(left, right)            => s"${left.showR()} > ${right.showR()}"
    case Expression.Operator.Less(left, right)               => s"${left.showR()} < ${right.showR()}"
    case Expression.Operator.GreaterEqual(left, right)       => s"${left.showR()} >= ${right.showR()}"
    case Expression.Operator.LessEqual(left, right)          => s"${left.showR()} <= ${right.showR()}"
    case Expression.Operator.And(left, right) if parentheses => s"(${left.showR()} && ${right.showR()})"
    case Expression.Operator.And(left, right)                => s"${left.showR()} && ${right.showR()}"
    case Expression.Operator.Or(left, right) if parentheses  => s"(${left.showR(true)} || ${right.showR(true)})"
    case Expression.Operator.Or(left, right)                 => s"${left.showR(true)} || ${right.showR(true)}"
  }

  def ===(other: Expression): Expression      = Expression.Operator.Equals(this, other)
  def ===(string: NotEmptyString): Expression = Expression.Operator.Equals(this, Expression.Constant(string))

  def =!=(other: Expression): Expression      = Expression.Operator.NotEquals(this, other)
  def =!=(string: NotEmptyString): Expression = Expression.Operator.NotEquals(this, Expression.Constant(string))

  def ==(other: Expression): Expression      = Expression.Operator.Equals(this, other)
  def ==(string: NotEmptyString): Expression = Expression.Operator.Equals(this, Expression.Constant(string))

  def !=(other: Expression): Expression      = Expression.Operator.NotEquals(this, other)
  def !=(string: NotEmptyString): Expression = Expression.Operator.NotEquals(this, Expression.Constant(string))

  def <(other: Expression): Expression      = Expression.Operator.Less(this, other)
  def <(string: NotEmptyString): Expression = Expression.Operator.Less(this, Expression.Constant(string))

  def >(other: Expression): Expression      = Expression.Operator.Greater(this, other)
  def >(string: NotEmptyString): Expression = Expression.Operator.Greater(this, Expression.Constant(string))

  def <=(other: Expression): Expression      = Expression.Operator.LessEqual(this, other)
  def <=(string: NotEmptyString): Expression = Expression.Operator.LessEqual(this, Expression.Constant(string))

  def >=(other: Expression): Expression      = Expression.Operator.GreaterEqual(this, other)
  def >=(string: NotEmptyString): Expression = Expression.Operator.GreaterEqual(this, Expression.Constant(string))

  def not(): Expression = Expression.Operator.Not(this)

  def ||(other: Expression) = Expression.Operator.Or(this, other)

  def &&(other: Expression) = Expression.Operator.And(this, other)

}

object Expression {

  final case class Constant private (private val value: NotEmptyString) extends Expression

  sealed trait Operator extends Expression

  object Operator {

    final case class Equals(left: Expression, right: Expression)       extends Operator
    final case class NotEquals(left: Expression, right: Expression)    extends Operator
    final case class Not(expression: Expression)                       extends Operator
    final case class Greater(left: Expression, right: Expression)      extends Operator
    final case class Less(left: Expression, right: Expression)         extends Operator
    final case class GreaterEqual(left: Expression, right: Expression) extends Operator
    final case class LessEqual(left: Expression, right: Expression)    extends Operator
    final case class And(left: Expression, right: Expression)          extends Operator
    final case class Or(left: Expression, right: Expression)           extends Operator

  }

  implicit val ExpressionEncoder: Encoder[Expression] = _.show().asYaml

  def unsafe(value: String): Expression = Expression.Constant(NotEmptyString.unsafe(value))

}
