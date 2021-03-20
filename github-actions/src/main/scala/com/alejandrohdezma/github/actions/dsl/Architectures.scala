package com.alejandrohdezma.github.actions.dsl

import com.alejandrohdezma.github.actions.Architecture

/** Contains aliases for the different [[Architecture]] possible values, so users don't need to import them when using
  * the DSL.
  */
@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait Architectures {

  /** Label indicating to only use a runner based on x64 hardware.
    *
    * @example {{{selfHosted.architecture(x64)}}}
    */
  lazy val x64 = Architecture.x64

  /** Label indicating to only use a runner based on ARM hardware hardware.
    *
    * @example {{{selfHosted.architecture(ARM)}}}
    */
  lazy val ARM = Architecture.ARM

  /** Label indicating to only use a runner based on ARM64 hardware hardware.
    *
    * @example {{{selfHosted.architecture(ARM64)}}}
    */
  lazy val ARM64 = Architecture.ARM64

}
