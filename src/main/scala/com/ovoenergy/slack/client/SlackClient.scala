package com.ovoenergy.slack.client

import java.net.URI
import javax.websocket.{EndpointConfig, Endpoint, Session}

import akka.actor.ActorRefFactory
import akka.event.LoggingAdapter
import com.ovoenergy.slack.api._
import com.ovoenergy.slack.util.{FutureValidation, ResponseUnmarshaller, JsonSupport}
import org.glassfish.tyrus.client.ClientManager

import spray.client.pipelining._

import com.netaporter.uri.dsl._

class SlackClient(val token: String)(implicit actorRefFactory: ActorRefFactory, log: LoggingAdapter) extends SlackApi with JsonSupport with ResponseUnmarshaller with SlackErrorHandler {
  val baseUrl = "https://slack.com/api"

  import actorRefFactory.dispatcher

  object auth extends AuthApi {
    def test = {
      val pipeline = sendReceive ~> unmarshalResponse[AuthTest]
      log.info("Authenticating...")

      pipeline {
        val uri = (baseUrl / "auth.test") ? ("token" -> token)
        Get(uri)
      }
    }
  }

  object channels extends ChannelsApi

  object chat extends ChatApi {
    def postMessage(channel: String, text: String, username: Option[String] = None, as_user: Option[Boolean] = None) = {
      val pipeline = sendReceive ~> unmarshalResponse[ChatMessagePost]
      log.info(s"Posting message to channel $channel")

      pipeline {
        val uri = (baseUrl / "chat.postMessage") ? ("token" -> token) & ("channel" -> channel) & ("text" -> text) & ("username" -> username) & ("as_user" -> as_user)
        Get(uri)
      }
    }

    def update(message: ChatMessage, text: String) = {
      val pipeline = sendReceive ~> unmarshalResponse[ChatMessageUpdate]
      log.info(s"Updating message $message")

      pipeline {
        val uri = (baseUrl / "chat.update") ? ("token" -> token) & ("ts" -> message.ts) & ("channel" -> message.channel) & ("text" -> text)
        Get(uri)
      }
    }

    def delete(message: ChatMessage) = {
      val pipeline = sendReceive ~> unmarshalResponse[ChatMessageDelete]
      log.info(s"Deleting message $message")

      pipeline {
        val uri = (baseUrl / "chat.delete") ? ("token" -> token) & ("ts" -> message.ts) & ("channel" -> message.channel)
        Get(uri)
      }
    }
  }

  object emoji extends EmojiApi {
    def list = {
      val pipeline = sendReceive ~> unmarshalResponse[EmojiList]
      log.info("Requesting list of emoji")

      pipeline {
        val uri = (baseUrl / "emoji.list") ? ("token" -> token)
        Get(uri)
      }
    }
  }

  object files extends FilesApi

  object groups extends GroupsApi

  object im extends InstantMessageApi {
    def open(user: String) = {
      val pipeline = sendReceive ~> unmarshalResponse[InstantMessageOpen]
      log.info(s"Opening IM channel to '$user'")

      pipeline {
        val uri = (baseUrl / "im.open") ? ("token" -> token) & ("user" -> user)
        Get(uri)
      }
    }

    def close(channel: String) = {
      val pipeline = sendReceive ~> unmarshalResponse[InstantMessageClose]
      log.info(s"Closing IM channel $channel")

      pipeline {
        val uri = (baseUrl / "im.close") ? ("token" -> token) & ("channel" -> channel)
        Get(uri)
      }
    }

    def list = {
      val pipeline = sendReceive ~> unmarshalResponse[InstantMessageList]
      log.info(s"Listing IM channels")

      pipeline {
        val uri = (baseUrl / "im.list") ? ("token" -> token)
        Get(uri)
      }
    }

    def mark(channel: String, ts: String) = {
      val pipeline = sendReceive ~> unmarshalResponse[Unit]
      log.info(s"Marking")

      pipeline {
        val uri = (baseUrl / "im.mark") ? ("token" -> token) & ("channel" -> channel) & ("ts" -> ts)
        Get(uri)
      }
    }
  }

  object rtm extends RealTimeMessageApi {
    def start = {
      def getUrl: Response[RtmStart] = {
        val pipeline = sendReceive ~> unmarshalResponse[RtmStart]
        log.info("Connecting to Slack...")

        pipeline {
          val uri = (baseUrl / "rtm.start") ? ("token" -> token)
          Get(uri)
        }
      }

      def connect(url: String): Response[Session] = {
        val client = ClientManager.createClient
        object EP extends Endpoint {
          override def onOpen(session: Session, endpointConfig: EndpointConfig) = ()
        }

        FutureValidation.success {
          client.connectToServer(EP, new URI(url))
        }
      }

      for {
        start <- getUrl
        session <- connect(start.url)
      } yield SlackRealTimeSessionImpl(session)
    }
  }

  object search extends SearchApi

  object stars extends StarsApi

  object team extends TeamApi

  object users extends UsersApi {
    def info(user: String) = {
      val pipeline = sendReceive ~> unmarshalResponse[UserInfo]
      log.info(s"Querying for user $user")

      pipeline {
        val uri = (baseUrl / "users.info") ? ("token" -> token) & ("user" -> user)
        Get(uri)
      }
    }

    def list = {
      val pipeline = sendReceive ~> unmarshalResponse[UserList]
      log.info("Listing users")

      pipeline {
        val uri = (baseUrl / "users.list") ? ("token" -> token)
        Get(uri)
      }
    }
  }
}

object SlackClient {
  def apply(token: String)(implicit actorRefFactory: ActorRefFactory, log: LoggingAdapter) = new SlackClient(token)
}