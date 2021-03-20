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

package com.alejandrohdezma.github.actions.dsl

import com.alejandrohdezma.github.actions.Container
import com.alejandrohdezma.github.actions.Input
import com.alejandrohdezma.github.actions.Job
import com.alejandrohdezma.github.actions.Matrix
import com.alejandrohdezma.github.actions.Strategy
import com.alejandrohdezma.github.actions.Workflow
import com.alejandrohdezma.github.actions.WorkflowFile
import com.alejandrohdezma.github.actions.base.Expression
import com.alejandrohdezma.github.actions.base.FileName
import com.alejandrohdezma.github.actions.base.Id
import com.alejandrohdezma.github.actions.base.NotEmptyList
import com.alejandrohdezma.github.actions.base.NotEmptyString

@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait Dsl
    extends Architectures
    with Contexts
    with Crons
    with Events
    with Expressions
    with Machines
    with Runners
    with Shells
    with Steps {

  def workflowFile(name: FileName)(workflow: Workflow): WorkflowFile = WorkflowFile(name, workflow)

  lazy val workflow = Workflow.Builder1()

  /** Matrix */

  def matrix(value: NotEmptyString): Expression = Expression.Constant(NotEmptyString.unsafe(s"matrix.${value.value}"))

  object matrix {

    def config(key: NotEmptyString)(first: NotEmptyString, rest: NotEmptyString*) =
      Strategy.FromMatrix(Matrix(NotEmptyList.of(Matrix.Configuration(key, NotEmptyList.of(first, rest: _*)))))

  }

  /** Input */

  def input(name: Id): Input = Input(name)

  /** Container */

  def from(image: NotEmptyString): Container = Container(image = image)

  /** Jobs */

  def job(id: Id): Job.Builder = Job.Builder(id)

}
