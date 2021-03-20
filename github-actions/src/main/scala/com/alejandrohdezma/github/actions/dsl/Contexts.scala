package com.alejandrohdezma.github.actions.dsl

import scala.language.dynamics

import com.alejandrohdezma.github.actions.Matrix
import com.alejandrohdezma.github.actions.Strategy
import com.alejandrohdezma.github.actions.base.Expression
import com.alejandrohdezma.github.actions.base.NonEmptyList
import com.alejandrohdezma.github.actions.base.NonEmptyString

/** Contains all the available contexts in a GitHub Action. Contexts are a way to access information about workflow
  * runs, runner environments, jobs, and steps.
  */
trait Contexts {

  /** Contains environment variables that have been set in a workflow, job, or step.
    *
    * The env context syntax allows you to use the value of an environment variable in your workflow file. You can use
    * the env context in the value of any key in a step except for the id and uses keys.
    *
    * You can either use one of the pre-defined ones or one created by you using the "dot" syntax.
    *
    * @example {{{
    * env.CI // Available by GitHub Actions
    *
    * env.MY_KEY // Needs to be populated by the user
    * }}}
    * @see [[https://docs.github.com/en/actions/reference/environment-variables]]
    * @see [[https://docs.github.com/en/actions/reference/context-and-expression-syntax-for-github-actions#env-context]]
    */

  object env extends Dynamic {

    /** @see [[https://www.scala-lang.org/api/current/scala/Dynamic.html]] */
    def selectDynamic(name: String): Expression = Expression.Constant(NonEmptyString.unsafe(s"env.$name"))

    /** Always set to true. */
    lazy val CI: Expression = Expression.Constant(NonEmptyString.unsafe(s"env.CI"))

    /** The name of the workflow. */
    lazy val GITHUB_WORKFLOW: Expression = Expression.Constant(NonEmptyString.unsafe(s"env.GITHUB_WORKFLOW"))

    /** A unique number for each run within a repository. This number does not change if you re-run the workflow run. */
    lazy val GITHUB_RUN_ID: Expression = Expression.Constant(NonEmptyString.unsafe(s"env.GITHUB_RUN_ID"))

    /** A unique number for each run of a particular workflow in a repository. This number begins at 1 for the
      * workflow's first run, and increments with each new run. This number does not change if you re-run the workflow
      * run.
      */
    lazy val GITHUB_RUN_NUMBER: Expression = Expression.Constant(NonEmptyString.unsafe(s"env.GITHUB_RUN_NUMBER"))

    /** The job_id of the current job. */
    lazy val GITHUB_JOB: Expression = Expression.Constant(NonEmptyString.unsafe(s"env.GITHUB_JOB"))

    /** The unique identifier (id) of the action. */
    lazy val GITHUB_ACTION: Expression = Expression.Constant(NonEmptyString.unsafe(s"env.GITHUB_ACTION"))

    /** Always set to true when GitHub Actions is running the workflow. You can use this variable to differentiate when
      * tests are being run locally or by GitHub Actions.
      */
    lazy val GITHUB_ACTIONS: Expression = Expression.Constant(NonEmptyString.unsafe(s"env.GITHUB_ACTIONS"))

    /** The name of the person or app that initiated the workflow. For example, octocat. */
    lazy val GITHUB_ACTOR: Expression = Expression.Constant(NonEmptyString.unsafe(s"env.GITHUB_ACTOR"))

    /** The owner and repository name. For example, octocat/Hello-World. */
    lazy val GITHUB_REPOSITORY: Expression = Expression.Constant(NonEmptyString.unsafe(s"env.GITHUB_REPOSITORY"))

    /** The name of the webhook event that triggered the workflow. */
    lazy val GITHUB_EVENT_NAME: Expression = Expression.Constant(NonEmptyString.unsafe(s"env.GITHUB_EVENT_NAME"))

    /** The path of the file with the complete webhook event payload. For example, /github/workflow/event.json. */
    lazy val GITHUB_EVENT_PATH: Expression = Expression.Constant(NonEmptyString.unsafe(s"env.GITHUB_EVENT_PATH"))

