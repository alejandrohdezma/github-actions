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

sealed abstract class Shell(val value: NotEmptyString)

object Shell {

  implicit val ShellEncoder: Encoder[Shell] = _.value.asYaml

  case object Bash       extends Shell("bash")
  case object Pwsh       extends Shell("pwsh")
  case object Python     extends Shell("python")
  case object Sh         extends Shell("sh")
  case object Cmd        extends Shell("cmd")
  case object Powershell extends Shell("powershell")

  /**
   * @see [[https://help.github.com/en/actions/reference/workflow-syntax-for-github-actions#custom-shell]]
   */
  final case class Custom(shell: NotEmptyString) extends Shell(shell)

}
