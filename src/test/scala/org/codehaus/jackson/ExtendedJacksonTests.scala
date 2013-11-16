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

object ExtendedObjectMapperTests extends Properties("ExtendedObjectMapper") {
  property("canParseValid") = {
    (new ObjectMapper).parse(TestData.talk(0)).isSuccess
  }

  property("canHandleFailure") = {
    (new ObjectMapper).parse(TestData.invalid(0)).isFailure
  }
}
