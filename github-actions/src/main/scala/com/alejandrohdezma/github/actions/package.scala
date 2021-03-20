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

package com.alejandrohdezma.github

import cats.kernel.Semigroup

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string._
import io.circe.Json

package object actions {

  type Cron = String Refined MatchesRegex[
    W.`"""^(((\\d+,)+\\d+|((\\d+|\\*)\\/\\d+|JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)|(\\d+-\\d+)|\\d+|\\*|MON|TUE|WED|THU|FRI|SAT|SUN) ?){5,7}$"""`.T
  ]

  implicit private[actions] val JsonSemigroup: Semigroup[Json] = Semigroup.instance { case (a, b) => b.deepMerge(a) }

}
