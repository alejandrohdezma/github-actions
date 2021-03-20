package com.alejandrohdezma.github.actions

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean._
import eu.timepit.refined.collection._
import eu.timepit.refined.string._
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Encoder
import io.circe.refined._
import io.circe.syntax._

final case class Expression(
    expression: String Refined And[NonEmpty, And[Not[StartsWith[W.`"${{"`.T]], Not[EndsWith[W.`"}}"`.T]]]]
) {

  lazy val value: NonEmptyString = NonEmptyString.unsafeFrom("${{ " + expression + " }}")

  def `||`(other: Expression) = Expression(Refined.unsafeApply(s"($expression || ${other.expression})"))

  def `&&`(other: Expression) = Expression(Refined.unsafeApply(s"($expression && ${other.expression})"))

}

object Expression {

  implicit val ExpressionEncoder: Encoder[Expression] = _.value.asJson

}
