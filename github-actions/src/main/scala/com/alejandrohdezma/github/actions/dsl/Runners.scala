package com.alejandrohdezma.github.actions.dsl

import com.alejandrohdezma.github.actions.Runner

/** Contains aliases and default values for the different runners on which to run a workflow's job. */
@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait Runners {

  /** Alias for the `macos-10.15` runner.
    *
    * @example {{{.runsOn(`macos-10.15`)}}}
    */
  lazy val `macos-10.15` = Runner.HostedRunner.`macos-10.15`

  /** Alias for the `macos-11.0` runner.
    *
    * @example {{{.runsOn(`macos-11.0`)}}}
    */
  lazy val `macos-11.0` = Runner.HostedRunner.`macos-11.0`

  /** Alias for the `macos-latest` runner.
    *
    * @example {{{.runsOn(`macos-latest`)}}}
    */
  lazy val `macos-latest` = Runner.HostedRunner.`macos-latest`

  /** Alias for the `self-hosted` runner.
    *
    * @example {{{.runsOn(`self-hosted`)}}}
    */
  lazy val `self-hosted` = Runner.HostedRunner.`self-hosted`

  /** Alias for the `ubuntu-16.04` runner.
    *
    * @example {{{.runsOn(`ubuntu-16.04`)}}}
    */
  lazy val `ubuntu-16.04` = Runner.HostedRunner.`ubuntu-16.04`

  /** Alias for the `ubuntu-18.04` runner.
    *
    * @example {{{.runsOn(`ubuntu-18.04`)}}}
    */
  lazy val `ubuntu-18.04` = Runner.HostedRunner.`ubuntu-18.04`

  /** Alias for the `ubuntu-20.04` runner.
    *
    * @example {{{.runsOn(`ubuntu-20.04`)}}}
    */
  lazy val `ubuntu-20.04` = Runner.HostedRunner.`ubuntu-20.04`

  /** Alias for the `ubuntu-latest` runner.
    *
    * @example {{{.runsOn(`ubuntu-latest`)}}}
    */
  lazy val `ubuntu-latest` = Runner.HostedRunner.`ubuntu-latest`

  /** Alias for the `windows-2016` runner.
    *
    * @example {{{.runsOn(`windows-2016`)}}}
    */
  lazy val `windows-2016` = Runner.HostedRunner.`windows-2016`

  /** Alias for the `windows-2019` runner.
    *
    * @example {{{.runsOn(`windows-2019`)}}}
    */
  lazy val `windows-2019` = Runner.HostedRunner.`windows-2019`

  /** Alias for the `windows-latest` runner.
    *
    * @example {{{.runsOn(`windows-lates)}}}
    */
  lazy val `windows-latest` = Runner.HostedRunner.`windows-latest`

  /** Default value for a [[Runner.SelfHosted self hosted runner]].
    *
    * @example {{{.runsOn(selfHosted.machine(Linux).architecture(x64).labels("miau", "meow"))}}}
    */
  lazy val selfHosted = Runner.SelfHosted()

}
