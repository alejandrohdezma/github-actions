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

import com.alejandrohdezma.github.actions.base.Expression
import com.alejandrohdezma.github.actions.base.Id
import com.alejandrohdezma.github.actions.base.NotEmptyString
import com.alejandrohdezma.github.actions.yaml._

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
    name: Id,
    description: Option[NotEmptyString] = None,
    deprecationMessage: Option[NotEmptyString] = None,
    required: Boolean = false,
    default: Option[NotEmptyString] = None
) {

  def description(newDescription: NotEmptyString): Input = copy(description = Some(newDescription))

  def deprecationMessage(message: NotEmptyString): Input =
    copy(deprecationMessage = Some(message))

  def isRequired(): Input = copy(required = true)

  def default(newDefault: NotEmptyString): Input = copy(default = Some(newDefault))

  def default(expression: Expression): Input = default(expression.show())

}

object Input {

  implicit val InputEncoder: Encoder[Input] = input =>
    Yaml.obj(
      input.name.value := Yaml.obj(
        "description"        := input.description,
        "deprecationMessage" := input.deprecationMessage,
        "required"           := input.required,
        "default"            := input.default
      )
    )
}
