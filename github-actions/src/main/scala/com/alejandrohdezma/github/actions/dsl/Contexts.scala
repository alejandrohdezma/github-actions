package com.alejandrohdezma.github.actions.dsl

import scala.language.dynamics

import com.alejandrohdezma.github.actions.base.Expression
import com.alejandrohdezma.github.actions.base.NotEmptyString

trait Contexts {

  /** Environment variables */

  object env extends Dynamic {

    def selectDynamic(name: String): Expression = Expression.Constant(NotEmptyString.unsafe(s"env.$name"))

    /**
     * Always set to true.
     */
    lazy val CI: Expression = Expression.Constant(NotEmptyString.unsafe(s"env.CI"))

    /**
     * The name of the workflow.
     */
    lazy val GITHUB_WORKFLOW: Expression = Expression.Constant(NotEmptyString.unsafe(s"env.GITHUB_WORKFLOW"))

    /**
     * A unique number for each run within a repository. This number does not change if you re-run the workflow run.
     */
    lazy val GITHUB_RUN_ID: Expression = Expression.Constant(NotEmptyString.unsafe(s"env.GITHUB_RUN_ID"))

    /**
     * A unique number for each run of a particular workflow in a repository. This number begins at 1 for the workflow's first run, and increments with each new run. This number does not change if you re-run the workflow run.
     */
    lazy val GITHUB_RUN_NUMBER: Expression = Expression.Constant(NotEmptyString.unsafe(s"env.GITHUB_RUN_NUMBER"))

    /**
     * The job_id of the current job.
     */
    lazy val GITHUB_JOB: Expression = Expression.Constant(NotEmptyString.unsafe(s"env.GITHUB_JOB"))

    /**
     * The unique identifier (id) of the action.
     */
    lazy val GITHUB_ACTION: Expression = Expression.Constant(NotEmptyString.unsafe(s"env.GITHUB_ACTION"))

    /**
     * Always set to true when GitHub Actions is running the workflow. You can use this variable to differentiate when tests are being run locally or by GitHub Actions.
     */
    lazy val GITHUB_ACTIONS: Expression = Expression.Constant(NotEmptyString.unsafe(s"env.GITHUB_ACTIONS"))

    /**
     * The name of the person or app that initiated the workflow. For example, octocat.
     */
    lazy val GITHUB_ACTOR: Expression = Expression.Constant(NotEmptyString.unsafe(s"env.GITHUB_ACTOR"))

    /**
     * The owner and repository name. For example, octocat/Hello-World.
     */
    lazy val GITHUB_REPOSITORY: Expression = Expression.Constant(NotEmptyString.unsafe(s"env.GITHUB_REPOSITORY"))

    /**
     * The name of the webhook event that triggered the workflow.
     */
    lazy val GITHUB_EVENT_NAME: Expression = Expression.Constant(NotEmptyString.unsafe(s"env.GITHUB_EVENT_NAME"))

    /**
     * The path of the file with the complete webhook event payload. For example, /github/workflow/event.json.
     */
    lazy val GITHUB_EVENT_PATH: Expression = Expression.Constant(NotEmptyString.unsafe(s"env.GITHUB_EVENT_PATH"))

    /**
     * The GitHub workspace directory path. The workspace directory is a copy of your repository if your workflow uses the actions/checkout action. If you don't use the actions/checkout action, the directory will be empty. For example, /home/runner/work/my-repo-name/my-repo-name.
     */
    lazy val GITHUB_WORKSPACE: Expression = Expression.Constant(NotEmptyString.unsafe(s"env.GITHUB_WORKSPACE"))

    /**
     * The commit SHA that triggered the workflow. For example, ffac537e6cbbf934b08745a378932722df287a53.
     */
    lazy val GITHUB_SHA: Expression = Expression.Constant(NotEmptyString.unsafe(s"env.GITHUB_SHA"))

    /**
     * The branch or tag ref that triggered the workflow. For example, refs/heads/feature-branch-1. If neither a branch or tag is available for the event type, the variable will not exist.
     */
    lazy val GITHUB_REF: Expression = Expression.Constant(NotEmptyString.unsafe(s"env.GITHUB_REF"))

    /**
     * Only set for pull request events. The name of the head branch.
     */
    lazy val GITHUB_HEAD_REF: Expression = Expression.Constant(NotEmptyString.unsafe(s"env.GITHUB_HEAD_REF"))