    /** The GitHub workspace directory path. The workspace directory is a copy of your repository if your workflow uses
      * the actions/checkout action. If you don't use the actions/checkout action, the directory will be empty. For
      * example, /home/runner/work/my-repo-name/my-repo-name.
      */
    lazy val GITHUB_WORKSPACE: Expression = Expression.Constant(NonEmptyString.unsafe(s"env.GITHUB_WORKSPACE"))

    /** The commit SHA that triggered the workflow. For example, ffac537e6cbbf934b08745a378932722df287a53. */
    lazy val GITHUB_SHA: Expression = Expression.Constant(NonEmptyString.unsafe(s"env.GITHUB_SHA"))

    /** The branch or tag ref that triggered the workflow. For example, refs/heads/feature-branch-1. If neither a branch
      * or tag is available for the event type, the variable will not exist.
      */
    lazy val GITHUB_REF: Expression = Expression.Constant(NonEmptyString.unsafe(s"env.GITHUB_REF"))

    /** Only set for pull request events. The name of the head branch. */
    lazy val GITHUB_HEAD_REF: Expression = Expression.Constant(NonEmptyString.unsafe(s"env.GITHUB_HEAD_REF"))

    /** Only set for pull request events. The name of the base branch. */
    lazy val GITHUB_BASE_REF: Expression = Expression.Constant(NonEmptyString.unsafe(s"env.GITHUB_BASE_REF"))

    /** Returns the URL of the GitHub server. For example: https://github.com. */
    lazy val GITHUB_SERVER_URL: Expression = Expression.Constant(NonEmptyString.unsafe(s"env.GITHUB_SERVER_URL"))

    /** Returns the API URL. For example: https://api.github.com. */
    lazy val GITHUB_API_URL: Expression = Expression.Constant(NonEmptyString.unsafe(s"env.GITHUB_API_URL"))

    /** Returns the GraphQL API URL. For example: https://api.github.com/graphql. */
    lazy val GITHUB_GRAPHQL_URL: Expression = Expression.Constant(NonEmptyString.unsafe(s"env.GITHUB_GRAPHQL_URL"))

  }

  /** Contains information about the currently running job.
    *
    * @example {{{job.container.id}}}
    * @see [[https://docs.github.com/en/actions/reference/context-and-expression-syntax-for-github-actions#job-context]]
    */
  object job {

    /** Information about the job's container. */
    object container {

      /** The id of the container. */
      lazy val id = Expression.Constant(NonEmptyString.unsafe("job.container.id"))

      /** The id of the container network. The runner creates the network used by all containers in a job. */
      lazy val network = Expression.Constant(NonEmptyString.unsafe("job.container.id"))

    }

    /** The service containers created for a job.
      *
      * @example {{{job.services.my_service.id}}}
      */
    object services extends Dynamic {

      /** Returns the information of a specific service.
        *
        * @example {{{job.services.my_service}}}
        * @see [[https://www.scala-lang.org/api/current/scala/Dynamic.html]]
        */
      def selectDynamic(name: String): JobInformation = JobInformation(
        Expression.Constant(NonEmptyString.unsafe(s"job.services.$name.id")),
        Expression.Constant(NonEmptyString.unsafe(s"job.services.$name.network")),
        Expression.Constant(NonEmptyString.unsafe(s"job.services.$name.ports"))
      )

      /** The job's information.
        *
        * @param id The id of the service container.
        * @param network The id of the service container network. The runner creates the network used by all containers
        *   in a job.
        * @param ports The id of the service container network. The runner creates the network used by all containers
        *   in a job.
        */
      case class JobInformation(id: Expression, network: Expression, ports: Expression)

    }

    /** The current status of the job. Possible values are success, failure, or cancelled. */
    lazy val status: Expression = Expression.Constant(NonEmptyString.unsafe("job.status"))

  }

