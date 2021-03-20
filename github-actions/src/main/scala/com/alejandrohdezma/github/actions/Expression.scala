package com.alejandrohdezma.github.actions

import io.circe.Encoder
import io.circe.syntax._

final case class Expression(private val expression: String) {

  lazy val value: String = "${{ " + expression + " }}"

}

object Expression {

  implicit val ExpressionEncoder: Encoder[Expression] = _.value.asJson

}
