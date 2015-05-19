package com.ovoenergy.slack.api

trait SlackApi {
  def auth: AuthApi

  def channels: ChannelsApi

  def chat: ChatApi

  def emoji: EmojiApi

  def files: FilesApi

  def groups: GroupsApi

  def im: InstantMessageApi

  def rtm: RealTimeMessageApi

  def search: SearchApi

  def stars: StarsApi

  def team: TeamApi

  def users: UsersApi
}

trait AuthApi {
  def test: Response[AuthTest]
}

trait ChannelsApi {
  // TODO methods to go here
}

trait ChatApi {
  def delete(message: ChatMessage): Response[ChatMessageDelete]

  def postMessage(channel: String, text: String, username: Option[String], as_user: Option[Boolean]): Response[ChatMessagePost]

  def update(message: ChatMessage, text: String) : Response[ChatMessageUpdate]
}

trait EmojiApi {
  def list: Response[EmojiList]
}

trait FilesApi {
  // TODO methods to go here
}

trait GroupsApi {
  // TODO methods to go here
}

trait InstantMessageApi {
  def open(user: String): Response[InstantMessageOpen]

  def close(channel: String): Response[InstantMessageClose]

  def list: Response[InstantMessageList]

  def mark(channel: String, ts: String): Response[Unit]
}

trait RealTimeMessageApi {
  def start: Response[SlackRealTimeSession]
}

trait SearchApi {
  // TODO methods to go here
}

trait StarsApi {
  // TODO methods to go here
}

trait TeamApi {
  // TODO methods to go here
}

trait UsersApi {
  def info(user: String): Response[UserInfo]

  def list: Response[UserList]

  // TODO methods to go here
}