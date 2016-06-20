name := "mesh3d.quadrats"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "jline" % "jline" % "2.12.1",
  "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.4",
  "org.clapper" %% "argot" % "1.0.3",
  "com.github.tototoshi" %% "scala-csv" % "1.2.2"
  //"com.nativelibs4java" %% "scalacl" % "0.3-SNAPSHOT"
)

assemblyJarName in assembly := "mesh_quadrats.jar"

// Avoid sbt-related macro classpath issues.
//fork := true

// Scalaxy/Reified snapshots are published on the Sonatype repository.
//resolvers += Resolver.sonatypeRepo("snapshots")


