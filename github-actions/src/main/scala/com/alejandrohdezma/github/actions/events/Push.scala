package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.Ignoring
import com.alejandrohdezma.github.actions.base.Matching
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow when someone pushes to a repository branch, which triggers the push event.
  *
  * Note: The webhook payload available to GitHub Actions does not include the added, removed, and modified attributes
  * in the commit object. You can retrieve the full commit object using the REST API.
  *
  * @see
  *   See more information about the [[https://developer.github.com/v3/repos/commits/#get-a-single-commitRESTAPI]].
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#push-event-push]]
  */
final case class Push(
    branches: Option[Either[Ignoring, Matching]] = None,
    tags: Option[Either[Ignoring, Matching]] = None,
    paths: Option[Either[Ignoring, Matching]] = None
) extends Event {

  def branches(matching: Matching): Push =
    copy(branches = Some(Right(matching)))
  def branches(ignore: Ignoring): Push = copy(branches = Some(Left(ignore)))

  def tags(matching: Matching): Push = copy(tags = Some(Right(matching)))
  def tags(ignore: Ignoring): Push   = copy(tags = Some(Left(ignore)))

  def paths(matching: Matching): Push = copy(paths = Some(Right(matching)))
  def paths(ignore: Ignoring): Push   = copy(paths = Some(Left(ignore)))

}

object Push {

  implicit val PushEncoder: Encoder[Push] = event =>
    Yaml.obj(
      "branches"        := event.branches.flatMap(_.toOption).map(_.value),
      "branches-ignore" := event.branches.flatMap(_.swap.toOption).map(_.value),
      "tags"            := event.tags.flatMap(_.toOption).map(_.value),
      "tags-ignore"     := event.tags.flatMap(_.swap.toOption).map(_.value),
      "paths"           := event.paths.flatMap(_.toOption).map(_.value),
      "paths-ignore"    := event.paths.flatMap(_.swap.toOption).map(_.value)
    )

}
