package org.codehaus.jackson.extended

import scala.language.implicitConversions
import scala.util.{Try,Success,Failure}

import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.JsonNode

class ExtendedObjectMapper(val mapper: ObjectMapper) {
  def parse[T](data: T): Try[JsonNode] = ???
}
