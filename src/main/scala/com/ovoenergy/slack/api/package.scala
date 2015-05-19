package com.ovoenergy.slack

import com.ovoenergy.slack.api.Message.IncomingMessage
import com.ovoenergy.slack.util.FutureValidation

package object api {
  type Response[T] = FutureValidation[SlackError, T]

  type MessageFunction = PartialFunction[IncomingMessage, Unit]
}