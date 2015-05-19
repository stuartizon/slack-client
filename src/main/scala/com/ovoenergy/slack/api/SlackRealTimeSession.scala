package com.ovoenergy.slack.api

import com.ovoenergy.slack.api.Message.IncomingMessage
import rx.lang.scala.Observable

trait SlackRealTimeSession {
  def sendMessage(channel: String, text: String) : Unit

  // TODO: Change to the general event case...
  def messages: Observable[IncomingMessage]
}