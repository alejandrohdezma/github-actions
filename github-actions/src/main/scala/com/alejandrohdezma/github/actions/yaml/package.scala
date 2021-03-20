package com.alejandrohdezma.github.actions

/** Contains all classes and methods necesary to encode a case class (such as [[Workflow]]) to a valid YAML file. */
package object yaml {

  /** Provides extension functions to create string-[[Yaml]] tuples from a `String`. */
  implicit final class StringKeyOps(private val value: String) extends AnyVal {

    /** Constructs a tuple (string-[[Yaml]]) that can be conviniently used with [[Yaml.obj]]. */
    final def :=[A: Encoder](a: A): (String, Yaml) = (value, Encoder[A].apply(a))

  }

  /** Provides a extension function for any `A` with a valid [[Encoder]] instance to transform it to [[Yaml]]. */
  implicit final class Encode2YamlOps[A](private val value: A) extends AnyVal {

    /** Converts this value to [[Yaml]] using the available [[Encoder]] instance. */
    final def asYaml(implicit encoder: Encoder[A]): Yaml = encoder(value)

  }

}
