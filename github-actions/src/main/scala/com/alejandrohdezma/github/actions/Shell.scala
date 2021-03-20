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

sealed abstract class Shell(val value: NonEmptyString)

object Shell {

  implicit val ShellEncoder: Encoder[Shell] = _.value.asYaml

  case object bash       extends Shell(NonEmptyString.unsafe("bash"))
  case object pwsh       extends Shell(NonEmptyString.unsafe("pwsh"))
  case object python     extends Shell(NonEmptyString.unsafe("python"))
  case object sh         extends Shell(NonEmptyString.unsafe("sh"))
  case object cmd        extends Shell(NonEmptyString.unsafe("cmd"))
  case object powershell extends Shell(NonEmptyString.unsafe("powershell"))

  /** @see
    *   [[https://help.github.com/en/actions/reference/workflow-syntax-for-github-actions#custom-shell]]
    */
  final case class Custom(shell: NonEmptyString) extends Shell(shell)

}
