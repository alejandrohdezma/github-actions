package com.alejandrohdezma.github

import com.alejandrohdezma.github.actions.dsl.Dsl

package object actions extends Dsl {

  implicit class ExpressionInterpolator(private val sc: StringContext) extends AnyVal {

    /**
     * Validates and transforms a literal string as a valid expression in compile time
     */
    def exp(args: Any*): Expression = macro Macros.expInterpolator

  }

}
