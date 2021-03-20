package com.alejandrohdezma.github.actions.dsl

import com.alejandrohdezma.github.actions.Machine

@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait Machines {

  lazy val Linux = Machine.Linux
  type Linux = Machine.Linux.type

  lazy val Macos = Machine.Macos
  type Macos = Machine.Macos.type

  lazy val Windows = Machine.Windows
  type Windows = Machine.Windows.type

}
