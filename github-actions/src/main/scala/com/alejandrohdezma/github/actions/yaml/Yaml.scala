package com.alejandrohdezma.github.actions.yaml

import scala.collection.immutable.ListMap

import com.alejandrohdezma.github.actions.yaml.Yaml.Bool
import com.alejandrohdezma.github.actions.yaml.Yaml.Scalar

sealed trait Yaml {

  /**
   * Merges the current [[Yaml]] value with a provided one.
   *
   * Only works if the two values are [[Yaml.Object]] or [[Yaml.Array]].
   *
   * Otherwise it will just return the supplied value.
   */
  def merge(that: Yaml): Yaml = (this, that) match {
    case (Yaml.Object(thisValue), Yaml.Object(thatValue)) => Yaml.Object(thisValue ++ thatValue)
    case (Yaml.Object(thisValue), Yaml.Null)              => Yaml.Object(thisValue)
    case (Yaml.Null, Yaml.Object(thatValue))              => Yaml.Object(thatValue)
    case (Yaml.Array(thisValue), Yaml.Array(thatValue))   => Yaml.Array(thisValue ++ thatValue)
    case (Yaml.Array(thisValue), Yaml.Null)               => Yaml.Array(thisValue)
    case (Yaml.Null, Yaml.Array(thatValue))               => Yaml.Array(thatValue)
    case (_, _)                                           => that
  }

  /**
   * Returns `true` if this value is a [[Yaml.Null]]; otherwise, returns `false`.
   */
  def isNull(): Boolean = this match {
    case Yaml.Null => true
    case _         => false
  }

  /**
   * Pretty-prints this [[Yaml]] as a valid YAML file.
   */
  def prettyPrint(): String = print("")

  private[yaml] def print(indent: String): String = this match {
    case Yaml.Object(map) if map.filterNot(_._2.isNull()).isEmpty => ""
    case Yaml.Object(map) =>
      map
        .filterNot(_._2.isNull())
        .filterNot {
          case (_, Yaml.Array(value)) if value.isEmpty => true
          case _                                       => false
        }
        .map {
          case (key, obj: Yaml.Object) if obj.value.nonEmpty =>
            obj.print(indent + "  ") match {
              case ""    => s"$indent$key:"
              case value => s"$indent$key:\n$value"
            }
          case (key, array: Yaml.Array) if array.isSmallScalarArray => s"$indent$key: ${array.print("")}"
          case (key, array: Yaml.Array)                             => s"$indent$key:\n${array.print(indent + "  ")}"
          case (key, scalar: Yaml.Scalar) if scalar.value.contains("\n") =>
            s"$indent$key: |\n${scalar.value.split("\n").map(indent + "  " + _).mkString("\n")}"
          case (key, yaml) => s"$indent$key:${yaml.print(" ")}"
        }
        .mkString("\n")
    case array @ Yaml.Array(list) if array.isSmallScalarArray => list.map(_.print(s"")).mkString("[", ", ", "]")
    case Yaml.Array(list)                                     => list.map(_.print(s"$indent  ").replaceFirst(s"$indent  ", s"$indent- ")).mkString("\n")
    case scalar: Scalar                                       => s"$indent${scalar.formatted}"
    case Bool(bool)                                           => s"$indent$bool"
    case Yaml.Number(number)                                  => s"$indent$number"
    case Yaml.Null                                            => ""
  }

}

object Yaml {

  lazy val empty = Yaml.obj()

  /**
   * Creates a [[Yaml.Object]] from a list of string-yaml tuples.
   */
  final def obj(fields: (String, Yaml)*): Yaml = Object(ListMap(fields: _*))

  final case class Object(value: Map[String, Yaml]) extends Yaml

  final case class Array(value: List[Yaml]) extends Yaml {

    /**
     * `true` if this array is only formed by five or less [[Scalar]] (strings).
     */
    lazy val isSmallScalarArray = value.forall {
      case _: Scalar => true
      case _         => false
    } && value.size <= 5

  }

  final case class Scalar(val value: String) extends Yaml {

    /**
     * Returns the value wrapped in single quotes if it contains any of the YAML special characters; otherwise
     * returns the raw value.
     */
    lazy val formatted =
      if (value.contains("*") || value.startsWith("[") || value.startsWith("!") || value.contains(": ")) s"'$value'"
      else value

  }

  final case class Bool(value: Boolean) extends Yaml

  final case class Number(value: Int) extends Yaml

  case object Null extends Yaml

}
