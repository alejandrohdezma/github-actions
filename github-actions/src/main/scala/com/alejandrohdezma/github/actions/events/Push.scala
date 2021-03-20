package com.alejandrohdezma.github.actions.events

import com.alejandrohdezma.github.actions.base.Ignoring
import com.alejandrohdezma.github.actions.base.Matching
import com.alejandrohdezma.github.actions.yaml._

/** Runs your workflow when someone pushes to a repository branch, which triggers the `push` event.
  *
  * Note: The webhook payload available to GitHub Actions does not include the `added`, `removed`, and `modified`
  * attributes in the commit object. You can retrieve the full commit object using the REST API.
  *
  * @param branches List of glob patterns that should be used to either match or ignore branches.
  * @param paths List of glob patterns that should be used to either match or ignore paths.
  * @param tags List of glob patterns that should be used to either match or ignore tags.
  *
  * @see [[https://developer.github.com/v3/repos/commits/#get-a-single-commit]]
  * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/events-that-trigger-workflows#push-event-push]]
  */
final case class Push(
    branches: Option[Either[Ignoring, Matching]] = None,
    tags: Option[Either[Ignoring, Matching]] = None,
    paths: Option[Either[Ignoring, Matching]] = None
) extends Event {

  /** Specifies the current workflow to run only if the current branch matches the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[branches(ignore* branches(ignore)]] this will override it.
    *
    * @example {{{push.branches(matching("releases&#47;**"))}}}
    */
  def branches(matching: Matching): Push = copy(branches = Some(Right(matching)))

  /** Specifies the current workflow to run only if the current branch does not match the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[branches(matching* branches(matching)]] this will override it.
    *
    * @example {{{push.branches(ignoring("wip&#47;**"))}}}
    */
  def branches(ignore: Ignoring): Push = copy(branches = Some(Left(ignore)))

  /** Specifies the current workflow to run only if the current tag matches the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[tags(ignore* tags(ignore)]] this will override it.
    *
    * @example {{{push.tags(matching("v*"))}}}
    */
  def tags(matching: Matching): Push = copy(tags = Some(Right(matching)))

  /** Specifies the current workflow to run only if the current tag does not match the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[tags(matching* tags(matching))]] this will override it.
    *
    * @example {{{push.tags(ignoring("*-SNAPSHOT"))}}}
    */
  def tags(ignore: Ignoring): Push = copy(tags = Some(Left(ignore)))

  /** Specifies the current workflow to run only if at least one modified file matches the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[paths(ignore* tags(ignore))]] this will override it.
    *
    * @example {{{push.paths(matching("**.sbt"))}}}
    */
  def paths(matching: Matching): Push = copy(paths = Some(Right(matching)))

  /** Specifies the current workflow to run only if at least one modified file does not match the provided patterns.
    *
    * Note that since only one of the two patterns can be used (ignore/matching) if there has been a previous
    * call to [[paths(matching* tags(matching))]] this will override it.
    *
    * @example {{{push.paths(ignoring("docs&#47;**"))}}}
    */
  def paths(ignore: Ignoring): Push = copy(paths = Some(Left(ignore)))

}

/** Contains implicit values relevant to [[Push]]. */
object Push {

  /** Allows converting a [[Push]] value into [[yaml.Yaml yaml]]. */
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
