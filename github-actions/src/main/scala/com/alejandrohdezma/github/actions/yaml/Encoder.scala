package com.alejandrohdezma.github.actions.yaml

/** A type class that provides a conversion from a value of type `A` to a [[Yaml]] value. */
trait Encoder[A] extends Function1[A, Yaml]

object Encoder {

  final def apply[A](implicit instance: Encoder[A]): Encoder[A] = instance

  implicit val identity: Encoder[Yaml]   = yaml => yaml
  implicit val string: Encoder[String]   = Yaml.Scalar(_)
  implicit val int: Encoder[Int]         = Yaml.Number(_)
  implicit val boolean: Encoder[Boolean] = Yaml.Bool(_)

  implicit def option[A: Encoder]: Encoder[Option[A]] =
    _.fold(Yaml.Null: Yaml)(Encoder[A])

  implicit def list[A: Encoder]: Encoder[List[A]] =
    list => Yaml.Array(list.map(Encoder[A]))

  implicit def map[A: Encoder]: Encoder[Map[String, A]] =
    map => Yaml.Object(map.mapValues(Encoder[A]))

}
