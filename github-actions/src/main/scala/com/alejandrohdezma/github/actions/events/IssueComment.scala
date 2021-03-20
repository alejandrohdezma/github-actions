package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NotEmptyString
import com.alejandrohdezma.github.actions.yaml._

/**
 * Runs your workflow anytime the issue_comment event occurs. More than one activity type triggers this event.
 *
 * @param types selects the types of activity that will trigger a workflow run.
 *
 * @see See more information about the [[https://developer.github.com/v3/issues/comments/ REST API]].
 * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#issue-comment-event-issue_comment]]
 */
final case class IssueComment(types: List[IssueComment.Types]) extends Event {

  def onCreated() = copy(types = types :+ IssueComment.Types.Created)

  def onEdited() = copy(types = types :+ IssueComment.Types.Edited)

  def onDeleted() = copy(types = types :+ IssueComment.Types.Deleted)

}

object IssueComment {

  implicit val IssueCommentEncoder: Encoder[IssueComment] = event => Yaml.obj("types" := event.types)

  sealed abstract class Types(val value: NotEmptyString)

  object Types {

    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    case object Created extends Types(NotEmptyString.unsafe("created"))
    case object Edited  extends Types(NotEmptyString.unsafe("edited"))
    case object Deleted extends Types(NotEmptyString.unsafe("deleted"))

  }

}
