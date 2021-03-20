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

import cats.data.NonEmptyList
import cats.data.NonEmptyMap

import eu.timepit.refined.cats._
import eu.timepit.refined.types.string.NonEmptyString
import io.circe.Encoder
import io.circe.Json
import io.circe.syntax._

/**
 * A strategy creates a build matrix for your jobs. You can define different variations of an environment to run each
 * job in.
 *
 * @param matrix a build matrix is a set of different configurations of the virtual environment
 * @param failFast when set to true, GitHub cancels all in-progress jobs if any matrix job fails
 * @param maxParallel the maximum number of jobs that can run simultaneously when using a matrix job strategy. By
 *   default, GitHub will maximize the number of jobs run in parallel depending on the available runners on
 *   GitHub-hosted virtual machines
 *
 * @see [[https://help.github.com/en/actions/automating-your-workflow-with-github-actions/workflow-syntax-for-github-actions#jobsjob_idstrategy]]
 */
sealed abstract class Strategy(
    val `fail-fast`: Option[Boolean] = None,
    val maxParallel: Option[Int] = None
)

object Strategy {

  final case class FromMatrix(
      val value: Matrix,
      override val `fail-fast`: Option[Boolean] = None,
      override val maxParallel: Option[Int] = None
  ) extends Strategy(`fail-fast`, maxParallel) {

    def failFast(): FromMatrix = copy(`fail-fast` = Some(true))

    def doNotfailFast(): FromMatrix = copy(`fail-fast` = Some(false))

    def maxParallel(max: Int): FromMatrix = copy(maxParallel = Some(max))

    def config(key: NonEmptyString)(first: NonEmptyString, rest: NonEmptyString*) =
      copy(value = value.copy(value = value.value.add(key -> NonEmptyList.of(first, rest: _*))))

    def include(
        first: (NonEmptyString, NonEmptyString),
        rest: (NonEmptyString, NonEmptyString)*
    ): FromMatrix = copy(value =
      value.copy(include =
        value.include
          .map(_.append(NonEmptyMap.of(first, rest: _*)))
          .orElse(Some(NonEmptyList.of(NonEmptyMap.of(first, rest: _*))))
      )
    )

    def exclude(
        first: (NonEmptyString, NonEmptyString),
        rest: (NonEmptyString, NonEmptyString)*
    ): FromMatrix = copy(value =
      value.copy(exclude =
        value.exclude
          .map(_.append(NonEmptyMap.of(first, rest: _*)))
          .orElse(Some(NonEmptyList.of(NonEmptyMap.of(first, rest: _*))))
      )
    )

  }

  def matrix(
      first: (NonEmptyString, NonEmptyList[NonEmptyString]),
      rest: (NonEmptyString, NonEmptyList[NonEmptyString])*
  ): FromMatrix =
    FromMatrix(Matrix(NonEmptyMap.of(first, rest: _*)))

  final case class FromExpression(
      val value: Expression,
      override val `fail-fast`: Option[Boolean] = None,
      override val maxParallel: Option[Int] = None
  ) extends Strategy(`fail-fast`, maxParallel) {

    def failFast(): FromExpression = copy(`fail-fast` = Some(true))

    def doNotfailFast(): FromExpression = copy(`fail-fast` = Some(false))

    def maxParallel(max: Int): FromExpression = copy(maxParallel = Some(max))

  }

  def fromExpression(expression: Expression): FromExpression = FromExpression(expression)

  implicit val StrategyEncoder: Encoder[Strategy] = {
    case FromMatrix(value, failFast, maxParallel) =>
      Json.obj(
        "matrix"       := value,
        "fail-fast"    := failFast,
        "max-parallel" := maxParallel
      )
    case FromExpression(value, failFast, maxParallel) =>
      Json.obj(
        "matrix"       := value,
        "fail-fast"    := failFast,
        "max-parallel" := maxParallel
      )
  }

}