  /** Information about the steps that have been run in a job
    *
    * @example {{{steps.build.outputs.result}}}
    * @see [[https://docs.github.com/en/actions/reference/context-and-expression-syntax-for-github-actions#steps-context]]
    */
  object steps extends Dynamic {

    /** Returns the information of a specific step.
      *
      * @see [[https://www.scala-lang.org/api/current/scala/Dynamic.html]]
      */
    def selectDynamic(name: String): StepInformation = StepInformation(name)(
      Expression.Constant(NonEmptyString.unsafe(s"steps.$name.conclusion")),
      Expression.Constant(NonEmptyString.unsafe(s"steps.$name.outcome")),
      StepOutputs(name)
    )

    /** Contains the outputs available for the step.
      *
      * @example {{{steps.build.outputs.result}}}
      */
    case class StepOutputs(private val id: String) extends Dynamic {

      /** Returns the value of a specific step output.
        *
        * @example {{{steps.build.outputs.result}}}
        * @see [[https://www.scala-lang.org/api/current/scala/Dynamic.html]]
        */
      def selectDynamic(name: String) =
        Expression.Constant(NonEmptyString.unsafe(s"steps.$id.outputs.$name"))

    }

    /** Contains the available information for the step.
      *
      * @param conclusion The result of a completed step after continue-on-error is applied. Possible values are
      *   success, failure, cancelled, or skipped. When a continue-on-error step fails, the outcome is failure, but the
      *   final conclusion is success.
      * @param outcome The result of a completed step before continue-on-error is applied. Possible values are success,
      *   failure, cancelled, or skipped. When a continue-on-error step fails, the outcome is failure, but the final
      *   conclusion is success.
      * @param outputs The step's outputs.
      */
    case class StepInformation(private val id: String)(
        val conclusion: Expression,
        val outcome: Expression,
        val outputs: StepOutputs
    )

  }

  /** Contains information about the runner that is executing the current job.
    *
    * @example {{{runner.os}}}
    */
  object runner {

    /** The operating system of the runner executing the job. Possible values are Linux, Windows, or macOS. */
    lazy val os = Expression.Constant(NonEmptyString.unsafe("runner.os"))

    /** The path of the temporary directory for the runner. This directory is guaranteed to be empty at the start of
      * each job, even on self-hosted runners.
      */
    lazy val temp = Expression.Constant(NonEmptyString.unsafe("runner.temp"))

    /** The path of the directory containing some of the preinstalled tools for GitHub-hosted runners.
      *
      * @see [[https://docs.github.com/en/actions/using-github-hosted-runners/about-github-hosted-runners#supported-software]]
      */
    lazy val tool_cache = Expression.Constant(NonEmptyString.unsafe("runner.tool_cache"))

  }

  /** Contains information about the workflow run and the event that triggered the run. You can also read most of the
    * github context data as [[env environment variables]].
    */

  object github {

    /** The name of the action currently running. GitHub removes special characters or uses the name run when the
      * current step runs a script. If you use the same action more than once in the same job, the name will include a
      * suffix with the sequence number. For example, the first script you run will have the name run1, and the second
      * script will be named run2. Similarly, the second invocation of actions/checkout will be actionscheckout2.
      */
    lazy val action = Expression.Constant(NonEmptyString.unsafe("github.action"))

    /** The path where your action is located. You can use this path to easily access files located in the same
      * repository as your action. This attribute is only supported in composite run steps actions.
      */
    lazy val action_path = Expression.Constant(NonEmptyString.unsafe("github.action_path"))

    /** The login of the user that initiated the workflow run. */
    lazy val actor = Expression.Constant(NonEmptyString.unsafe("github.actor"))

    /** The base_ref or target branch of the pull request in a workflow run. This property is only available when the
      * event that triggers a workflow run is a pull_request.
      */
    lazy val base_ref = Expression.Constant(NonEmptyString.unsafe("github.base_ref"))

    /** The full event webhook payload. You can access individual properties of the event using this context.
      *
      * @example {{{github.event.pull_request.head.repo.full_name}}}
      * @see [[https://docs.github.com/en/articles/events-that-trigger-workflows]]
      */
    lazy val event = Expression.InfiniteDotExpression(NonEmptyString.unsafe("github.event"))