    /**
     * Only set for pull request events. The name of the base branch.
     */
    lazy val GITHUB_BASE_REF: Expression = Expression.Constant(NotEmptyString.unsafe(s"env.GITHUB_BASE_REF"))

    /**
     * Returns the URL of the GitHub server. For example: https://github.com.
     */
    lazy val GITHUB_SERVER_URL: Expression = Expression.Constant(NotEmptyString.unsafe(s"env.GITHUB_SERVER_URL"))

    /**
     * Returns the API URL. For example: https://api.github.com.
     */
    lazy val GITHUB_API_URL: Expression = Expression.Constant(NotEmptyString.unsafe(s"env.GITHUB_API_URL"))

    /**
     * Returns the GraphQL API URL. For example: https://api.github.com/graphql.
     */
    lazy val GITHUB_GRAPHQL_URL: Expression = Expression.Constant(NotEmptyString.unsafe(s"env.GITHUB_GRAPHQL_URL"))

  }

  /** Jobs */

  object job {

    object container {

      lazy val id = Expression.Constant(NotEmptyString.unsafe("job.container.id"))

      lazy val network = Expression.Constant(NotEmptyString.unsafe("job.container.id"))

    }

    object services extends Dynamic {

      def selectDynamic(name: String): JobServiceDsl = JobServiceDsl(
        Expression.Constant(NotEmptyString.unsafe(s"job.services.$name.id")),
        Expression.Constant(NotEmptyString.unsafe(s"job.services.$name.network")),
        Expression.Constant(NotEmptyString.unsafe(s"job.services.$name.ports"))
      )

      case class JobServiceDsl(id: Expression, network: Expression, ports: Expression)

    }

    lazy val status: Expression = Expression.Constant(NotEmptyString.unsafe("job.status"))

  }

  /** Steps */

  object steps extends Dynamic {

    def selectDynamic(name: String): StepsDsl = StepsDsl(name)(
      Expression.Constant(NotEmptyString.unsafe(s"steps.$name.conclusion")),
      Expression.Constant(NotEmptyString.unsafe(s"steps.$name.outcome")),
      OutputsDsl(name)
    )

    case class OutputsDsl(private val id: String) extends Dynamic {

      def selectDynamic(name: String) = Expression.Constant(NotEmptyString.unsafe(s"steps.$id.outputs.$name"))

    }

    case class StepsDsl(private val id: String)(
        val conclusion: Expression,
        val outcome: Expression,
        val outputs: OutputsDsl
    )

  }

  /** Runner */

  object runner {

    lazy val os = Expression.Constant(NotEmptyString.unsafe("runner.os"))

    lazy val temp = Expression.Constant(NotEmptyString.unsafe("runner.temp"))

    lazy val tool_cache = Expression.Constant(NotEmptyString.unsafe("runner.tool_cache"))

  }

  /** Github */

  object github {

    /**
     *  The name of the action currently running. GitHub removes special characters or uses the name run when the current step runs a script. If you use the same action more than once in the same job, the name will include a suffix with the sequence number. For example, the first script you run will have the name run1, and the second script will be named run2. Similarly, the second invocation of actions/checkout will be actionscheckout2.
     */
    lazy val action = Expression.Constant(NotEmptyString.unsafe("github.action"))

    /**
     *  The path where your action is located. You can use this path to easily access files located in the same repository as your action. This attribute is only supported in composite run steps actions.
     */
    lazy val action_path = Expression.Constant(NotEmptyString.unsafe("github.action_path"))

    /**
     *  The login of the user that initiated the workflow run.
     */
    lazy val actor = Expression.Constant(NotEmptyString.unsafe("github.actor"))

    /**
     *  The base_ref or target branch of the pull request in a workflow run. This property is only available when the event that triggers a workflow run is a pull_request.
     */
    lazy val base_ref = Expression.Constant(NotEmptyString.unsafe("github.base_ref"))

    /**
     * The full event webhook payload. For more information, see "Events that trigger workflows." You can access individual properties of the event using this context.
     */
    lazy val event = Expression.ExtendableExpression(NotEmptyString.unsafe("github.event"))

