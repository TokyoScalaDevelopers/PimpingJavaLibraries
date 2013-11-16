package org.codehaus.jackson.extended

import scala.language.implicitConversions
import scala.util.{Try,Success,Failure}

import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.JsonNode
import org.codehaus.jackson.node.{ObjectNode,ArrayNode}

// Readable[T] imports
import org.codehaus.jackson.JsonParser
import java.io.InputStream
import java.net.URL
import java.io.File
import java.io.Reader

trait Readable[T] {
  def read(mapper: ObjectMapper, data: T): JsonNode
}

class ExtendedObjectMapper(val mapper: ObjectMapper) {
  def parse[T](data: T)(implicit ev: Readable[T]): Try[JsonNode] = Try(ev.read(mapper, data))
  def createObject: ObjectNode = mapper.createObjectNode
  def createArray: ArrayNode = mapper.createArrayNode
}

trait ObjectMapperImplicits {
  implicit val readableURL = new Readable[URL] { def read(mapper: ObjectMapper, data: URL) = mapper.readTree(data) }
  implicit val readableFile = new Readable[File] { def read(mapper: ObjectMapper, data: File) = mapper.readTree(data)}
  implicit val readableReader = new Readable[Reader] { def read(mapper: ObjectMapper, data: Reader) = mapper.readTree(data)}
  implicit val readableString = new Readable[String] { def read(mapper: ObjectMapper, data: String) = mapper.readTree(data)}
  implicit val readableByteArray = new Readable[Array[Byte]] { def read(mapper: ObjectMapper, data: Array[Byte]) = mapper.readTree(data)}
  implicit val readableJsonParser = new Readable[JsonParser] { def read(mapper: ObjectMapper, data: JsonParser) = mapper.readTree(data)}
  implicit val readableInputStream = new Readable[InputStream] { def read(mapper: ObjectMapper, data: InputStream) = mapper.readTree(data)}

  implicit def extendObjectMapper(mapper: ObjectMapper): ExtendedObjectMapper = new ExtendedObjectMapper(mapper)
}
