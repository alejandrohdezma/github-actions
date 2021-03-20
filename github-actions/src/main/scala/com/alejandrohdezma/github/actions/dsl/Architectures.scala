package com.alejandrohdezma.github.actions.dsl

import com.alejandrohdezma.github.actions.Architecture

@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait Architectures {

  lazy val x64 = Architecture.x64
  type x64 = Architecture.x64.type

  lazy val ARM = Architecture.ARM
  type ARM = Architecture.ARM.type

  lazy val ARM64 = Architecture.ARM64
  type ARM64 = Architecture.ARM64.type

}
