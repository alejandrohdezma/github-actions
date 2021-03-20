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

import com.alejandrohdezma.github.actions.base.NotEmptyList
import com.alejandrohdezma.github.actions.base.NotEmptyString
import com.alejandrohdezma.github.actions.yaml._

/**
 * The [[Branches.Matching]] and [[Branches.Ignore]] accept glob patterns that use the * and ** wildcard characters to
 * match more than one branch.
 *
 * These patterns are evaluated against the Git ref's name. For example, defining the pattern `mona/octocat` in
 * [[Branches.Matching]] will match the `refs/heads/mona/octocat` Git ref. The pattern `releases*&#47;**` will match the
 * `refs/heads/releases/10` Git ref.
 *
 * You can use two types of filters to prevent a workflow from running` on pushes and pull requests to branches:
 * [[Branches.Matching]] or [[Branches.Ignore]]. You cannot use both filters for the same event in a workflow. Use the
 * [[Branches.Matching]] filter when you need to filter branches for positive matches and exclude branches. Use the
 * [[Branches.Ignore]] filter when you only need to exclude branch names.
 *
 * You can exclude branches using the ! character. The order that you define patterns matters.
 *
 * - A matching negative pattern (prefixed with !) after a positive match will exclude the Git ref.
 * - A matching positive pattern after a negative match will include the Git ref again.
 *
 * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/workflow-syntax-for-github-actions#filter-pattern-cheat-sheet]]
 */
sealed trait Branches

object Branches {

  final case class Matching(matches: NotEmptyList[NotEmptyString]) extends Branches

  final case class Ignore(matches: NotEmptyList[NotEmptyString]) extends Branches

  implicit val BranchesEncoder: Encoder[Branches] = {
    case Branches.Matching(matches) => Yaml.obj("branches" := matches)
    case Branches.Ignore(matches)   => Yaml.obj("branches-ignore" := matches)
  }

}
