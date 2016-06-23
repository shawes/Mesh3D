name := "mesh3d.quadrats"

version := "1.0"

description := """a command line tool for calculating areas of faces in a 3D mesh using quadrats"""

scalaVersion := "2.11.8"

assemblyJarName in assembly := "meshquadrats.jar"

resolvers += Resolver.sonatypeRepo("public")

libraryDependencies ++= Seq(
  "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.4",
  "org.scala-lang.modules" % "scala-parser-combinators_2.11" % "1.0.4",
  "org.scala-lang" % "scala-reflect" % "2.11.8",
  "com.github.tototoshi" %% "scala-csv" % "1.3.2",
  "com.github.scopt" %% "scopt" % "3.5.0",
  "org.clapper" %% "grizzled-scala" % "2.3.1"
)



