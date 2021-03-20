package com.alejandrohdezma.github.actions.dsl

import com.alejandrohdezma.github.actions.Machine

/** Contains aliases for the different [[Machines]] possible values, so users don't need to import them when using
  * the DSL.
  */
@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait Machines {

  /** Label indicating to only use a runner using a "Linux" machine.
    *
    * @example {{{selfHosted.machine(linux)}}}
    */
  lazy val linux = Machine.Linux

  /** Label indicating to only use a runner using a "MacOS" machine.
    *
    * @example {{{selfHosted.machine(macOs)}}}
    */
  lazy val macOS = Machine.Macos

  /** Label indicating to only use a runner using a "Windows" machine.
    *
    * @example {{{selfHosted.machine(windows)}}}
    */
  lazy val windows = Machine.Windows

}
