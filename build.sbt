name := """play-java"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs
)

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.34"

libraryDependencies += "org.hibernate" % "hibernate-entitymanager" % "4.3.8.Final"

libraryDependencies += "org.webjars" % "bootstrap" % "3.3.1"