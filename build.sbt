name := "mesh3d.quadrats"

version := "2.1"

description := """a command line tool for calculating areas of faces in a 3D mesh using quadrats"""

scalaVersion := "2.12.0"

assemblyJarName in assembly := "mesh3d.jar"

resolvers += Resolver.sonatypeRepo("public")

libraryDependencies ++= Seq(
  "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6",
  "org.scala-lang.modules" % "scala-parser-combinators_2.12" % "1.0.4",
  "org.scala-lang" % "scala-reflect" % "2.12.0",
  "com.github.tototoshi" %% "scala-csv" % "1.3.4",
  "com.github.scopt" %% "scopt" % "3.5.0",
  "org.clapper" %% "grizzled-scala" % "4.0.0",
  "org.apache.commons" % "commons-io" % "1.3.2"
)



