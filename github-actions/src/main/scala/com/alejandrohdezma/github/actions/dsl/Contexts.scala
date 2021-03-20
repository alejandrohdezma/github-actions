package com.alejandrohdezma.github.actions.dsl

import com.alejandrohdezma.github.actions.EnvName
import com.alejandrohdezma.github.actions.Expression
import com.alejandrohdezma.github.actions.NotEmptyString

@SuppressWarnings(Array("scalafix:DisableSyntax.valInAbstract"))
trait Contexts {

  /** Environment variables */

  object env {

    def apply(name: EnvName): Expression = Expression.unsafe("env." + name.value)

    /**
     * Always set to true.
     */
    lazy val CI: Expression = Expression.Constant("env.CI")

    /**
     * The name of the workflow.
     */
    lazy val GITHUB_WORKFLOW: Expression = Expression.Constant("env.GITHUB_WORKFLOW")

    /**
     * A unique number for each run within a repository. This number does not change if you re-run the workflow run.
     */
    lazy val GITHUB_RUN_ID: Expression = Expression.Constant("env.GITHUB_RUN_ID")

    /**
     * A unique number for each run of a particular workflow in a repository. This number begins at 1 for the workflow's first run, and increments with each new run. This number does not change if you re-run the workflow run.
     */
    lazy val GITHUB_RUN_NUMBER: Expression = Expression.Constant("env.GITHUB_RUN_NUMBER")

    /**
     * The job_id of the current job.
     */
    lazy val GITHUB_JOB: Expression = Expression.Constant("env.GITHUB_JOB")

    /**
     * The unique identifier (id) of the action.
     */
    lazy val GITHUB_ACTION: Expression = Expression.Constant("env.GITHUB_ACTION")

    /**
     * Always set to true when GitHub Actions is running the workflow. You can use this variable to differentiate when tests are being run locally or by GitHub Actions.
     */
    lazy val GITHUB_ACTIONS: Expression = Expression.Constant("env.GITHUB_ACTIONS")

    /**
     * The name of the person or app that initiated the workflow. For example, octocat.
     */
    lazy val GITHUB_ACTOR: Expression = Expression.Constant("env.GITHUB_ACTOR")

    /**
     * The owner and repository name. For example, octocat/Hello-World.
     */
    lazy val GITHUB_REPOSITORY: Expression = Expression.Constant("env.GITHUB_REPOSITORY")

    /**
     * The name of the webhook event that triggered the workflow.
     */
    lazy val GITHUB_EVENT_NAME: Expression = Expression.Constant("env.GITHUB_EVENT_NAME")

    /**
     * The path of the file with the complete webhook event payload. For example, /github/workflow/event.json.
     */
    lazy val GITHUB_EVENT_PATH: Expression = Expression.Constant("env.GITHUB_EVENT_PATH")

    /**
     * The GitHub workspace directory path. The workspace directory is a copy of your repository if your workflow uses the actions/checkout action. If you don't use the actions/checkout action, the directory will be empty. For example, /home/runner/work/my-repo-name/my-repo-name.
     */
    lazy val GITHUB_WORKSPACE: Expression = Expression.Constant("env.GITHUB_WORKSPACE")

    /**
     * The commit SHA that triggered the workflow. For example, ffac537e6cbbf934b08745a378932722df287a53.
     */
    lazy val GITHUB_SHA: Expression = Expression.Constant("env.GITHUB_SHA")

    /**
     * The branch or tag ref that triggered the workflow. For example, refs/heads/feature-branch-1. If neither a branch or tag is available for the event type, the variable will not exist.
     */
    lazy val GITHUB_REF: Expression = Expression.Constant("env.GITHUB_REF")

    /**
     * Only set for pull request events. The name of the head branch.
     */
    lazy val GITHUB_HEAD_REF: Expression = Expression.Constant("env.GITHUB_HEAD_REF")

    /**
     * Only set for pull request events. The name of the base branch.
     */
    lazy val GITHUB_BASE_REF: Expression = Expression.Constant("env.GITHUB_BASE_REF")

    /**
     * Returns the URL of the GitHub server. For example: https://github.com.
     */
    lazy val GITHUB_SERVER_URL: Expression = Expression.Constant("env.GITHUB_SERVER_URL")

    /**
     * Returns the API URL. For example: https://api.github.com.
     */
    lazy val GITHUB_API_URL: Expression = Expression.Constant("env.GITHUB_API_URL")

    /**
     * Returns the GraphQL API URL. For example: https://api.github.com/graphql.
     */
    lazy val GITHUB_GRAPHQL_URL: Expression = Expression.Constant("env.GITHUB_GRAPHQL_URL")

  }

  /** Jobs */

  object job {

    object container {

      lazy val id = Expression.Constant("job.container.id")

      lazy val network = Expression.Constant("job.container.id")

    }

    object services {

      case class JobServiceDsl(id: Expression, network: Expression, ports: Expression)

      def apply(name: NotEmptyString): JobServiceDsl = JobServiceDsl(
        Expression.Constant(NotEmptyString.unsafe(s"job.services.$name.id")),
        Expression.Constant(NotEmptyString.unsafe(s"job.services.$name.network")),
        Expression.Constant(NotEmptyString.unsafe(s"job.services.$name.ports"))
      )

    }

    lazy val status: Expression = Expression.Constant("job.status")

  }

  /** Secrets */

  object secrets {

    def apply(name: EnvName): Expression = Expression.Constant(NotEmptyString.unsafe("secrets." + name.value))

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
    lazy val GITHUB_TOKEN: Expression = secrets("GITHUB_TOKEN")

  }

}
