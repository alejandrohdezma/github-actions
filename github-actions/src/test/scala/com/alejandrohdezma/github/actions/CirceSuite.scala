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

import java.nio.file.Files

import scala.reflect.ClassTag
import scala.reflect.classTag
import scala.util.control.NoStackTrace

import cats.data.NonEmptyList
import cats.data.NonEmptyMap

import eu.timepit.refined.auto._
import io.circe.syntax._
import io.circe.yaml.Printer
import io.circe.yaml.Printer._

class CirceSuite extends munit.FunSuite {

  implicit class StringOperatorOps(string: String) {

    def or(other: String)  = s"($string || $other)"
    def and(other: String) = s"($string && $other)"

  }

  case class InvalidExpression(error: String) extends NoStackTrace {

    override def getMessage(): String = error

  }

  implicit class ExpressionInterpolator(sc: StringContext) {

    @SuppressWarnings(Array("scalafix:Disable.scala.collection.mutable"))
    def exp(args: Any*): Expression = {
      val literals    = sc.parts.iterator
      val expressions = args.iterator
      val buffer      = new StringBuilder(literals.next())

      while (literals.hasNext) // scalafix:ok DisableSyntax.while
        buffer.append(expressions.next()).append(literals.next())

      Expression(buffer.mkString)
    }

  }

  def is[E <: Event: ClassTag] = {
    val eventName =
      classTag[E].runtimeClass
        .getSimpleName()
        .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
        .replaceAll("([a-z\\d])([A-Z])", "$1_$2")
        .toLowerCase()

    s"github.event_name == '$eventName'"
  }

  lazy val always = "always()"

