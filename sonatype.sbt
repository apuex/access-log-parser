sonatypeProfileName := "com.github.apuex"

publishMavenStyle := true

licenses := Seq("GPL3" -> url("https://www.gnu.org/licenses/gpl-3.0.txt"))

import xerial.sbt.Sonatype._
sonatypeProjectHosting := Some(GitHubHosting("apuex", "access-log-parser", "xtwxy@hotmail.com"))

homepage := Some(url("https://github.com/apuex/access-log-parser"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/apuex/access-log-parser.git"),
    "scm:git@github.com:apuex/access-log-parser.git"
  )
)

developers := List(
  Developer(id="apuex", name="Wangxy", email="xtwxy@hotmail.com", url=url("https://github.com/apuex"))
)

/*
Command Line Usage

Publish a GPG-signed artifact to Sonatype:

$ sbt publishSigned

Do close and promote at once:

$ sbt sonatypeRelease

 */
