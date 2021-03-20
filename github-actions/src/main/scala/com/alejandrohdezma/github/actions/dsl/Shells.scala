package com.alejandrohdezma.github.actions.dsl

import com.alejandrohdezma.github.actions.Shell
import com.alejandrohdezma.github.actions.base.NonEmptyString

@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait Shells {

  lazy val bash = Shell.bash
  type bash = Shell.bash.type

  lazy val pwsh = Shell.pwsh
  type pwsh = Shell.pwsh.type

  lazy val python = Shell.python
  type python = Shell.python.type

  lazy val sh = Shell.sh
  type sh = Shell.sh.type

  lazy val cmd = Shell.cmd
  type cmd = Shell.cmd.type

  lazy val powershell = Shell.powershell
  type powershell = Shell.powershell.type

  def customShell(shell: NonEmptyString): Shell.Custom = Shell.Custom(shell)

}
