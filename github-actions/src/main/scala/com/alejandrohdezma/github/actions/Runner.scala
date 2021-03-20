/*
 * Copyright 2021 Alejandro Hernández <https://github.com/alejandrohdezma>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alejandrohdezma.github.actions

import com.alejandrohdezma.github.actions.yaml._
sealed trait Runner

object Runner {

  implicit val RunsOnEncoder: Encoder[Runner] = {
    case h: HostedRunner   => h.value.asYaml
    case e: FromExpression => e.expression.asYaml
    case r: SelfHosted =>
      (List("self-hosted".asYaml, r.machine.asYaml, r.architecture.asYaml) ++ r.extraLabels.map(_.asYaml))
        .filterNot(_.isNull())
        .asYaml
  }

  sealed abstract class HostedRunner(val value: NotEmptyString) extends Runner

  object HostedRunner {

    case object `macos-10.15`    extends HostedRunner("macos-10.15")
    case object `macos-11.0`     extends HostedRunner("macos-11.0")
    case object `macos-latest`   extends HostedRunner("macos-latest")
    case object `self-hosted`    extends HostedRunner("self-hosted")
    case object `ubuntu-16.04`   extends HostedRunner("ubuntu-16.04")
    case object `ubuntu-18.04`   extends HostedRunner("ubuntu-18.04")
    case object `ubuntu-20.04`   extends HostedRunner("ubuntu-20.04")
    case object `ubuntu-latest`  extends HostedRunner("ubuntu-latest")
    case object `windows-2016`   extends HostedRunner("windows-2016")
    case object `windows-2019`   extends HostedRunner("windows-2019")
    case object `windows-latest` extends HostedRunner("windows-latest")

  }

  final case class FromExpression(expression: Expression) extends Runner

  final case class SelfHosted(
      machine: Option[Machine] = None,
      architecture: Option[Architecture] = None,
      extraLabels: List[NotEmptyString] = Nil
  ) extends Runner {

    def machine(machine: Machine): SelfHosted = copy(machine = Some(machine))

    def architecture(architecture: Architecture): SelfHosted = copy(architecture = Some(architecture))

    def labels(labels: NotEmptyString*): SelfHosted = copy(extraLabels = extraLabels ++ labels)

  }

}
