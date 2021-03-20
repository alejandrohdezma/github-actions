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
import com.alejandrohdezma.github.actions.base.Url
import com.alejandrohdezma.github.actions.yaml._

/** The environment that a job references.
  *
  * @param name
  *   the name of the environment configured in the repo
  * @param url
  *   a deployment URL
  */
final case class Environment(name: NonEmptyString, url: Option[Url] = None)

object Environment {

  /** Allows converting a [[Environment]] value into [[yaml.Yaml yaml]]. */
  implicit val EnvironmentEncoder: Encoder[Environment] = e => Yaml.obj("name" := e.name, "url" := e.url)

}
