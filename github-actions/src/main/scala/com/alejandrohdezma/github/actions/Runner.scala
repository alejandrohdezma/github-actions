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

import enumeratum.CirceEnum
import enumeratum.Enum
import enumeratum.EnumEntry
import io.circe.Encoder
import io.circe.syntax._

sealed trait Runner

object Runner {

  implicit val RunsOnEncoder: Encoder[Runner] = {
    case h: HostedRunner   => h.asJson
    case e: FromExpression => e.expression.asJson
    case r: SelfHosted =>
      (List("self-hosted".asJson, r.machine.asJson, r.architecture.asJson) ++ r.extraLabels.map(
        _.asJson
      )).asJson.deepDropNullValues
  }

  sealed trait HostedRunner extends EnumEntry with Runner

  object HostedRunner extends Enum[HostedRunner] with CirceEnum[HostedRunner] {

    case object `macos-10.15`    extends HostedRunner
    case object `macos-11.0`     extends HostedRunner
    case object `macos-latest`   extends HostedRunner
    case object `self-hosted`    extends HostedRunner
    case object `ubuntu-16.04`   extends HostedRunner
    case object `ubuntu-18.04`   extends HostedRunner
    case object `ubuntu-20.04`   extends HostedRunner
    case object `ubuntu-latest`  extends HostedRunner
    case object `windows-2016`   extends HostedRunner
    case object `windows-2019`   extends HostedRunner
    case object `windows-latest` extends HostedRunner

    val values = findValues

  }

  final case class FromExpression(expression: Expression) extends Runner
  def apply(expression: Expression): FromExpression = FromExpression(expression)

  final case class SelfHosted(
      machine: Option[Machine] = None,
      architecture: Option[Architecture] = None,
      extraLabels: List[String] = Nil
  ) extends Runner

  lazy val selfHosted = SelfHosted()

  def selfHosted(
      machine: Option[Machine] = None /* scalafix:ok DisableSyntax.defaultArgs */,
      architecture: Option[Architecture] = None /* scalafix:ok DisableSyntax.defaultArgs */,
      extraLabels: List[String] = Nil /* scalafix:ok DisableSyntax.defaultArgs */
  ): SelfHosted = SelfHosted(machine, architecture, extraLabels)

}