    /** The name of the event that triggered the workflow run. */
    lazy val event_name = Expression.Constant(NonEmptyString.unsafe("github.event_name"))

    /** The path to the full event webhook payload on the runner. */
    lazy val event_path = Expression.Constant(NonEmptyString.unsafe("github.event_path"))

    /** The head_ref or source branch of the pull request in a workflow run. This property is only available when the
      * event that triggers a workflow run is a pull_request.
      */
    lazy val head_ref = Expression.Constant(NonEmptyString.unsafe("github.head_ref"))

    /** The [[https://docs.github.com/en/actions/reference/workflow-syntax-for-github-actions#jobsjob_id job_id]] of the
      * current job.
      */
    lazy val job = Expression.Constant(NonEmptyString.unsafe("github.job"))

    /** The branch or tag ref that triggered the workflow run. For branches this in the format refs/heads/<branch_name>,
      * and for tags it is refs/tags/<tag_name>.
      */
    lazy val ref = Expression.Constant(NonEmptyString.unsafe("github.ref"))

    /** The owner and repository name. For example, Codertocat/Hello-World. */
    lazy val repository = Expression.Constant(NonEmptyString.unsafe("github.repository"))

    /** The repository owner's name. For example, Codertocat. */
    lazy val repository_owner = Expression.Constant(NonEmptyString.unsafe("github.repository_owner"))

    /** A unique number for each run within a repository. This number does not change if you re-run the workflow run. */
    lazy val run_id = Expression.Constant(NonEmptyString.unsafe("github.run_id"))

    /** A unique number for each run of a particular workflow in a repository. This number begins at 1 for the
      * workflow's first run, and increments with each new run. This number does not change if you re-run the workflow
      * run.
      */
    lazy val run_number = Expression.Constant(NonEmptyString.unsafe("github.run_number"))

    /** The commit SHA that triggered the workflow run. */
    lazy val sha = Expression.Constant(NonEmptyString.unsafe("github.sha"))

    /** A token to authenticate on behalf of the GitHub App installed on your repository. This is functionally
      * equivalent to the [[secrets.GITHUB_TOKEN GITHUB_TOKEN secret]].
      *
      * @see [[https://docs.github.com/en/actions/automating-your-workflow-with-github-actions/authenticating-with-the-github_token]]
      */
    lazy val token = Expression.Constant(NonEmptyString.unsafe("github.token"))

    /** The name of the workflow. If the workflow file doesn't specify a name, the value of this property is the full
      * path of the workflow file in the repository.
      */
    lazy val workflow = Expression.Constant(NonEmptyString.unsafe("github.workflow"))

    /** The default working directory for steps and the default location of your repository when using the
      * [[https://github.com/actions/checkout checkout]] action.
      */
    lazy val workspace = Expression.Constant(NonEmptyString.unsafe("github.workspace"))

  }

  /** Contains outputs from all jobs that are defined as a dependency of the current job.
    *
    * @see [[https://docs.github.com/en/actions/reference/workflow-syntax-for-github-actions#jobsjob_idneeds]]
    */
  object needs extends Dynamic {

    /** Allows selecting a job using "dot syntax".
      *
      * @example {{{needs.build}}}
      * @param name The name of the job.
      * @return The job information available for this context.
      * @see [[https://www.scala-lang.org/api/current/scala/Dynamic.html]]
      */
    def selectDynamic(name: String) = NeedsJob(name)(
      Expression.Constant(NonEmptyString.unsafe(s"needs.$name.result")),
      JobOutputs(name)
    )

    /** Contains the set of outputs of a job that the current job depends on.
      *
      * Allows accesing them using "dot-syntax".
      *
      * @example {{{needs.build.outputs.result}}}
      */
    case class JobOutputs(private val id: String) extends Dynamic {

      /** Returns a job's output.
        *
        * @example {{{needs.build.outputs.result}}}
        * @param name The name of the output.
        * @return The job output.
        * @see [[https://www.scala-lang.org/api/current/scala/Dynamic.html]]
        */
      def selectDynamic(name: String) = Expression.Constant(NonEmptyString.unsafe(s"needs.$id.outputs.$name"))

    }

