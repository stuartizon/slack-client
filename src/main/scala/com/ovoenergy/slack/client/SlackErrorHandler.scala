package com.ovoenergy.slack.client

import com.ovoenergy.slack.api._
import spray.client.pipelining._
import spray.http.HttpResponse
import spray.httpx.unmarshalling._

trait SlackErrorHandler {
  def unmarshalError(implicit ua: FromResponseUnmarshaller[ResponseError]): HttpResponse => SlackError = {
    unmarshal[ResponseError].andThen {
      case ResponseError("invalid_auth") => InvalidAuth
      case ResponseError("migration_in_progress") => MigrationInProgress
      case ResponseError("not_authed") => NotAuthenticated
      case ResponseError("account_inactive") => AccountInactive
      case ResponseError("user_not_found") => UserNotFound
      case ResponseError("user_not_visible") => UserNotVisible
      case ResponseError("message_not_found") => MessageNotFound
      case ResponseError("cant_update_message") => CantUpdateMessage
      case ResponseError("cant_delete_message") => CantDeleteMessage
      case ResponseError("channel_not_found") => ChannelNotFound
      case ResponseError("edit_window_closed") => EditWindowClosed
      case ResponseError("msg_too_long") => MessageTooLong
      case ResponseError("no_text") => NoText
      case ResponseError("invalid_ts_latest") => InvalidLatestTimestamp
      case ResponseError("invalid_ts_oldest") => InvalidOldestTimestamp
      case ResponseError("not_in_channel") => NotInChannel
      case _ => UnknownError
    }
  }
}