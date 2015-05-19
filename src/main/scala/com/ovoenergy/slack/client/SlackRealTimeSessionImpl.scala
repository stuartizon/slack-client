package com.ovoenergy.slack.client

import javax.websocket.{MessageHandler, Session}

import akka.event.LoggingAdapter
import com.ovoenergy.slack.api.Message.{Ping, IncomingMessage}
import com.ovoenergy.slack.api.{Message, OutgoingMessage, SlackRealTimeSession}
import com.ovoenergy.slack.util.JsonSupport

import org.json4s.native.Serialization._
import rx.lang.scala._

import scala.concurrent.duration._

class SlackRealTimeSessionImpl(session: Session)(implicit log: LoggingAdapter) extends SlackRealTimeSession with JsonSupport {
  private val messageIds = Iterator.from(1)

  Observable.interval(30 seconds).foreach {
    _ =>
      log.debug("ping")
      session.getAsyncRemote.sendText(write(Ping(messageIds.next)))
  }

  def sendMessage(channel: String, text: String) = {
    log.info("Sending message")
    session.getAsyncRemote.sendText(write(OutgoingMessage(messageIds.next, channel, text)))
  }

  def messages = Observable.create { observer =>
    session.addMessageHandler(new SlackMessageHandler(observer))
    Subscription()
  }
}

object SlackRealTimeSessionImpl {
  def apply(session: Session)(implicit log: LoggingAdapter) = new SlackRealTimeSessionImpl(session)
}

class SlackMessageHandler(messages: Observer[IncomingMessage])(implicit log: LoggingAdapter) extends MessageHandler.Whole[String] with JsonSupport {
  def onMessage(string: String) = {
    string.as[Message].`type` match {
      case Message.PongEvent => log.debug("Pong")
      case Message.HelloEvent => log.info("Connected")
      case Message.MessageEvent => messages.onNext(string.as[IncomingMessage])
    }
  }
}
