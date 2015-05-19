package com.ovoenergy.slack.api

case class Message(`type`: String)

case class Reply(ok: Boolean, `reply_to`: Int)
case class ReplySuccess(ts: String, text: String)
case class ReplyFailure(error: ReplyError)
case class ReplyError(code: Int, msg: String)

//object EventType extends Enumeration {
//  val Hello =
//}

object Message {
  val HelloEvent = "hello"
//  val PingEvent = "ping"
  val PongEvent = "pong"
  val MessageEvent = "message"

  case class Ping(id: Int, `type`: String = "ping")

  case class Pong(reply_to: Int, `type`: String = PongEvent)

  case class IncomingMessage(channel: String, user: String, text: String, ts: String, team: String, `type`: String = MessageEvent)
}

//removed id :Int
case class OutgoingMessage(id: Int, channel: String, text: String, `type`: String="message")

case object SendPing