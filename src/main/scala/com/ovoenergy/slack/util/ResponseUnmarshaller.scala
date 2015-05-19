package com.ovoenergy.slack.util

import com.ovoenergy.slack.api.{ResponseStatus, ResponseError, SlackError}
import spray.client.pipelining.unmarshal
import spray.http.HttpResponse
import spray.httpx.unmarshalling.FromResponseUnmarshaller

import scala.concurrent.{Future, ExecutionContext}
import scalaz.{Failure, Success, Validation}

trait ResponseUnmarshaller {
  def unmarshalError(implicit ua: FromResponseUnmarshaller[ResponseError]): HttpResponse => SlackError

  implicit def toFutureValidation[T](f: Future[Validation[SlackError, T]])(implicit ec: ExecutionContext): FutureValidation[SlackError, T] = FutureValidation(f)

  def unmarshalResponse[T](implicit ua:FromResponseUnmarshaller[ResponseStatus], ub: FromResponseUnmarshaller[ResponseError], uc: FromResponseUnmarshaller[T]): HttpResponse=>Validation[SlackError, T] = {
    hr => unmarshal[ResponseStatus].apply(hr) match {
      case ResponseStatus(true) => Success(unmarshal[T].apply(hr))
      case ResponseStatus(false) => Failure(unmarshalError.apply(hr))
    }
  }
}