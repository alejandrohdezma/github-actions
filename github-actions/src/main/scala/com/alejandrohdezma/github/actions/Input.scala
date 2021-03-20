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

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string._
import io.circe.Encoder
import io.circe.Json
import io.circe.syntax._

/**
 * Input parameters allow you to specify data that the action expects to use during runtime. GitHub stores input
 * parameters as environment variables. Input ids with uppercase letters are converted to lowercase during runtime. We
 * recommended using lowercase input ids.
 *
 * @param name a string identifier to associate with the input. The value of <input_id> is a map of the input's
 *   metadata. The <input_id> must be a unique identifier within the inputs object. The <input_id> must start with a
 *   letter or _ and contain only alphanumeric characters, -, or _
 * @param description a string description of the input parameter
 * @param deprecationMessage a string shown to users using the deprecated input
 * @param required a boolean to indicate whether the action requires the input parameter. Set to true when the parameter is required
 * @param default a string representing the default value. The default value is used when an input parameter isn't
 *   specified in a workflow file
 */
final case class Input(
    name: String Refined MatchesRegex[W.`"^[_a-zA-Z][a-zA-Z0-9_-]*$"`.T],
    description: String = "",
    deprecationMessage: String = "",
    required: Boolean = false,
    default: String = ""
)

object Input {

  implicit val InputEncoder: Encoder[Input] = input =>
    Json.obj(
      input.name.value := Json.obj(
        "description"        := input.description,
        "deprecationMessage" := input.deprecationMessage,
        "required"           := input.required,
        "default"            := input.default
      )
    )
}