  test("Create ci.yml workflow") {
    val workflow = Workflow(
      name = Some("CI"),
      env = Some(NonEmptyMap.of("SOME_KEY" -> "my_value")),
      defaults = Some(Defaults(Some(Shell.Custom("perl {0}")), Some("/user"))),
      on = NonEmptyList.of(
        Event.CheckRun(),
        Event.CheckSuite(),
        Event.Create,
        Event.Delete,
        Event.Deployment,
        Event.DeploymentStatus,
        Event.Fork,
        Event.Gollum,
        Event.IssueComment(),
        Event.Issues(),
        Event.Label(),
        Event.Member(),
        Event.Milestone(),
        Event.PageBuild,
        Event.Project(),
        Event.ProjectCard(),
        Event.ProjectColumn(),
        Event.Public,
        Event.PullRequest(
          branches = Some(Branches.Matching(NonEmptyList.of("main", "releases/**"))),
          tags = Some(Tags.Matching(NonEmptyList.of("v**", "latest"))),
          paths = Some(Paths.Matching(NonEmptyList.of(".github/ci.yml", "README.md")))
        ),
        Event.PullRequestReview(),
        Event.PullRequestReviewComment(),
        Event.PullRequestTarget(),
        Event.Push(
          branches = Some(Branches.Ignore(NonEmptyList.of("main", "releases/**"))),
          tags = Some(Tags.Ignore(NonEmptyList.of("v**", "latest"))),
          paths = Some(Paths.Ignore(NonEmptyList.of(".github/ci.yml", "README.md")))
        ),
        Event.RegistryPackage(),
        Event.Release(),
        Event.Status,
        Event.Watch,
        Event.WorkflowDispatch(
          Input(
            name = "repository",
            description = "The repository to use",
            deprecationMessage = "This input is deprecated, use repositories instead",
            required = false,
            default = exp"env.GITHUB_REPOSITORY".value
          ),
          Input(
            name = "repositories",
            description = "Comma-separated list of repositories",
            deprecationMessage = "The list of repositories to use",
            required = false,
            default = exp"env.GITHUB_REPOSITORY".value
          )
        ),
        Event.WorkflowRun(
          workflows = NonEmptyList.of("ci", "release"),
          branches = Some(Branches.Matching(NonEmptyList.of("main")))
        ),
        Event.RepositoryDispatch("some_event", "another_event"),
        Event.Schedule(NonEmptyList.of("0 5 * * *", "0 3 * * *"))
      ),
      jobs = NonEmptyList.of(
        Job(
          "scala_steward",
          name = Some("Run `sbt fix generateCiFiles` in Scala Steward PRs"),
          environment = Some(Environment("staging", Some("https://example.com"))),
          env = Some(NonEmptyMap.of("TOKEN" -> exp"secrets.MY_TOKEN".value)),
          defaults = Some(Defaults(Some(Shell.Bash), Some("/user/home"))),
          continueOnError = Some(Left(false)),
          `if` = Some(
            is[Event.PullRequest] and "github.event.pull_request.head.repo.full_name == github.repository" and
              "github.event.pull_request.user.login == 'alejandrohdezma-steward[bot]'"
          ),
          outputs = Some(NonEmptyMap.of("changes_detected" -> exp"steps.push.outputs.changes_detected".value)),
          container = Some(
            Container(
              image = "ubuntu:latest",
              credentials = Some(Container.Credentials("alejandrohdezma", exp"secrets.DOCKER_PASSWORD".value)),
              env = Some(NonEmptyMap.of("USER_DIR" -> "/home/alex")),
              ports = Some(NonEmptyList.of(8080, 8090)),
              volumes = Some(NonEmptyList.of("/home/alex/mount:/dev/mount")),
              options = Some("--silent")
            )
          ),
          services = Some(
            NonEmptyList.of(
              Service("redis", container = Container("redis:latest", ports = Some(NonEmptyList.of(4004))))
            )
          ),
          strategy = Some(
            Strategy(
              Matrix.Configurations(
                NonEmptyList.of(
                  Matrix.Configuration("os", NonEmptyList.of("ubuntu-16.04", "ubuntu-18.04")),
                  Matrix.Configuration("node", NonEmptyList.of("6", "8", "10"))
                ),
                include = Some(
                  NonEmptyList.of(NonEmptyMap.of("os" -> "ubuntu-16.04", "node" -> "8", "experimental" -> "true"))
                ),
                exclude = Some(
                  NonEmptyList.of(NonEmptyMap.of("os" -> "ubuntu-18.04", "node" -> "6"))
                )
              ),
              failFast = Some(false),
              maxParallel = Some(2)
            )
          ),
          steps = NonEmptyList.of(
            Step.Uses(
              "hmarr/auto-approve-action@v2",
              "github-token" -> exp"secrets.ADMIN_GITHUB_TOKEN".value
            )(name = Some("Auto-approve Scala Steward PRs")),
            Step.Run("sbt generateCiFiles fix || sbt \"generateCiFiles; scalafmtAll; scalafmtSbt\" || true")
          )
        ),
        Job(
          "test",
          runsOn = RunsOn.Expression(exp"matrix.os".value),
          `if` = Some(is[Event.PullRequest]),
          timeoutMinutes = Some(10),
          continueOnError = Some(Right(exp"matrix.os == 'linux'")),
          steps = NonEmptyList.of(Step.Uses("coursier/cache-action@v6")(), Step.Run("sbt ci-test"))
        ),
        Job(
          "test-2",
          runsOn = RunsOn.SelfHostedRunner(Some(Machine.Linux), Some(Architecture.x64), List("miau", "meow")),
          needs = Some(NonEmptyList.of("scala_steward", "test")),
          steps = NonEmptyList.of(Step.Uses("coursier/cache-action@v6")(), Step.Run("sbt ci-test"))
        )
      )
    )

    val printer = Printer(
      preserveOrder = true,
      dropNullKeys = true,
      indent = 2,
      maxScalarWidth = 80,
      splitLines = false,
      indicatorIndent = 0,
      tags = Map.empty,
      sequenceStyle = FlowStyle.Block,
      mappingStyle = FlowStyle.Block,
      stringStyle = StringStyle.Plain,
      lineBreak = LineBreak.Unix,
      explicitStart = false,
      explicitEnd = false,
      version = YamlVersion.Auto
    )

    val content = printer.pretty(workflow.asJson)

    val path = java.nio.file.Paths.get(s"${sys.props.getOrElse("user.dir", "")}/.github/workflows/test.yml")

    Files.writeString(path, content)
  }

}
