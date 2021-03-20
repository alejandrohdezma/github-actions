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
/**
 * The `Tags.Matching` and `Tags.Ignore accept glob patterns that use the * and ** wildcard characters to match
 * more than one tag.
 *
 * The patterns defined in tags are evaluated against the Git ref's name. For example, defining the pattern
 * `latest` in `Tags.Matching` will match just the `latest` tag. The pattern "v*" will match any tag starting with `v`.
 *
 * You can use two types of filters to prevent a workflow from running on pushes and pull requests to tags:
 *
 * - `Tags.Matching` or `Tags.Ignore` - You cannot use both the `Tags` and `Tags.Ignore` filters for the
 *   same event in a workflow. Use the `Tags` filter when you need to filter tags for positive matches and
 *   exclude tags. Use the `Tags.Ignore` filter when you only need to exclude tag names.
 *
 * You can exclude tags using the ! character. The order that you define patterns matters.
 *
 * - A matching negative pattern (prefixed with !) after a positive match will exclude the Git ref.
 * - A matching positive pattern after a negative match will include the Git ref again.
 *
 * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/workflow-syntax-for-github-actions#filter-pattern-cheat-sheet]]
 */
sealed trait Tags

object Tags {

  final case class Matching(matches: NotEmptyList[NotEmptyString]) extends Tags

  final case class Ignore(matches: NotEmptyList[NotEmptyString]) extends Tags

  implicit val TagsEncoder: Encoder[Tags] = {
    case Tags.Matching(matches) => Yaml.obj("tags" := matches)
    case Tags.Ignore(matches)   => Yaml.obj("tags-ignore" := matches)
  }

}
