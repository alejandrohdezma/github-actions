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

import com.alejandrohdezma.github.actions.base.NotEmptyString
import com.alejandrohdezma.github.actions.yaml._

/**
 * Represent the different architectures that can be used when using a self-hosted runner
 * to run a workflow's job.
 */
sealed abstract class Architecture(val value: NotEmptyString)

object Architecture {

  implicit val ArchitectureEncoder: Encoder[Architecture] = _.value.asYaml

  case object x64   extends Architecture(NotEmptyString.unsafe("x64"))
  case object ARM   extends Architecture(NotEmptyString.unsafe("ARM"))
  case object ARM64 extends Architecture(NotEmptyString.unsafe("ARM64"))

}
