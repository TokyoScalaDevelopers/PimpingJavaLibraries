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
