package com.alejandrohdezma.github.actions.base

import com.alejandrohdezma.github.actions.yaml._

final case class NotEmptyList[A] private (val value: List[A]) extends AnyVal {

  def head: A       = value.head // scalafix:ok
  def tail: List[A] = value.tail

  def reduce(f: (A, A) => A): A = value.reduce(f)

  def map[B](f: A => B): NotEmptyList[B] = NotEmptyList(value.map(f))

  def :::(list: NotEmptyList[A]): NotEmptyList[A] = NotEmptyList(value ++ list.value)

  def :+(a: A): NotEmptyList[A] = NotEmptyList(value :+ a)

}

object NotEmptyList {

  implicit def NotEmptyListEncoder[A: Encoder]: Encoder[NotEmptyList[A]] = _.value.asYaml

  def of[A](head: A, tail: A*) = NotEmptyList(List(head) ++ tail.toList)

}
