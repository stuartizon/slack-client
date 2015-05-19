package com.ovoenergy.slack.util

import org.json4s.DefaultFormats
import org.json4s.native.JsonParser._
import spray.httpx.Json4sSupport

trait JsonSupport extends Json4sSupport {
  override implicit val json4sFormats = DefaultFormats.lossless

  implicit class MessageConversion(message: String) {
    def as[T](implicit manifest: Manifest[T]) = parse(message).extract[T]
  }
}