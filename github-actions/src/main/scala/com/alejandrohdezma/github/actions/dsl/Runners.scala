package com.alejandrohdezma.github.actions.dsl

import com.alejandrohdezma.github.actions.Runner

@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait Runners {

  lazy val `macos-10.15` = Runner.HostedRunner.`macos-10.15`
  type `macos-10.15` = Runner.HostedRunner.`macos-10.15`.type

  lazy val `macos-11.0` = Runner.HostedRunner.`macos-11.0`
  type `macos-11.0` = Runner.HostedRunner.`macos-11.0`.type

  lazy val `macos-latest` = Runner.HostedRunner.`macos-latest`
  type `macos-latest` = Runner.HostedRunner.`macos-latest`.type

  lazy val `self-hosted` = Runner.HostedRunner.`self-hosted`
  type `self-hosted` = Runner.HostedRunner.`self-hosted`.type

  lazy val `ubuntu-16.04` = Runner.HostedRunner.`ubuntu-16.04`
  type `ubuntu-16.04` = Runner.HostedRunner.`ubuntu-16.04`.type

  lazy val `ubuntu-18.04` = Runner.HostedRunner.`ubuntu-18.04`
  type `ubuntu-18.04` = Runner.HostedRunner.`ubuntu-18.04`.type

  lazy val `ubuntu-20.04` = Runner.HostedRunner.`ubuntu-20.04`
  type `ubuntu-20.04` = Runner.HostedRunner.`ubuntu-20.04`.type

  lazy val `ubuntu-latest` = Runner.HostedRunner.`ubuntu-latest`
  type `ubuntu-latest` = Runner.HostedRunner.`ubuntu-latest`.type

  lazy val `windows-2016` = Runner.HostedRunner.`windows-2016`
  type `windows-2016` = Runner.HostedRunner.`windows-2016`.type

  lazy val `windows-2019` = Runner.HostedRunner.`windows-2019`
  type `windows-2019` = Runner.HostedRunner.`windows-2019`.type

  lazy val `windows-latest` = Runner.HostedRunner.`windows-latest`
  type `windows-latest` = Runner.HostedRunner.`windows-latest`.type

  lazy val selfHosted = Runner.SelfHosted()

}
