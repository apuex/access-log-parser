package com.github.apuex.accesslog

import java.util.regex.Pattern

import com.github.apuex.accesslog.ApacheHttpdParser._
import com.github.apuex.accesslog.Field._

object ApacheHttpdParser {
  // %h %l %u [%{%Y-%m-%d %H:%M:%S}t] %D \"%r\" %>s %b \"%{Referer}i\" \"%{User-Agent}i\" %I %O

  val fieldPatterns: Map[Field, String] = Map(
    Host           -> "\\s*([\\d.:]+)",   // %h, host
    RemoteLogin    -> "\\s*(\\S+)",   // %l, remote logname
    RemoteUser     -> "\\s*(\\S+)",   // %u, remote user
    RequestTime    -> "\\s*\\[([\\w:/]+\\s[+\\-]\\d{4})\\]",   // %t, request time
    RequestTimeIO  -> "\\s*\\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\]",   // %t, request time
    TimeTaken      -> "\\s*(\\d+)",   // %D, time taken
    Request        -> "\\s*\"(.+)\"",   // %r, request
    Status         -> "\\s*(\\d{3})",   // %>s, status code
    BodyLength     -> "\\s*(\\d+)",   // %b, body length
    Referer        -> "\\s*\"([^\"]+|(.+?))\"",   // %{Referer}i,
    UserAgent      -> "\\s*\"([^\"]+|(.+?))\"",   // %{User-Agent}i,
    BytesReceived  -> "\\s*(\\d+)",   // %I, bytes received
    BytesSent      -> "\\s*(\\d+)"    // %O, bytes sent
  )

  def tokenLine(line: String, logPattern: Pattern): Array[String] = {
    val m = logPattern.matcher(line)
    if(m.find()) {
      val result: Array[String] = new Array(m.groupCount())
      (1 to m.groupCount()).foreach(x => result(x - 1) = m.group(x))
      result
    } else {
      Array()
    }
  }
}

class ApacheHttpdParser (fields: Array[Field]) {

  val logPattern = Pattern.compile(fields.foldLeft("")((s, f) => s + fieldPatterns(f)))

  def tokenLine(line: String): Array[String] = {
    val m = logPattern.matcher(line)
    if(m.find()) {
      val result: Array[String] = new Array(m.groupCount())
      (1 to m.groupCount()).foreach(x => result(x - 1) = m.group(x))
      result
    } else {
      Array()
    }
  }
}
