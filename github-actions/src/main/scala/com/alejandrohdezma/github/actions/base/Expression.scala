package com.alejandrohdezma.github.actions.base

import scala.language.dynamics

import com.alejandrohdezma.github.actions.yaml._

/** Represents GitHub Actions expressions.
  *
  * ==Overview==
  * You can use expressions to programmatically set variables in workflow files and access contexts.
  *
  * An expression can be any combination of literal values, references to a context, or functions. You can combine
  * literals, context references, and functions using operators.
  *
  * Expressions are commonly used in conditionals ("if") on jobs and steps.
  *
  * ==How to create expressions?==
  * There is a bunch of ways to create an expression:
  *
  *   - You can use any of the predefined expressions and aliases available [[dsl.Expressions here]].
  *   - You can also create an [[Expression]] by getting any of the [[dsl.Contexts context values]].
  *   - Finally you can always create an [[Expression]] value by using the [[Expression.Constant]] constructor.
  *
  * ==How to work with expressions?==
  * You have available all the typical operators that GitHub Actions allow to extend
  * your expressions, such as: [[Expression.==(other* ==]], [[Expression.<(other* <]], [[Expression.&&(other* &&]]...
  *
  * @example {{{(is[PullRequest] && github.actor == "dependabot") || (is[Push] && matrix.os == "windows")}}}
  * @see [[https://docs.github.com/en/actions/reference/context-and-expression-syntax-for-github-actions]]
  */
sealed trait Expression {

  /** Returns this expression as a [[NonEmptyString]] by wrapping it inside "\${{ expression }}" as GitHub Actions
    * dictates.
    */
  def show(): NonEmptyString = NonEmptyString.unsafe("${{ " + showR() + " }}")

  /** Returns this expression as a `String` by wrapping it inside "\${{ expression }}" as GitHub Actions dictates. */
  override def toString(): String = show().value /* scalafix:ok */

  private def showR(parentheses: Boolean = false /* scalafix:ok */ ): String =
    this match {
      case Expression.Constant(value)              => s"'${value.value}'"
      case Expression.InfiniteDotExpression(value) => value.value
      case Expression.Equals(left, right) =>
        s"${left.showR()} == ${right.showR()}"
      case Expression.NotEquals(left, right) =>
        s"${left.showR()} != ${right.showR()}"
      case Expression.Not(Expression.Constant(value)) => s"!${value.value}"
      case Expression.Not(Expression.InfiniteDotExpression(value)) =>
        s"!${value.value}"
      case Expression.Not(operator) => s"!(${operator.showR()})"
      case Expression.Greater(left, right) =>
        s"${left.showR()} > ${right.showR()}"
      case Expression.Less(left, right) => s"${left.showR()} < ${right.showR()}"
      case Expression.GreaterEqual(left, right) =>
        s"${left.showR()} >= ${right.showR()}"
      case Expression.LessEqual(left, right) =>
        s"${left.showR()} <= ${right.showR()}"
      case Expression.And(left, right) if parentheses =>
        s"(${left.showR()} && ${right.showR()})"
      case Expression.And(left, right) => s"${left.showR()} && ${right.showR()}"
      case Expression.Or(left, right) if parentheses =>
        s"(${left.showR(true)} || ${right.showR(true)})"
      case Expression.Or(left, right) =>
        s"${left.showR(true)} || ${right.showR(true)}"
    }

  /** Alias for [[Expression.==(other* ==]]. It allows to avoid warnings created by linters about `==` usages.
    *
    * @example {{{context.actor === env.SPECIAL_ACTOR}}}
    */
  def ===(other: Expression): Expression = Expression.Equals(this, other)

  /** Alias for [[Expression.==(string* ==]]. It allows to avoid warnings created by linters about `==` usages.
    *
    * @example {{{context.actor === "dependabot"}}}
    */
  def ===(string: NonEmptyString): Expression = Expression.Equals(this, Expression.Constant(string))

  /** Alias for [[Expression.!=(other* !=]]. It allows to avoid warnings created by linters about `!=` usages.
    *
    * @example {{{context.actor =!= secrets.SECRET_ACTOR}}}
    */
  def =!=(other: Expression): Expression = Expression.NotEquals(this, other)

  /** Alias for [[Expression.!=(string* !=]]. It allows to avoid warnings created by linters about `!=` usages.
    *
    * @example {{{context.actor =!= "dependabot"}}}
    */
  def =!=(string: NonEmptyString): Expression = Expression.NotEquals(this, Expression.Constant(string))

  /** Creates an equals expression, that checks if the two parts of the expression are equal.
    *
    * @example {{{context.actor == env.SPECIAL_ACTOR}}}
    */
  def ==(other: Expression): Expression = Expression.Equals(this, other)

  /** Creates an equals expression, that checks if the two parts of the expression are equal.
    *
    * @example {{{context.actor == "dependabot"}}}
    */
  def ==(string: NonEmptyString): Expression = Expression.Equals(this, Expression.Constant(string))

  /** Creates a not-equals expression, that checks if the two parts of the expression aren't equal.
    *
    * @example {{{context.actor != secrets.SECRET_ACTOR}}}
    */
  def !=(other: Expression): Expression = Expression.NotEquals(this, other)

  /** Creates a not-equals expression, that checks if the two parts of the expression aren't equal.
    *
    * @example {{{context.actor != "dependabot"}}}
    */
  def !=(string: NonEmptyString): Expression = Expression.NotEquals(this, Expression.Constant(string))

