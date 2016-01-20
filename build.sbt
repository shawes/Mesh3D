name := "mesh3d.quadrats"

version := "1.0"

scalaVersion := "2.11.7"
libraryDependencies += "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.4"
libraryDependencies += "jline" % "jline" % "2.12.1"
//libraryDependencies += "org.clapper" %% "argot" % "1.0.3"
libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.2.2"
libraryDependencies += "com.github.scopt" %% "scopt" % "3.3.0"

resolvers += Resolver.sonatypeRepo("public")
assemblyJarName in assembly := "meshquadrats.jar"
