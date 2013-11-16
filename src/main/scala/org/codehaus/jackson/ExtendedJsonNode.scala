package org.codehaus.jackson.extended

import scala.language.implicitConversions
import scala.util.{Try,Success,Failure}

import org.codehaus.jackson.JsonNode

class ExtendedJsonNode(val node: JsonNode) {
  def apply[T](key: T): JsonNode = ???
  def lift[T](key: T): Option[JsonNode] = ???
}
