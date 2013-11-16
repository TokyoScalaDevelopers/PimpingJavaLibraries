package org.codehaus.jackson.extended

import scala.language.implicitConversions

import org.codehaus.jackson.JsonNode
import org.codehaus.jackson.node.{BooleanNode, NullNode, TextNode, BigIntegerNode, DecimalNode, DoubleNode, IntNode, LongNode}

object Helpers {
  def text(string: String): JsonNode = new TextNode(string)
  def bool(bool: Boolean): JsonNode = boolean(bool)
  def boolean(bool: Boolean): JsonNode = if(bool) BooleanNode.TRUE else BooleanNode.FALSE
  def `null`: JsonNode = NullNode.instance

  trait NumericConverter[T] {
    def convert(num: T): JsonNode
  }
  implicit val convertInt = new NumericConverter[Int] { def convert(num: Int) = new IntNode(num) }
  implicit val convertLong = new NumericConverter[Long] { def convert(num: Long) = new LongNode(num) }
  implicit val convertBigInteger = new NumericConverter[java.math.BigInteger] { def convert(num: java.math.BigInteger) = new BigIntegerNode(num) }
  implicit val convertDouble = new NumericConverter[Double] { def convert(num: Double) = new DoubleNode(num) }

  def numeric[T](num: T)(implicit nc: NumericConverter[T]): JsonNode = nc.convert(num)
}