    /**
     *  The name of the event that triggered the workflow run.
     */
    lazy val event_name = Expression.Constant(NotEmptyString.unsafe("github.event_name"))

    /**
     *  The path to the full event webhook payload on the runner.
     */
    lazy val event_path = Expression.Constant(NotEmptyString.unsafe("github.event_path"))

    /**
     *  The head_ref or source branch of the pull request in a workflow run. This property is only available when the event that triggers a workflow run is a pull_request.
     */
    lazy val head_ref = Expression.Constant(NotEmptyString.unsafe("github.head_ref"))

    /**
     *  The job_id of the current job.
     */
    lazy val job = Expression.Constant(NotEmptyString.unsafe("github.job"))

    /**
     *  The branch or tag ref that triggered the workflow run. For branches this in the format refs/heads/<branch_name>, and for tags it is refs/tags/<tag_name>.
     */
    lazy val ref = Expression.Constant(NotEmptyString.unsafe("github.ref"))

    /**
     *  The owner and repository name. For example, Codertocat/Hello-World.
     */
    lazy val repository = Expression.Constant(NotEmptyString.unsafe("github.repository"))

    /**
     *  The repository owner's name. For example, Codertocat.
     */
    lazy val repository_owner = Expression.Constant(NotEmptyString.unsafe("github.repository_owner"))

    /**
     *  A unique number for each run within a repository. This number does not change if you re-run the workflow run.
     */
    lazy val run_id = Expression.Constant(NotEmptyString.unsafe("github.run_id"))

    /**
     *  A unique number for each run of a particular workflow in a repository. This number begins at 1 for the workflow's first run, and increments with each new run. This number does not change if you re-run the workflow run.
     */
    lazy val run_number = Expression.Constant(NotEmptyString.unsafe("github.run_number"))

    /**
     *  The commit SHA that triggered the workflow run.
     */
    lazy val sha = Expression.Constant(NotEmptyString.unsafe("github.sha"))

    /**
     *  A token to authenticate on behalf of the GitHub App installed on your repository. This is functionally equivalent to the GITHUB_TOKEN secret. For more information, see "Authenticating with the GITHUB_TOKEN."
     */
    lazy val token = Expression.Constant(NotEmptyString.unsafe("github.token"))

    /**
     *  The name of the workflow. If the workflow file doesn't specify a name, the value of this property is the full path of the workflow file in the repository.
     */
    lazy val workflow = Expression.Constant(NotEmptyString.unsafe("github.workflow"))

    /**
     *  The default working directory for steps and the default location of your repository when using the checkout action.
     */
    lazy val workspace = Expression.Constant(NotEmptyString.unsafe("github.workspace"))

  }

  /** Needs */

  object needs extends Dynamic {

    def selectDynamic(name: String) = NeedsDsl(name)(
      Expression.Constant(NotEmptyString.unsafe(s"needs.$name.result")),
      OutputsDsl(name)
    )

    case class OutputsDsl(private val id: String) extends Dynamic {

      def selectDynamic(name: String) = Expression.Constant(NotEmptyString.unsafe(s"needs.$id.outputs.$name"))

    }

    case class NeedsDsl(private val id: String)(val result: Expression, val outputs: OutputsDsl)

  }

  /** Secrets */

  object secrets extends Dynamic {

    def selectDynamic(name: String): Expression = Expression.Constant(NotEmptyString.unsafe("secrets." + name))

    /**
     * GitHub automatically creates a GITHUB_TOKEN secret to use in your workflow. You can use the GITHUB_TOKEN to
     * authenticate in a workflow run.
     *
     * | Permission          | Access type | Access by forked repos |
     * | ---                 | :---:       | :---:                  |
     * | actions             | read/write  | read                   |
     * | checks              | read/write  | read                   |
     * | contents            | read/write  | read                   |
     * | deployments         | read/write  | read                   |
     * | issues              | read/write  | read                   |
     * | metadata            | read        | read                   |
     * | packages            | read/write  | read                   |
     * | pull requests       | read/write  | read                   |
     * | repository projects | read/write  | read                   |
     * | statuses            | read/write  | read                   |
     *
     * The token is also available in the github.token context.
     */
    lazy val GITHUB_TOKEN: Expression = Expression.Constant(NotEmptyString.unsafe("secrets.GITHUB_TOKEN"))

  }

}
