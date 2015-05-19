package com.ovoenergy.slack.api

sealed trait SlackError {
  def message: String
}

case object InvalidAuth extends SlackError {
  val message = "Invalid authentication token"
}

case object MigrationInProgress extends SlackError {
  val message = "Team is being migrated between servers"
}

case object NotAuthenticated extends SlackError {
  val message = "No authentication token provided"
}

case object NotInChannel extends SlackError {
  val message = "Caller is not a member of the channel"
}

case object AccountInactive extends SlackError {
  val message = "Authentication token is for a deleted user or team"
}

case object UserNotFound extends SlackError {
  val message = "Value passed for user was invalid"
}

case object UserNotVisible extends SlackError {
  val message = "The calling user is restricted from seeing the requested user"
}

case object MessageNotFound extends SlackError {
  val message = "No message exists with the requested timestamp"
}

case object CantUpdateMessage extends SlackError {
  val message = "Authenticated user does not have permission to update this message"
}

case object CantDeleteMessage extends SlackError {
  val message = "Authenticated user does not have permission to delete this message"
}

case object ChannelNotFound extends SlackError {
  val message = "Value passed for channel was invalid"
}

case object UserDoesNotOwnChannel extends SlackError {
  val message = "Calling user does not own this DM channel"
}

case object EditWindowClosed extends SlackError {
  val message = "The message cannot be edited due to the team message edit settings"
}

case object MessageTooLong extends SlackError {
  val message = "Message text is too long"
}

case object NoText extends SlackError {
  val message = "No message text provided"
}

case object InvalidLatestTimestamp extends SlackError {
  val message = "Value passed for latest was invalid"
}

case object InvalidOldestTimestamp extends SlackError {
  val message = "Value passed for oldest was invalid"
}

case object UnknownError extends SlackError {
  val message = "Unknown error"
}
