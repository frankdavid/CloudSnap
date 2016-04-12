name := "cloud_test"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies += "com.typesafe.play" %% "play" % "2.3.0"

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

lazy val frontend = (project in file("frontend")).enablePlugins(PlayScala) dependsOn (common) settings {
  scalaVersion := "2.10.4"
  resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"
  resolvers += "Spy Repository" at "http://files.couchbase.com/maven2"
  libraryDependencies ++= Seq(
    "com.github.seratch" %% "awscala" % "0.4.+",
    anorm,
    "mysql" % "mysql-connector-java" % "5.1.34",
    jdbc,
    cache,
    "com.github.mumoshu" %% "play2-memcached" % "0.6.0"
  )
}
lazy val screenshot_service = (project in file("screenshot_service")) dependsOn (common) settings {
  scalaVersion := "2.10.4"
  libraryDependencies ++= Seq(
    "com.github.seratch" %% "awscala" % "0.4.+",
    "org.seleniumhq.selenium" % "selenium-java" % "2.44.0")
}

lazy val common = (project in file("common")) settings {
  scalaVersion := "2.10.4"
  libraryDependencies += "com.github.seratch" %% "awscala" % "0.4.+"
}