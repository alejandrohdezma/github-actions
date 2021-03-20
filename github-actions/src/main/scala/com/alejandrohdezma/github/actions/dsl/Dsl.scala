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

import com.alejandrohdezma.github.actions.Workflow
import com.alejandrohdezma.github.actions.WorkflowFile
import com.alejandrohdezma.github.actions.base.FileName

/** Main DSL for this library. Most of the time you won't need to use this `trait` directly, but instead add the
  * following import:
  *
  * {{{
  * import com.alejandrohdezma.github.actions._
  * }}}
  *
  * However, if you want to create your own entrypoint for your SBT plugin or library you can just extend this trait.
  *
  * {{{
  * object MyPlugin extends sbt.AutoPlugin {
  *
  * object autoImport extends com.alejandrohdezma.github.actions.dsl.Dsl
  *
  * ...
  *
  * }
  * }}}
  *
  * @note Importing `com.alejandrohdezma.github.actions._` won't be needed if using the DSL from SBT, only from `.scala`
  *   files.
  */
trait Dsl
    extends Architectures
    with Contexts
    with Crons
    with Containers
    with Events
    with Expressions
    with Machines
    with Runners
    with Shells
    with Steps {

  /** Creates a GitHub Action workflow file definition that can be passed to the plugin's `workflows` setting.
    *
    * @example {{{
    * workflows += workflowFile("hello-world") {
    *   workflow
    *     .on(push, pullRequest)
    *     .job("hello")(_.step(run("echo 'Hello World'")))
    * }
    * }}}
    * @param name Name of the file (without the extension).
    * @param workflow The workflow definition.
    * @return A GitHub Action workflow file definition.
    */
  def workflowFile(name: FileName)(workflow: Workflow): WorkflowFile = WorkflowFile(name, workflow)

  /** Entrypoint for creating workflows. Contains methods for providing all the different aspects of a GitHub Actions
    * workflow.
    *
    * @example {{{
    *   workflow
    *     .name("Scala CI")
    *     .on(push.branches(matching("main")))
    *     .on(pullRequest.branches(matching("main")))
    *     .job("build") {
    *       _.runsOn(`ubuntu-latest`)
    *         .steps(
    *           uses("actions/checkout@v2"),
    *           uses("actions/setup-java@v2")
    *             .name("Set up JDK 11")
    *             .withInput("java-version", "11")
    *             .withInput("distribution", "adopt"),
    *           run("sbt test").name("Run tests")
    *         )
    *     }
    * }}}
    */
  lazy val workflow = Workflow.Builder1() /* scalafix:ok DisableSyntax.valInAbstract */

}