    /** Information about a job for the [[needs]] context. Contains information about the job's result and its outputs.
      *
      * @param result The result of a job that the current job depends on. Possible values are `success`, `failure`,
      *    `cancelled`, or `skipped`.
      * @param outputs The set of outputs of a job that the current job depends on.
      */
    case class NeedsJob(private val id: String)(val result: Expression, val outputs: JobOutputs)

  }

  /** Enables access to secrets.
    *
    * Allows accessing secrets using "dot-syntax".
    *
    * It also allows direct access to [[secrets.GITHUB_TOKEN]].
    *
    * @see [[https://docs.github.com/en/actions/automating-your-workflow-with-github-actions/creating-and-using-encrypted-secrets]]
    */

  object secrets extends Dynamic {

    /** Returns a secret.
      *
      * @example {{{secrets.MY_GITHUB_TOKEN}}}
      * @param name The name of the secret.
      * @return A workflow secret.
      * @see [[https://www.scala-lang.org/api/current/scala/Dynamic.html]]
      */
    def selectDynamic(name: String): Expression = Expression.Constant(NonEmptyString.unsafe("secrets." + name))

    /** GitHub automatically creates a GITHUB_TOKEN secret to use in your workflow. You can use the GITHUB_TOKEN to
      * authenticate in a workflow run.
      *
      * | Permission          | Access type | Access by forked repos |
      * |:--------------------|:-----------:|:----------------------:|
      * | actions             | read/write  |          read          |
      * | checks              | read/write  |          read          |
      * | contents            | read/write  |          read          |
      * | deployments         | read/write  |          read          |
      * | issues              | read/write  |          read          |
      * | metadata            |    read     |          read          |
      * | packages            | read/write  |          read          |
      * | pull requests       | read/write  |          read          |
      * | repository projects | read/write  |          read          |
      * | statuses            | read/write  |          read          |
      *
      * The token is also available in the github.token context.
      */
    lazy val GITHUB_TOKEN: Expression = Expression.Constant(NonEmptyString.unsafe("secrets.GITHUB_TOKEN"))

  }

  /** Enables access to the matrix parameters you configured for the current job. For example, if you configure a matrix
    * build with the os and node versions, the matrix context object includes the os and node versions of the current
    * job.
    *
    * It also contains the [[matrix.config]] smart constructor for defining a matrix-strategy. Although this is not a
    * "context" it is included here so the DSL is not broken.
    *
    * @example {{{
    * .job("build") {
    *   _.name(s"Run \"sbt ci-test\" on JDK \${matrix.jdk}")
    *    .strategy(
    *       matrix
    *         .config("jdk")("adopt:1.8", "adopt:1.11", "adopt:1.15")
    *         .doNotFailFast
    *    )
    * }}}
    */
  object matrix extends Dynamic {

    /** Creates a matrix strategy with the provided key and values.
      *
      * @param key The name of the matrix configuration.
      * @param first The first value of the configuration.
      * @param rest The other values of the configuration.
      * @example {{{matrix.config("jdk")("adopt:1.8", "adopt:1.11", "adopt:1.15")}}}
      */
    def config(key: NonEmptyString)(first: NonEmptyString, rest: NonEmptyString*): Strategy.FromMatrix =
      Strategy.FromMatrix(Matrix(NonEmptyList.of(Matrix.Configuration(key, NonEmptyList.of(first, rest: _*)))))

    /** Returns a matrix configuration value.
      *
      * @example {{{matrix.jdk}}}
      * @param name The name of the matrix parameter.
      * @return A matrix configuration value.
      * @see [[https://www.scala-lang.org/api/current/scala/Dynamic.html]]
      */
    def selectDynamic(name: String): Expression = Expression.Constant(NonEmptyString.unsafe("matrix." + name))

  }

}
