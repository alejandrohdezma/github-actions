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
 * A set of default settings for `run` steps.
 *
 * @param shell the default shell settings in the runner's operating system. You can use built-in shell keywords, or
 *   you can define a custom set of shell options.
 * @param workingDirectory the working directory of where to run commands
 */
final case class Defaults(shell: Option[Shell] = None, workingDirectory: Option[NotEmptyString] = None)

object Defaults {

  implicit lazy val DefaultsEncoder: Encoder[Defaults] = {
    case d: Defaults if d.shell.nonEmpty || d.workingDirectory.nonEmpty =>
      Yaml.obj("run" := Yaml.obj("shell" := d.shell, "working-directory" := d.workingDirectory))
    case _ => Yaml.Null
  }

}
