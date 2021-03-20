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

class CirceSuite extends munit.FunSuite {

  test("Create ci.yml workflow") {
    val test = workflowFile("test") {
      workflow
        .name("CI")
        .env("SOME_KEY", "my_value")
        .defaults(shell = customShell("perl {0}"), workingDirectory = "/user")
        .on(checkRun)
        .on(checkSuite)
        .on(create)
        .on(delete)
        .on(deployment)
        .on(deploymentStatus)
        .on(fork)
        .on(gollum)
        .on(issueComment)
        .on(issues)
        .on(label)
        .on(member)
        .on(milestone)
        .on(pageBuild)
        .on(project)
        .on(projectCard)
        .on(projectColumn)
        .on(public)
        .on(
          pullRequest
            .branches(matching("main", "releases/**"))
            .tags(matching("v**", "latest"))
            .paths(matching(".github/ci.yml", "README.md"))
        )
        .on(pullRequestReview)
        .on(pullRequestReviewComment)
        .on(pullRequestTarget)
        .on(
          push
            .branches(ignore("main", "releases/**"))
            .tags(ignore("v**", "latest"))
            .paths(ignore(".github/ci.yml", "README.md"))
        )
        .on(registryPackage)
        .on(release)
        .on(status)
        .on(watch)
        .on(
          workflowDispatch.inputs(
            input("repository")
              .description("The repository to use")
              .deprecationMessage("This input is deprecated, use repositories instead")
              .isRequired()
              .default(env.GITHUB_REPOSITORY),
            input("repositories")
              .description("Comma-separated list of repositories")
              .deprecationMessage("The list of repositories to use")
              .isRequired()
              .default(env.GITHUB_REPOSITORY)
          )
        )
        .on(
          workflowRun
            .onWorkflows("ci", "release")
            .branches(matching("main"))
        )
        .on(repositoryDispatch.types("some_event", "another_event"))
        .on(schedule(yearly, everydayAt(7)))
        .job("scala_steward") {
          _.name("Run `sbt fix generateCiFiles` in Scala Steward PRs")
            .runsOn(`ubuntu-20.04`)
            .environment("staging", "https://example.com")
            .env("TOKEN", secrets("MY_TOKEN"))
            .defaults(shell = Bash, workingDirectory = "/user/home")
            .continueOnError()
            .runsIf(
              is[PullRequest] && exp"github.event.pull_request.head.repo.full_name == github.repository" &&
                exp"github.event.pull_request.user.login == 'alejandrohdezma-steward[bot]'"
            )
            .output("changes_detected", fromStep("push"))
            .container(
              from("ubuntu:latest")
                .credentials(username = "alejandrohdezma", password = secrets("DOCKER_PASSWORD"))
                .env("USER_DIR", env("SOME_KEY"))
                .exposePorts(8080, 8090)
                .exposeVolume("/home/alex/mount:/dev/mount")
                .options("--silent")
            )
            .service("redis", container = from("redis:latest").exposePort(4004))
            .strategy(
              matrix
                .config("os")("ubuntu-16.04", "ubuntu-18.04")
                .config("node")("6", "8", "10")
                .include(("os", "ubuntu-16.04"), ("node", "8"), ("experimental", "true"))
                .exclude(("os", "ubuntu-18.04"), ("node", "6"))
                .failFast()
                .maxParallel(2)
            )
            .steps(
              uses("hmarr/auto-approve-action@v2")
                .withInput("github-token", secrets("ADMIN_GITHUB_TOKEN"))
                .name("Auto-approve Scala Steward PRs")
                .id("auto-approve")
                .runsIf(is[Push])
                .env("GITHUB_TOKEN", secrets.GITHUB_TOKEN)
                .continueOnError()
                .timeoutMinutes(3),
              run("""sbt generateCiFiles fix || sbt "generateCiFiles; scalafmtAll; scalafmtSbt" || true""")
                .name("Run generate")
                .id("run-generate")
                .runsIf(is[Push] || is[PullRequest])
                .env("ONE", "1")
                .env("TWO", "2")
                .continueOnErrorIf(is[PullRequest])
                .timeoutMinutes(7)
            )
        }
        .job("test") {
          _.runsOn(matrix("os"))
            .runsIf(is[PullRequest])
            .timeoutMinutes(10)
            .continueOnErrorIf(matrix("os") === "linux")
            .steps(
              uses("coursier/cache-action@v6"),
              run("sbt ci-test")
            )
        }
        .job("test-2") {
          _.runsOn(selfHosted.machine(Linux).architecture(x64).labels("miau", "meow"))
            .needs("scala_steward", "test")
            .steps(
              uses("coursier/cache-action@v6"),
              run("""sbt ci-test
                |sbt compile
                |echo "success"""")
            )
        }
    }

    val ci = workflowFile("ci") {
      workflow
        .name("CI")
        .on(
          push
            .branches(matching("main")),
          pullRequest
        )
        .job("update_release_draft") {
          _.name("Drafts/updates the next repository release and update PR labels")
            .runsIf(
              is[Push] || (is[PullRequest] && exp"github.event.pull_request.head.repo.full_name == github.repository")
            )
            .steps(
              uses("jnwng/github-app-installation-token-action@v2")
                .name("Get the GitHub App installation token")
                .id("installation_token")
                .withInput("appId", "104853")
                .withInput("installationId", "15336457")
                .withInput("privateKey", secrets("SCALA_STEWARD_APP_PRIVATE_KEY")),
              uses("release-drafter/release-drafter@v5")
                .env("GITHUB_TOKEN", exp"steps.installation_token.outputs.token")
            )
        }
        .job("update-prs") {
          _.name("Update outdated PRs to latest main")
            .runsIf(is[Push])
            .steps(
              uses("jnwng/github-app-installation-token-action@v2")
                .name("Get the GitHub App installation token")
                .id("installation_token")
                .withInput("appId", "104853")
                .withInput("installationId", "15336457")
                .withInput("privateKey", secrets("SCALA_STEWARD_APP_PRIVATE_KEY")),
              uses("adRise/update-pr-branch@v0.4.0")
                .name("Automatically update PR")
                .withInput("token", exp"steps.installation_token.outputs.token")
                .withInput("base", "main")
                .withInput("required_approval_count", "1")
            )
        }
        .job("scala-steward") {
          _.name("Process Scala Steward PRs: auto-approves, enable auto-merge...")
            .runsIf(
              is[PullRequest] && exp"github.event.pull_request.head.repo.full_name == github.repository" &&
                exp"github.event.pull_request.user.login == 'alejandrohdezma-steward[bot]'"
            )
            .output("changes_detected", exp"steps.push.outputs.changes_detected")
            .steps(
              uses("jnwng/github-app-installation-token-action@v2")
                .name("Get the GitHub App installation token")
                .id("installation_token")
                .withInput("appId", "104853")
                .withInput("installationId", "15336457")
                .withInput("privateKey", secrets("SCALA_STEWARD_APP_PRIVATE_KEY")),
              uses("actions/checkout@v2.3.4")
                .name("Checkout project")
                .withInput("token", exp"steps.installation_token.outputs.token")
                .withInput("ref", exp"github.event.pull_request.head.ref"),
              uses("coursier/cache-action@v6")
                .name("Enable Coursier cache"),
              uses("laughedelic/coursier-setup@v1")
                .name("Setup Coursier")
                .withInput("jvm", "adopt:1.11")
                .withInput("apps", "sbt"),
              uses("andstor/file-existence-action@v1")
                .name("Check if `.github/auto_assign.yml` exists")
                .id("check_files")
                .withInput("files", ".github/auto_assign.yml"),
              uses("kentaro-m/auto-assign-action@v1.1.2")
                .runsIf(exp"steps.check_files.outputs.files_exists == 'true'")
                .name("Add Pull Request Reviewer")
                .withInput("repo-token", exp"steps.installation_token.outputs.token"),
              uses("alexwilson/enable-github-automerge-action@main")
                .name("Enable auto-merge for this PR")
                .withInput("github-token", exp"secrets.ADMIN_GITHUB_TOKEN")
                .withInput("merge-method", "MERGE"),
              uses("hmarr/auto-approve-action@v2")
                .name("Auto-approve Scala Steward PRs")
                .withInput("github-token", secrets("ADMIN_GITHUB_TOKEN")),
              run("""sbt generateCiFiles fix || sbt "generateCiFiles; scalafmtAll; scalafmtSbt" || true"""),
              uses("stefanzweifel/git-auto-commit-action@v4.9.2")
                .id("push")
                .name("Push changes")
                .withInput("commit_message", "Regenerate files with `sbt generateCiFiles fix`")
            )
        }
        .job("test") {
          _.needs("scala-steward")
            .runsIf(always && is[PullRequest] && exp"needs.scala-steward.outputs.changes_detected != 'true'")
            .name("""Run "sbt ci-test" on JDK {{ matrix.jdk }}""")
            .strategy(
              matrix
                .config("jdk")("adopt:1.8", "adopt:1.11", "adopt:1.15")
                .doNotFailFast
            )
            .steps(
              uses("actions/checkout@v2.3.4")
                .name("Checkout project"),
              uses("coursier/cache-action@v6")
                .name("Enable Coursier cache"),
              uses("laughedelic/coursier-setup@v1")
                .name("Setup Coursier")
                .withInput("jvm", matrix("jdk"))
                .withInput("apps", "sbt"),
              run("sbt ci-test")
                .name("Run checks (non-fork)")
                .runsIf(exp"github.event.pull_request.head.repo.full_name == github.repository")
                env ("GITHUB_TOKEN", secrets("ADMIN_GITHUB_TOKEN")),
              run("sbt ci-test")
                .name("Run checks (fork)")
                .runsIf(exp"github.event.pull_request.head.repo.full_name != github.repository")
                env ("GITHUB_TOKEN", secrets("GITHUB_TOKEN"))
            )
        }
    }

    import com.softwaremill.quicklens._

    val workflowFiles = List(test, ci)

    val newWorkflowFiles = workflowFiles.modify(_.eachWhere(_.fileName.is("test")).workflow) {
      _.disableDefaults()
        .modify(_.env)(_ - "SOME_KEY")
        .modify(_.on.value.each.when[PullRequest])(_.onLabeled().onAssigned())
        .modify(_.jobs.value.eachWhere(_.id.is("scala_steward"))) {
          _.disableEnvironment().modify(_.steps.value.each.eachRight.id.at.value)(_.toUpperCase())
        }
    }

    val yamlFiles = newWorkflowFiles.map(file => file.fileName -> file.workflow.asYaml)

    val newYamlFiles = yamlFiles.modify {
      _.eachWhere(_._1.is("test"))._2
        .when[Yaml.Object]
        .value
        .index("jobs")
        .when[Yaml.Object]
        .value
        .index("scala_steward")
        .when[Yaml.Object]
        .value
        .index("runs-on")
        .when[Yaml.Scalar]
        .value
    }.setTo("ubuntu-latest")

    newYamlFiles.foreach { case (name, yaml) =>
      java.nio.file.Files.write(
        java.nio.file.Paths.get(s"${sys.props("user.dir")}/.github/workflows/${name.value}.yml"), // scalafix:ok
        yaml.prettyPrint().getBytes()
      )
    }
  }

}
