package com.alejandrohdezma.github.actions.dsl

import com.alejandrohdezma.github.actions.NotEmptyString
import com.alejandrohdezma.github.actions.Shell

@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait Shells {

  lazy val Bash = Shell.Bash
  type Bash = Shell.Bash.type

  lazy val Pwsh = Shell.Pwsh
  type Pwsh = Shell.Pwsh.type

  lazy val Python = Shell.Python
  type Python = Shell.Python.type

  lazy val Sh = Shell.Sh
  type Sh = Shell.Sh.type

  lazy val Cmd = Shell.Cmd
  type Cmd = Shell.Cmd.type

  lazy val Powershell = Shell.Powershell
  type Powershell = Shell.Powershell.type

  def customShell(shell: NotEmptyString): Shell.Custom = Shell.Custom(shell)

}
