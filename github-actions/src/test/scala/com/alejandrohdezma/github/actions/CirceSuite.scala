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

import cats.data.NonEmptyList
import cats.data.NonEmptyMap

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.cats._
import io.circe.Encoder
import io.circe.yaml.Printer
import io.circe.yaml.Printer._

class CirceSuite extends munit.FunSuite {

  test("Create ci.yml workflow") {
    val ci = workflow
      .name("CI")
      .env("SOME_KEY", "my_value")
      .defaults(shell = Shell.Custom("perl {0}"), workingDirectory = "/user")
      .on(CheckRun())
      .on(CheckSuite())
      .on(Create)
      .on(Delete)
      .on(Deployment)
      .on(DeploymentStatus)
      .on(Fork)
      .on(Gollum)
      .on(IssueComment())
      .on(Issues())
      .on(Label())
      .on(Member())
      .on(Milestone())
      .on(PageBuild)
      .on(Project())
      .on(ProjectCard())
      .on(ProjectColumn())
      .on(Public)
      .on(
        PullRequest(
          branches = Some(Branches.matching("main", "releases/**")),
          tags = Some(Tags.matching("v**", "latest")),
          paths = Some(Paths.matching(".github/ci.yml", "README.md"))
        )
      )
      .on(PullRequestReview())
      .on(PullRequestReviewComment())
      .on(PullRequestTarget())
      .on(
        Push(
          branches = Some(Branches.ignore("main", "releases/**")),
          tags = Some(Tags.ignore("v**", "latest")),
          paths = Some(Paths.ignore(".github/ci.yml", "README.md"))
        )
      )
      .on(RegistryPackage())
      .on(Release())
      .on(Status)
      .on(Watch)
      .on(
        WorkflowDispatch(
          Input(
            name = "repository",
            description = Some("The repository to use"),
            deprecationMessage = Some("This input is deprecated, use repositories instead"),
            required = false,
            default = Some(exp"env.GITHUB_REPOSITORY".value)
          ),
          Input(
            name = "repositories",
            description = Some("Comma-separated list of repositories"),
            deprecationMessage = Some("The list of repositories to use"),
            required = false,
            default = Some(exp"env.GITHUB_REPOSITORY".value)
          )
        )
      )
      .on(
        WorkflowRun(
          workflows = NonEmptyList.of("ci", "release"),
          branches = Some(Branches.matching("main"))
        )
      )
      .on(RepositoryDispatch(Some(NonEmptyList.of("some_event", "another_event"))))
      .on(Schedule(NonEmptyList.of(yearly, everydayAt(7))))
      .job(
        Job(
          "scala_steward",
          name = Some("Run `sbt fix generateCiFiles` in Scala Steward PRs"),
          environment = Some(Environment("staging", Some("https://example.com"))),
          env = Some(NonEmptyMap.of(("TOKEN", exp"secrets.MY_TOKEN".value))),
          defaults = Some(Defaults(Some(Shell.Bash), Some("/user/home"))),
          continueOnError = Some(Left(false)),
          `if` = Some(
            is[PullRequest] `&&` exp"github.event.pull_request.head.repo.full_name == github.repository" `&&`
              exp"github.event.pull_request.user.login == 'alejandrohdezma-steward[bot]'"
          ),
          outputs = Some(NonEmptyMap.of(("changes_detected", exp"steps.push.outputs.changes_detected".value))),
          container = Some(
            Container(
              image = "ubuntu:latest",
              credentials = Some(Container.Credentials("alejandrohdezma", exp"secrets.DOCKER_PASSWORD".value)),
              env = Some(NonEmptyMap.of(("USER_DIR", "/home/alex"))),
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
            matrix
              .config("os")("ubuntu-16.04", "ubuntu-18.04")
              .config("node")("6", "8", "10")
              .include(("os", "ubuntu-16.04"), ("node", "8"), ("experimental", "true"))
              .exclude(("os", "ubuntu-18.04"), ("node", "6"))
              .failFast()
              .maxParallel(2)
          ),
          steps = NonEmptyList.of(
            Step.Uses(
              "hmarr/auto-approve-action@v2",
              `with` = Some(NonEmptyMap.of("github-token" -> exp"secrets.ADMIN_GITHUB_TOKEN".value)),
              name = Some("Auto-approve Scala Steward PRs"),
              id = Some("auto-approve"),
              `if` = Some(is[Push]),
              env = Some(NonEmptyMap.of("GITHUB_TOKEN" -> exp"secrets.GITHUB_TOKEN".value)),
              continueOnError = Some(Left(true)),
              timeoutMinutes = Some(3)
            ),
            Step.Run(
              "sbt generateCiFiles fix || sbt \"generateCiFiles; scalafmtAll; scalafmtSbt\" || true",
              name = Some("Run generate"),
              id = Some("run-generate"),
              `if` = Some(is[Push] `||` is[PullRequest]),
              env = Some(NonEmptyMap.of("ONE" -> "1", "TWO" -> "2")),
              continueOnError = Some(Right(is[PullRequest])),
              timeoutMinutes = Some(7)
            )
          )
        )
      )
      .job(
        Job(
          "test",
          runsOn = Runner.FromExpression(exp"matrix.os"),
          `if` = Some(is[PullRequest]),
          timeoutMinutes = Some(10),
          continueOnError = Some(Right(exp"matrix.os == 'linux'")),
          steps = NonEmptyList.of(Step.Uses("coursier/cache-action@v6"), Step.Run("sbt ci-test"))
        )
      )
      .job(
        Job(
          "test-2",
          runsOn = Runner.selfHosted(Some(Machine.Linux), Some(x64), List("miau", "meow")),
          needs = Some(NonEmptyList.of("scala_steward", "test")),
          steps = NonEmptyList.of(Step.Uses("coursier/cache-action@v6"), Step.Run("sbt ci-test"))
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

    val content = printer.pretty(Encoder[Workflow].apply(ci))

    val path = java.nio.file.Paths.get(s"${sys.props.getOrElse("user.dir", "")}/.github/workflows/test.yml")

    Files.write(path, content.getBytes())
  }

}
