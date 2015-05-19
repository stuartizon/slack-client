package com.ovoenergy.slack.api

case class ResponseStatus(ok: Boolean)
case class ResponseError(error: String)

case class AuthTest(url: String, team: String, user: String, team_id:String, user_id: String)

trait ChatMessage {
  def ts: String
  def channel: String
}

case class ChatMessagePost(channel: String, ts: String) extends ChatMessage

case class ChatMessageDelete(channel: String, ts: String) extends ChatMessage

case class ChatMessageUpdate(channel: String, ts: String, text: String) extends ChatMessage

case class EmojiList(emoji: Map[String, String])

case class InstantMessageOpen(channel: InstantMessageChannelId, no_op: Option[Boolean], already_open: Option[Boolean])

case class InstantMessageClose(no_op: Option[Boolean], already_open: Option[Boolean])

case class InstantMessageList(ims: List[InstantMessageChannel])

case class InstantMessageChannelId(id: String)

case class InstantMessageChannel(id: String, is_im: Boolean, user: String, created: String, is_user_deleted: Boolean)

case class RtmSelf(id: String, name:String, created: Int, manual_presence: String)

case class RtmStart(url: String, self: RtmSelf)

case class UserProfile(first_name: Option[String], last_name: Option[String], real_name: Option[String], email: Option[String], skype: Option[String], phone: Option[String], image_24: String, image_32: String, image_48: String, image_72: String, image_192: String)

case class User(id: String, name: String, deleted: Boolean, color: String, profile: UserProfile, is_admin: Boolean, is_owner: Boolean, has_2fa: Option[Boolean], has_files: Boolean)

case class UserInfo(user: User)

case class UserList(members: List[User])