package org.codehaus.jackson.extended

import scala.language.implicitConversions
import scala.util.{Try,Success,Failure}

import org.codehaus.jackson.JsonNode

trait Extractable[T] {
  def extract(node: JsonNode, key: T): JsonNode
}

class ExtendedJsonNode(val node: JsonNode) {
  def apply[T](key: T)(implicit ev: Extractable[T]): JsonNode = ev.extract(node, key)
  def lift[T](key: T)(implicit ev: Extractable[T]): Option[JsonNode] = Option(ev.extract(node, key))
}

class MaybeJsonNode(val node: Option[JsonNode]) {
  def /[T](key: T)(implicit ev: Extractable[T]): Option[JsonNode] = node.flatMap({ node => Option(ev.extract(node, key)) })
}

trait JsonNodeImplicits {
  implicit val extractKey = new Extractable[String] { def extract(node: JsonNode, key: String) = node.get(key) }
  implicit val extractIndex = new Extractable[Int] { def extract(node: JsonNode, key: Int) = node.get(key) }

  implicit def extendJsonNode(node: JsonNode): ExtendedJsonNode = new ExtendedJsonNode(node)
  implicit def tryJsonToMaybeJsonNode(wrapped: Try[JsonNode]): MaybeJsonNode = extendMaybeJsonNode(wrapped.toOption)
  implicit def extendMaybeJsonNode(node: Option[JsonNode]): MaybeJsonNode = new MaybeJsonNode(node)
}
