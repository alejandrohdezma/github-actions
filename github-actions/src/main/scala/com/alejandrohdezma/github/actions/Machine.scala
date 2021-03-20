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

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

sealed abstract class Machine(val value: NonEmptyString)

object Machine {

  /** Allows converting a [[Machine]] value into [[yaml.Yaml yaml]]. */
  implicit val MachineEncoder: Encoder[Machine] = _.value.asYaml

  case object Linux   extends Machine(NonEmptyString.unsafe("linux"))
  case object Macos   extends Machine(NonEmptyString.unsafe("macos"))
  case object Windows extends Machine(NonEmptyString.unsafe("windows"))

}
