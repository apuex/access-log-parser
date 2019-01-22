package com.github.apuex.accesslog

object Field extends Enumeration {
  type Field = Value
  val Host, RemoteLogin, RemoteUser, RequestTimeIO, RequestTime, TimeTaken, Request, StatusCode,
  BodyLength, Referer, UserAgent, BytesReceived, BytesSent, VirtualHost = Value
}
