package com.alejandrohdezma.github.actions.base

import com.alejandrohdezma.github.actions.yaml._

/** Represents a list that cannot be empty. Similar to cats' `NonEmptyList` but using a `List` internally so it can be
  * easily modified by lenses.
  */
final case class NonEmptyList[A] private (val value: List[A]) extends AnyVal {

  /** Selects the first element of this list. */
  def head: A = value.head // scalafix:ok

  /** Selects all but the first element of this list. */
  def tail: List[A] = value.tail

  /** Reduces the elements of this list using the specified associative binary operator.
    *
    * @param op a binary operator that must be associative
    * @return the result of applying reduce operator `op` between all the elements
    */
  def reduce(op: (A, A) => A): A = value.reduce(op)

  /** Transforms the elements of this list using the specified function.
    *
    * @param f a function to transform each element of this list
    * @return a new list resulting of applying the transforming function to each element of the list
    */
  def map[B](f: A => B): NonEmptyList[B] = NonEmptyList(value.map(f))

  /** Concatenates this list with another one. */
  def :::(list: NonEmptyList[A]): NonEmptyList[A] =
    NonEmptyList(value ++ list.value)

  /** Appends a new element to this list.
    *
    * @param a the element to append
    * @return the list with the provided element appended at the end
    */
  def :+(a: A): NonEmptyList[A] = NonEmptyList(value :+ a)

}

object NonEmptyList {

  implicit def NonEmptyListEncoder[A: Encoder]: Encoder[NonEmptyList[A]] =
    _.value.asYaml

  /** Creates a [[NonEmptyList]] from a list of elements. At least one element must be provided. */
  def of[A](head: A, tail: A*) = NonEmptyList(List(head) ++ tail.toList)

}
