import org.scalacheck.Properties
import org.scalacheck.Prop.forAll

import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.JsonNode

import org.codehaus.jackson.extended._
import org.codehaus.jackson.extended.implicits._

import util.{Try,Success,Failure}

object TestData {
  val talk = List(
    """{"kind":"talk","user":"Robot","message":"I'm still alive","members":["Robot","devon"]}""",
    """{"kind":"talk","user":"devon","message":"Yo","members":["Robot","devon"]}"""
  )

  val joinPart = List(
    """{"kind":"quit","user":"foo","message":"has left the room","members":["Robot","devon"]}""",
    """{"kind":"join","user":"foo","message":"has entered the room","members":["Robot","devon","foo"]}""",
    """{"kind":"join","user":"devon","message":"has entered the room","members":["Robot","devon"]}"""
  )

  val send = List(
    """{"text":"Yo"}"""
  )

  val allMessages = talk ::: joinPart ::: send

  val invalid = List(
    """{"kind":"talk","user":"Robot","me""" // ssage":"I'm still alive","members":["Robot","devon"]}"""
  )
}

trait SharedMapper {
  def mapper = new ObjectMapper
}

object ExtendedObjectMapperTests extends Properties("ExtendedObjectMapper") with SharedMapper {
  property("canParseValid") = {
    mapper.parse(TestData.talk(0)).isSuccess
  }

  property("canHandleFailure") = {
    mapper.parse(TestData.invalid(0)).isFailure
  }
}

object ExtendedJsonNodeTests extends Properties("ExtendedJsonNode") with SharedMapper {
  property("apply") = {
    val data = TestData.joinPart(1)
    mapper.parse(data).get.apply("message") == Helpers.text("has entered the room")
  }

  property("hiddenApply") = { // This test is almost exactly like the last test, but since Option.get is a unary function, we can call JsonNode's apply like this.
    val data = TestData.joinPart(1)
    mapper.parse(data).get("message") == Helpers.text("has entered the room")
  }

  property("getIndex") = {
    val data = TestData.joinPart(1)
    mapper.parse(data).get.apply("members").apply(0) == Helpers.text("Robot")
  }

  property("liftGetKey") = {
    val data = TestData.joinPart(1)
    mapper.parse(data).toOption.flatMap( _.lift("message") ) == Some(Helpers.text("has entered the room"))
  }

  property("getIndex") = {
    val data = TestData.joinPart(1)
    mapper.parse(data).toOption.flatMap( _.lift("members") ).flatMap( _.lift(0) ) == Some(Helpers.text("Robot"))
  }

  property("getNiceIndex") = {
    val data = TestData.joinPart(1)
    mapper.parse(data) / "members" / 0 == Some(Helpers.text("Robot"))
  }

  property("getInvalidNiceIndex") = {
    val data = TestData.joinPart(1)
    mapper.parse(data) / "foo" / 100 == None
  }

  property("getOrElse") = {
    val data = TestData.joinPart(2)
    mapper.parse(data).get.getOrElse("user", Helpers.text("fallback")) == Helpers.text("devon")
  }

  property("getOrElse2") = {
    val data = TestData.joinPart(2)
    mapper.parse(data).get.getOrElse("missingKey", Helpers.text("fallback")) == Helpers.text("fallback")
  }
}

object JsonNodeHelperTests extends Properties("Helpers") with SharedMapper {
  property("buildIntegerNode") = {
    Helpers.numeric(1234:Int).toString == "1234"
  }

  property("buildLongNode") = {
    Helpers.numeric(1234:Long).toString == "1234"
  }

  property("buildBigIntegerNode") = {
    Helpers.numeric(new java.math.BigInteger("1234")).toString == "1234"
  }

  property("buildDoubleNode") = {
    Helpers.numeric(1234.0: Double).toString == "1234.0"
  }

  property("buildTrue") = {
    Helpers.bool(true).toString == "true" && Helpers.boolean(true).toString == "true"
  }

  property("buildFalse") = {
    Helpers.bool(false).toString == "false" && Helpers.boolean(false).toString == "false"
  }

  property("buildNull") = {
    Helpers.`null`.toString == "null"
  }

  property("buildString") = {
    Helpers.text("""This is a string "with quotes".""").toString == "\"" + """This is a string \"with quotes\".""" + "\""
  }
}

object BuildJsonTests extends Properties("<Build Json>") with SharedMapper {
  property("emptyObject") = {
    mapper.createObject.toString == "{}"
  }

  property("emptyArray") = {
    mapper.createArray.toString == "[]"
  }
}
