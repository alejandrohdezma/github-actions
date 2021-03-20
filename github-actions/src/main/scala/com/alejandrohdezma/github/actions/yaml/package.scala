package com.alejandrohdezma.github.actions

package object yaml {

  implicit final class StringKeyOps(private val value: String) extends AnyVal {

    /**
     * Constructs a tuple (string-yaml) that can be conviniently used with [[Yaml.obj]].
     */
    final def :=[A: Encoder](a: A): (String, Yaml) = (value, Encoder[A].apply(a))

  }

  implicit final class YamlOps[A](private val value: A) extends AnyVal {

    /**
     * Converts this value to [[Yaml]] using the available [[Encoder]] instance.
     */
    final def asYaml(implicit encoder: Encoder[A]): Yaml = encoder(value)

  }

}
