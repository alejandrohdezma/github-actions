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
 * A build matrix is a set of different configurations of the virtual environment. For example you might run a job
 * against more than one supported version of a language, operating system, or tool. Each configuration is a copy of the
 * job that runs and reports a status.
 *
 * You can specify a matrix by supplying an array for the configuration options. For example, if the GitHub virtual
 * environment supports Node.js versions 6, 8, and 10 you could specify an array of those versions in the matrix.
 *
 * When you define a matrix of operating systems, you must set the required runs-on keyword to the operating system of
 * the current job, rather than hard-coding the operating system name. To access the operating system name, you can use
 * the matrix.os context parameter to set runs-on.
 *
 * @see [[https://help.github.com/en/articles/contexts-and-expression-syntax-for-github-actions]]
 */
final case class Matrix(
    value: NotEmptyList[Matrix.Configuration],
    include: List[Map[NotEmptyString, NotEmptyString]] = Nil,
    exclude: List[Map[NotEmptyString, NotEmptyString]] = Nil
)

object Matrix {

  final case class Configuration(key: NotEmptyString, values: NotEmptyList[NotEmptyString])

  implicit val MatrixEncoder: Encoder[Matrix] = matrix => {
    val jsonExclude = matrix.exclude.filter(_.nonEmpty) match {
      case Nil  => Yaml.obj()
      case list => Yaml.obj("exclude" := list.map(_.map(t => t._1.value -> t._2)))
    }

    val jsonInclude = matrix.include.filter(_.nonEmpty) match {
      case Nil  => Yaml.obj()
      case list => Yaml.obj("include" := list.map(_.map(t => t._1.value -> t._2)))
    }

    jsonExclude.merge(jsonInclude).merge(matrix.value.map(c => c.key.value -> c.values).value.toMap.asYaml)
  }

}
