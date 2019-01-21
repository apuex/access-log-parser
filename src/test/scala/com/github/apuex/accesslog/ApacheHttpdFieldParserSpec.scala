package com.github.apuex.accesslog

import java.util.regex.Pattern

import com.github.apuex.accesslog.ApacheHttpdParser._
import com.github.apuex.accesslog.Field._
import org.scalatest._

class ApacheHttpdFieldParserSpec extends FlatSpec with Matchers {

  "An ApacheHttpdFieldParser" should "parse host" in {
    val pattern = Pattern.compile(fieldPatterns(Host))
    val expected = Array("192.168.0.1")
    tokenLine("192.168.0.1", pattern) should be(expected)
    tokenLine(" 192.168.0.1", pattern) should be(expected)
    tokenLine("\t192.168.0.1", pattern) should be(expected)
    tokenLine(" 192.168.0.1 ", pattern) should be(expected)
    tokenLine("\t192.168.0.1\t", pattern) should be(expected)
  }

  it should "parse remote login" in {
    val pattern = Pattern.compile(fieldPatterns(RemoteLogin))
    val expected = Array("-")
    tokenLine("-", pattern) should be(expected)
    tokenLine(" -", pattern) should be(expected)
    tokenLine(" - ", pattern) should be(expected)
  }

  it should "parse remote user" in {
    val pattern = Pattern.compile(fieldPatterns(RemoteUser))
    val expected = Array("-")
    tokenLine("-", pattern) should be(expected)
    tokenLine(" -", pattern) should be(expected)
    tokenLine(" - ", pattern) should be(expected)
  }

  it should "parse request time" in {
    val pattern = Pattern.compile(fieldPatterns(RequestTime))
    val expected = Array("[2019-01-21 15:17:33]")
    tokenLine("[2019-01-21 15:17:33]", pattern) should be(expected)
    tokenLine(" [2019-01-21 15:17:33]", pattern) should be(expected)
    tokenLine(" [2019-01-21 15:17:33] ", pattern) should be(expected)
  }

  it should "parse time taken" in {
    val pattern = Pattern.compile(fieldPatterns(TimeTaken))
    val expected = Array("123")
    tokenLine("123", pattern) should be(expected)
    tokenLine(" 123", pattern) should be(expected)
    tokenLine(" 123 ", pattern) should be(expected)
  }

  it should "parse request" in {
    val pattern = Pattern.compile(fieldPatterns(Request))
    val expected = Array("GET /bugzilla/images/favicon.ico HTTP/1.1")
    tokenLine("\"GET /bugzilla/images/favicon.ico HTTP/1.1\"", pattern) should be(expected)
    tokenLine(" \"GET /bugzilla/images/favicon.ico HTTP/1.1\"", pattern) should be(expected)
    tokenLine(" \"GET /bugzilla/images/favicon.ico HTTP/1.1\" ", pattern) should be(expected)
  }

  it should "parse status code" in {
    val pattern = Pattern.compile(fieldPatterns(Status))
    val expected = Array("201")
    tokenLine("201", pattern) should be(expected)
    tokenLine(" 201", pattern) should be(expected)
    tokenLine(" 201 ", pattern) should be(expected)
  }

  it should "parse body length" in {
    val pattern = Pattern.compile(fieldPatterns(BodyLength))
    val expected = Array("123")
    tokenLine("123", pattern) should be(expected)
    tokenLine(" 123", pattern) should be(expected)
    tokenLine(" 123 ", pattern) should be(expected)
  }

  it should "parse referer" in {
    val pattern = Pattern.compile(fieldPatterns(Referer))
    val expected = Array("http://192.168.0.161/bugzilla/userprefs.cgi")
    tokenLine("\"http://192.168.0.161/bugzilla/userprefs.cgi\"", pattern) should be(expected)
    tokenLine(" \"http://192.168.0.161/bugzilla/userprefs.cgi\"", pattern) should be(expected)
    tokenLine(" \"http://192.168.0.161/bugzilla/userprefs.cgi\" ", pattern) should be(expected)
  }

  it should "parse user agent" in {
    val pattern = Pattern.compile(fieldPatterns(UserAgent))
    val expected = Array("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:64.0) Gecko/20100101 Firefox/64.0")
    tokenLine("\"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:64.0) Gecko/20100101 Firefox/64.0\"", pattern) should be(expected)
    tokenLine(" \"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:64.0) Gecko/20100101 Firefox/64.0\"", pattern) should be(expected)
    tokenLine(" \"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:64.0) Gecko/20100101 Firefox/64.0\" ", pattern) should be(expected)
  }

  it should "parse bytes received" in {
    val pattern = Pattern.compile(fieldPatterns(BytesReceived))
    val expected = Array("123")
    tokenLine("123", pattern) should be(expected)
    tokenLine(" 123", pattern) should be(expected)
    tokenLine(" 123 ", pattern) should be(expected)
  }

  it should "parse bytes sent" in {
    val pattern = Pattern.compile(fieldPatterns(BytesSent))
    val expected = Array("123")
    tokenLine("123", pattern) should be(expected)
    tokenLine(" 123", pattern) should be(expected)
    tokenLine(" 123 ", pattern) should be(expected)
  }

  it should "parse 192.168.0.78 - - [2019-01-21 15:17:54] 738068 \"GET /bugzilla/userprefs.cgi?tab=email HTTP/1.1\" 200 21797 \"http://192.168.0.161/bugzilla/userprefs.cgi\" \"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:64.0) Gecko/20100101 Firefox/64.0\" 798 22397" in {
    val linePattern = Pattern.compile(
      Array(
        Host,
        RemoteLogin,
        RemoteUser,
        RequestTime,
        TimeTaken,
        Request,
        Status,
        BodyLength,
        Referer,
        UserAgent,
        BytesReceived,
        BytesSent
      )
        .map(fieldPatterns(_))
        .foldLeft("")((l, x) => l + x)
    )
    val expected = Array("192.168.0.78", "-", "-", "[2019-01-21 15:17:54]", "738068", "GET /bugzilla/userprefs.cgi?tab=email HTTP/1.1", "200", "21797", "http://192.168.0.161/bugzilla/userprefs.cgi", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:64.0) Gecko/20100101 Firefox/64.0", "798", "22397")
    tokenLine("192.168.0.78 - - [2019-01-21 15:17:54] 738068 \"GET /bugzilla/userprefs.cgi?tab=email HTTP/1.1\" 200 21797 \"http://192.168.0.161/bugzilla/userprefs.cgi\" \"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:64.0) Gecko/20100101 Firefox/64.0\" 798 22397", linePattern) should be(expected)
  }
}
