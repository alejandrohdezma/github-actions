package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.NonEmptyString
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow anytime the `issue_comment` event occurs. More than one activity type triggers this event.
  *
  * @param types Selects the types of activity that will trigger a workflow run.
  *
  * @see [[https://developer.github.com/v3/issues/comments/]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#issue-comment-event-issue_comment]]
  */
final case class IssueComment(types: List[IssueComment.Types]) extends Event {

  /** Launch this workflow when a comment is created. */
  def onCreated() = copy(types = types :+ IssueComment.Types.Created)

  /** Launch this workflow when a comment is edited. */
  def onEdited() = copy(types = types :+ IssueComment.Types.Edited)

  /** Launch this workflow when a comment is deleted. */
  def onDeleted() = copy(types = types :+ IssueComment.Types.Deleted)

}

/** Contains implicit values and classes relevant to [[IssueComment]]. */
object IssueComment {

  /** Allows converting a [[IssueComment]] value into [[yaml.Yaml yaml]]. */
  implicit val IssueCommentEncoder: Encoder[IssueComment] = event => Yaml.obj("types" := event.types)

  /** The different types on which the [[IssueComment]] event can be triggered. */
  sealed abstract class Types(val value: NonEmptyString)

  /** Contains the possible values for the [[Types]] hierarchy and implicits relevant to this class. */
  object Types {

    /** Allows converting a [[Types]] value into [[yaml.Yaml yaml]]. */
    implicit val TypesEncoder: Encoder[Types] = _.value.asYaml

    /** An issue comment is created. */
    case object Created extends Types(NonEmptyString.unsafe("created"))

    /** An issue comment is edited. */
    case object Edited extends Types(NonEmptyString.unsafe("edited"))

    /** An issue comment is deleted. */
    case object Deleted extends Types(NonEmptyString.unsafe("deleted"))

  }

}