  /** Creates a less-than expression, that checks if the left part of the expression is less than the value on the
    * right.
    *
    * @example {{{steps.build.outputs.warnings < envs.MAX_WARNINGS}}}
    */
  def <(other: Expression): Expression = Expression.Less(this, other)

  /** Creates a less-than expression, that checks if the left part of the expression is less than the value on the
    * right.
    *
    * @example {{{steps.build.outputs.warnings < "2"}}}
    */
  def <(string: NonEmptyString): Expression = Expression.Less(this, Expression.Constant(string))

  /** Creates a greater-than expression, that checks if the left part of the expression is greater than the value on the
    * right.
    *
    * @example {{{steps.build.outputs.warnings > envs.MAX_WARNINGS}}}
    */
  def >(other: Expression): Expression = Expression.Greater(this, other)

  /** Creates a greater-than expression, that checks if the left part of the expression is greater than the value on the
    * right.
    *
    * @example {{{steps.build.outputs.warnings > "2"}}}
    */
  def >(string: NonEmptyString): Expression = Expression.Greater(this, Expression.Constant(string))

  /** Creates a less-than-or-equals expression, that checks if the left part of the expression is less or equal to the
    * value on the right.
    *
    * @example {{{steps.build.outputs.warnings <= envs.MAX_WARNINGS}}}
    */
  def <=(other: Expression): Expression = Expression.LessEqual(this, other)

  /** Creates a less-than-or-equals expression, that checks if the left part of the expression is less or equal to the
    * value on the right.
    *
    * @example {{{steps.build.outputs.warnings <= "2"}}}
    */
  def <=(string: NonEmptyString): Expression = Expression.LessEqual(this, Expression.Constant(string))

  /** Creates a greater-than-or-equals expression, that checks if the left part of the expression is greater or equal to
    * the value on the right.
    *
    * @example {{{steps.build.outputs.warnings >= envs.MAX_WARNINGS}}}
    */
  def >=(other: Expression): Expression = Expression.GreaterEqual(this, other)

  /** Creates a greater-than-or-equals expression, that checks if the left part of the expression is greater or equal to
    * the value on the right.
    *
    * @example {{{steps.build.outputs.warnings >= "2"}}}
    */
  def >=(string: NonEmptyString): Expression = Expression.GreaterEqual(this, Expression.Constant(string))

  /** Negates the current expression.
    *
    * @example {{{(github.actor == "dependabot").not}}}
    */
  def not(): Expression = Expression.Not(this)

  /** Returns an expression that evaluates to `true` if either of the sides of the expression evaluates to `true`.
    *
    * @example {{{github.actor == "dependabot" || github.actor == "dependabot[bot]"}}}
    */
  def ||(other: Expression): Expression = Expression.Or(this, other)

  /** Returns an expression that evaluates to `true` if both of the sides of the expression evaluates to `true`.
    *
    * @example {{{is[PullRequest] && github.actor == "dependabot"}}}
    */
  def &&(other: Expression): Expression = Expression.And(this, other)

}

/** Contains constructors and implicit instances for the [[Expression]] class. */
object Expression {

  /** Allows converting an [[Expression]] value into [[yaml.Yaml yaml]]. */
  /** Allows converting a [[Expression]] value into [[yaml.Yaml yaml]]. */
  implicit val ExpressionEncoder: Encoder[Expression] = _.show().asYaml

  /** Represent a constant value to be used for an expression, such as a function call, a context value, or a literal
    * value. It can also be used to create complex expressions without using the operators DSL.
    */
  final case class Constant private (private val value: NonEmptyString) extends Expression

  /** Special case of expressions used by some contexts like [[dsl.Contexts.github.event github.event]] that allows to
    * extend it infinitely with inner members.
    *
    * @example {{{github.event.pull_request.head.repo.full_name}}}
    */
  final case class InfiniteDotExpression private (private val value: NonEmptyString) extends Expression with Dynamic {

    /** @see [[https://www.scala-lang.org/api/current/scala/Dynamic.html]] */
    def selectDynamic(name: String): InfiniteDotExpression = InfiniteDotExpression(
      NonEmptyString.unsafe(s"$value.$name")
    )

  }

  /** Represents an "equals" expression. */
  final case class Equals(left: Expression, right: Expression) extends Expression

  /** Represents a "not-equals" expression. */
  final case class NotEquals(left: Expression, right: Expression) extends Expression

  /** Represents a "negative" expression. */
  final case class Not(expression: Expression) extends Expression

  /** Represents a "greater-than" expression. */
  final case class Greater(left: Expression, right: Expression) extends Expression

  /** Represents a "less-than" expression. */
  final case class Less(left: Expression, right: Expression) extends Expression

  /** Represents a "greater-than-or-equals" expression. */
  final case class GreaterEqual(left: Expression, right: Expression) extends Expression

  /** Represents a "less-than-or-equals" expression. */
  final case class LessEqual(left: Expression, right: Expression) extends Expression

  /** Represents an "and" expression. */
  final case class And(left: Expression, right: Expression) extends Expression

  /** Represents an "or" expression. */
  final case class Or(left: Expression, right: Expression) extends Expression

  /** Unsafely creates a [[Expression.Constant expression]]. Take into account that this method will throw an exception
    * if the supplied value is not a valid expression. If you are not sure if the value is valid use any of the
    * predefined constructors instead.
    *
    * @param value The value to convert to an [[Expression]].
    * @throws InvalidRefinement If `value` is not valid.
    * @return An [[Expression]] value.
    */
  def unsafe(value: String): Expression = Expression.Constant(NonEmptyString.unsafe(value))

}
