package com.alejandrohdezma.github.actions.yaml

/** A type class that provides a conversion from an `A` to a [[Yaml]] value. */
trait Encoder[A] extends Function1[A, Yaml]

/** Contains [[Encoder]] instances for std-library values, like `List`, `Option` or `String`. */
object Encoder {

  /** Summons an [[Encoder]] instance for the type `A` available in the implicit scope. */
  final def apply[A](implicit instance: Encoder[A]): Encoder[A] = instance

  /** A [[Yaml]] values is just encoded to itself. */
  implicit val identity: Encoder[Yaml] = yaml => yaml

  /** `String` values are encoded as [[Yaml.Scalar]]. */
  implicit val string: Encoder[String] = Yaml.Scalar(_)

  /** `Int` values are encoded as [[Yaml.Number]]. */
  implicit val int: Encoder[Int] = Yaml.Number(_)

  /** `Boolean` values are encoded as [[Yaml.Bool]]. */
  implicit val boolean: Encoder[Boolean] = Yaml.Bool(_)

  /** `Option` values are either encoded as [[Yaml.Null]] if they are empty or as the value returned
    * for encoding its contained value.
    */
  implicit def option[A: Encoder]: Encoder[Option[A]] = _.fold(Yaml.Null: Yaml)(Encoder[A])

  /** `List` values are encoded as [[Yaml.Array]] with their values converted to [[Yaml]] using the
    * available [[Encoder]] instance.
    */
  implicit def list[A: Encoder]: Encoder[List[A]] = list => Yaml.Array(list.map(Encoder[A]))

  /** `Map` values (whose keys are `String`) are encoded as [[Yaml.Object]] with their values converted
    * to [[Yaml]] using the available [[Encoder]] instance.
    */
  implicit def map[A: Encoder]: Encoder[Map[String, A]] = map => Yaml.Object(map.mapValues(Encoder[A]))

}
