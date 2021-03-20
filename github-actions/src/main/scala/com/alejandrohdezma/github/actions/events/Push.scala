package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.Branches
import com.alejandrohdezma.github.actions.Paths
import com.alejandrohdezma.github.actions.Tags
import com.alejandrohdezma.github.actions.base.Ignore
import com.alejandrohdezma.github.actions.base.Matching
import com.alejandrohdezma.github.actions.yaml._

/**
 * Runs your workflow when someone pushes to a repository branch, which triggers the push event.
 *
 * Note: The webhook payload available to GitHub Actions does not include the added, removed, and modified attributes
 * in the commit object. You can retrieve the full commit object using the REST API.
 *
 * @see See more information about the [[https://developer.github.com/v3/repos/commits/#get-a-single-commit REST API]].
 * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#push-event-push]]
 */
final case class Push(branches: Option[Branches] = None, tags: Option[Tags] = None, paths: Option[Paths] = None)
    extends Event {

  def branches(matching: Matching): Push = copy(branches = Some(Branches.Matching(matching.value)))
  def branches(ignore: Ignore): Push     = copy(branches = Some(Branches.Ignore(ignore.value)))

  def tags(matching: Matching): Push = copy(tags = Some(Tags.Matching(matching.value)))
  def tags(ignore: Ignore): Push     = copy(tags = Some(Tags.Ignore(ignore.value)))

  def paths(matching: Matching): Push = copy(paths = Some(Paths.Matching(matching.value)))
  def paths(ignore: Ignore): Push     = copy(paths = Some(Paths.Ignore(ignore.value)))

}

object Push {

  implicit val PushEncoder: Encoder[Push] = event =>
    event.branches.asYaml.merge(event.tags.asYaml).merge(event.paths.asYaml)

}
