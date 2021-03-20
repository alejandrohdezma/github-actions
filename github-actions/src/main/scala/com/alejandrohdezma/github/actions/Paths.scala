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
 * When using the push and pull_request events, you can configure a workflow to run when at least one file does not
 * match `Paths.Ignore` or at least one modified file matches the configured paths. Path filters are not evaluated for
 * pushes to tags.
 *
 * The `Paths.Ignore` and `Paths.Matching` keywords accept glob patterns that use the * and ** wildcard characters to
 * match more than one path name.
 *
 * You can exclude paths using two types of filters. You cannot use both of these filters for the same event in a
 * workflow.
 *
 * - `Paths.Ignore` - Use the `Paths.Ignore` filter when you only need to exclude path names.
 * - `Paths.Matching` - Use the `Paths.Matching` filter when you need to filter paths for positive matches and exclude paths.
 *
 * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/workflow-syntax-for-github-actions#filter-pattern-cheat-sheet]]
 * @see [[https://help.github.com/en/github/automating-your-workflow-with-github-actions/workflow-syntax-for-github-actions#onpushpull_requestpaths]]
 */
sealed trait Paths

object Paths {

  final case class Matching(matches: NotEmptyList[NotEmptyString]) extends Paths

  final case class Ignore(matches: NotEmptyList[NotEmptyString]) extends Paths

  implicit val PathsEncoder: Encoder[Paths] = {
    case Paths.Matching(matches) => Yaml.obj("paths" := matches)
    case Paths.Ignore(matches)   => Yaml.obj("paths-ignore" := matches)
  }

}
