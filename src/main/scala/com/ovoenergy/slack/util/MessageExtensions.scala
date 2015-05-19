package com.ovoenergy.slack.util

import com.ovoenergy.slack.api.Message.IncomingMessage
import com.ovoenergy.slack.api.SlackRealTimeSession

trait MessageExtensions {

  implicit class IncomingMessageExtension(message: IncomingMessage) {
    def reply(text: String)(implicit session: SlackRealTimeSession) = session.sendMessage(message.channel, text)
  }

}