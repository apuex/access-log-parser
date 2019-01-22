package com.github.apuex.accesslog

import java.util.regex.Pattern

import com.github.apuex.accesslog.ApacheHttpdParser._
import com.github.apuex.accesslog.Field._
import org.scalatest._

class ApacheHttpdFieldParserSpec extends FlatSpec with Matchers {

  "An ApacheHttpdFieldParser" should "parse IPv4 host" in {
    val pattern = Pattern.compile(fieldPatterns(Host))
    val expected = Array("192.168.0.1")
    tokenLine("192.168.0.1", pattern) should be(expected)
    tokenLine(" 192.168.0.1", pattern) should be(expected)
    tokenLine("\t192.168.0.1", pattern) should be(expected)
    tokenLine(" 192.168.0.1 ", pattern) should be(expected)
    tokenLine("\t192.168.0.1\t", pattern) should be(expected)
  }

  "An ApacheHttpdFieldParser" should "parse IPv6 host" in {
    val pattern = Pattern.compile(fieldPatterns(Host))
    val expected = Array("::1")
    tokenLine("::1", pattern) should be(expected)
    tokenLine(" ::1", pattern) should be(expected)
    tokenLine(" ::1 ", pattern) should be(expected)
    tokenLine("\t::1", pattern) should be(expected)
    tokenLine("\t::1\t", pattern) should be(expected)
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
    val expected = Array("11/Jan/2018:22:10:43 +0800")
    tokenLine("[11/Jan/2018:22:10:43 +0800]", pattern) should be(expected)
    tokenLine(" [11/Jan/2018:22:10:43 +0800]", pattern) should be(expected)
    tokenLine(" [11/Jan/2018:22:10:43 +0800] ", pattern) should be(expected)
  }

  it should "parse request time io" in {
    val pattern = Pattern.compile(fieldPatterns(RequestTimeIO))
    val expected = Array("2019-01-21 15:17:33")
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
    val pattern = Pattern.compile(fieldPatterns(StatusCode))
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
    tokenLine("\"http://192.168.0.161/bugzilla/userprefs.cgi\"", pattern).filter(_ != null) should be(expected)
    tokenLine(" \"http://192.168.0.161/bugzilla/userprefs.cgi\"", pattern).filter(_ != null) should be(expected)
    tokenLine(" \"http://192.168.0.161/bugzilla/userprefs.cgi\" ", pattern).filter(_ != null) should be(expected)
  }

  it should "parse user agent" in {
    val pattern = Pattern.compile(fieldPatterns(UserAgent))
    val expected = Array("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:64.0) Gecko/20100101 Firefox/64.0")
    tokenLine("\"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:64.0) Gecko/20100101 Firefox/64.0\"", pattern).filter(_ != null) should be(expected)
    tokenLine(" \"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:64.0) Gecko/20100101 Firefox/64.0\"", pattern).filter(_ != null) should be(expected)
    tokenLine(" \"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:64.0) Gecko/20100101 Firefox/64.0\" ", pattern).filter(_ != null) should be(expected)
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

  it should "parse combinedio(time taken added)" in {
    val linePattern = Pattern.compile(
      Array(
        Host,
        RemoteLogin,
        RemoteUser,
        RequestTimeIO,
        TimeTaken,
        Request,
        StatusCode,
        BodyLength,
        Referer,
        UserAgent,
        BytesReceived,
        BytesSent
      )
        .map(fieldPatterns(_))
        .foldLeft("")((l, x) => l + x)
    )
    val expected = Array("192.168.0.78", "-", "-", "2019-01-21 15:17:54", "738068", "GET /bugzilla/userprefs.cgi?tab=email HTTP/1.1", "200", "21797", "http://192.168.0.161/bugzilla/userprefs.cgi", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:64.0) Gecko/20100101 Firefox/64.0", "798", "22397")
    tokenLine("192.168.0.78 - - [2019-01-21 15:17:54] 738068 \"GET /bugzilla/userprefs.cgi?tab=email HTTP/1.1\" 200 21797 \"http://192.168.0.161/bugzilla/userprefs.cgi\" \"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:64.0) Gecko/20100101 Firefox/64.0\" 798 22397", linePattern).filter(_ != null) should be(expected)
  }

  it should "parse common" in {
    // standard `common` format.
    val linePattern = Pattern.compile(
      Array(
        Host,
        RemoteLogin,
        RemoteUser,
        RequestTime,
        Request,
        StatusCode,
        BodyLength
      )
        .map(fieldPatterns(_))
        .foldLeft("")((l, x) => l + x)
    )
    val expected = Array("0:0:0:0:0:0:0:1", "-", "-", "02/Jul/2017:14:17:36 +0800", "POST /my-12306/login?action=login HTTP/1.1", "200", "4133")
    tokenLine("0:0:0:0:0:0:0:1 - - [02/Jul/2017:14:17:36 +0800] \"POST /my-12306/login?action=login HTTP/1.1\" 200 4133", linePattern).filter(_ != null) should be(expected)
  }

  it should "parse combined" in {
    // standard `combined` format.
    val linePattern = Pattern.compile(
      Array(
        Host,
        RemoteLogin,
        RemoteUser,
        RequestTime,
        Request,
        StatusCode,
        BodyLength,
        Referer,
        UserAgent
      )
        .map(fieldPatterns(_))
        .foldLeft("")((l, x) => l + x)
    )
    val expected = Array("::1", "-", "-", "11/Jan/2018:22:10:43 +0800", "GET /~master/ HTTP/1.1", "200", "4011", "-", "Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:57.0) Gecko/20100101 Firefox/57.0")
    tokenLine("::1 - - [11/Jan/2018:22:10:43 +0800] \"GET /~master/ HTTP/1.1\" 200 4011 \"-\" \"Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:57.0) Gecko/20100101 Firefox/57.0\"", linePattern).filter(_ != null) should be(expected)
  }

  it should "parse combinedv" in {
    // standard `combinedv` format.
    val linePattern = Pattern.compile(
      Array(
        Host,
        RemoteLogin,
        RemoteUser,
        RequestTime,
        Request,
        StatusCode,
        BodyLength,
        Referer,
        UserAgent,
        VirtualHost
      )
        .map(fieldPatterns(_))
        .foldLeft("")((l, x) => l + x)
    )
    val expected = Array("::1", "-", "-", "11/Jan/2018:22:10:43 +0800", "GET /~master/ HTTP/1.1", "200", "4011", "-", "Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:57.0) Gecko/20100101 Firefox/57.0", "concerto")
    tokenLine("::1 - - [11/Jan/2018:22:10:43 +0800] \"GET /~master/ HTTP/1.1\" 200 4011 \"-\" \"Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:57.0) Gecko/20100101 Firefox/57.0\" concerto", linePattern).filter(_ != null) should be(expected)
  }
}
