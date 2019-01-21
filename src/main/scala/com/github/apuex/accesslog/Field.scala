package com.github.apuex.accesslog

object Field extends Enumeration {
  type Field = Value
  val Host, RemoteLogin, RemoteUser, RequestTimeIO, RequestTime, TimeTaken, Request, Status,
  BodyLength, Referer, UserAgent, BytesReceived, BytesSent = Value
}
