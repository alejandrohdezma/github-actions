package com.alejandrohdezma.github.actions.dsl

import com.alejandrohdezma.github.actions.Shell
import com.alejandrohdezma.github.actions.base.NonEmptyString

/** Contains aliases and default values for the different shells on which to run a workflow's [[Run run]] step. */
@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait Shells {

  /** Alias for the [[Shell.bash bash shell]]. It can be used to indicate in which shell to [[run]] steps.
    *
    * @example {{{run("sbt test").shell(bash)}}}
    */
  lazy val bash = Shell.bash

  /** Alias for the [[Shell.pwsh pwsh shell]]. It can be used to indicate in which shell to [[run]] steps.
    *
    * @example {{{run("sbt test").shell(pwsh)}}}
    */
  lazy val pwsh = Shell.pwsh

  /** Alias for the [[Shell.python python shell]]. It can be used to indicate in which shell to [[run]] steps.
    *
    * @example {{{run("sbt test").shell(python)}}}
    */
  lazy val python = Shell.python

  /** Alias for the [[Shell.sh sh shell]]. It can be used to indicate in which shell to [[run]] steps.
    *
    * @example {{{run("sbt test").shell(sh)}}}
    */
  lazy val sh = Shell.sh

  /** Alias for the [[Shell.cmd cmd shell]]. It can be used to indicate in which shell to [[run]] steps.
    *
    * @example {{{run("sbt test").shell(cmd)}}}
    */
  lazy val cmd = Shell.cmd

  /** Alias for the [[Shell.powershell powershell shell]]. It can be used to indicate in which shell to [[run]] steps.
    *
    * @example {{{run("sbt test").shell(powershell)}}}
    */
  lazy val powershell = Shell.powershell

  /** You can set the shell value to a template string using `command […options] {0} [..more_options]`. GitHub
    * interprets the first whitespace-delimited word of the string as the command, and inserts the file name for the
    * temporary script at `{0}`.
    *
    * @example {{{customShell("perl {0}")}}}
    */
  def customShell(shell: NonEmptyString): Shell.Custom = Shell.Custom(shell)

}
