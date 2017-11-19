name := "Chaine3D"

version := "0.1"

scalaVersion := "2.11.12"
libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.2.0"
libraryDependencies += "org.scalanlp" % "breeze_2.11" % "0.12"
libraryDependencies += "org.apache.ignite" % "ignite-core" % "2.3.0"
libraryDependencies += "org.apache.ignite" % "ignite-spark" % "2.3.0" excludeAll(ExclusionRule("org.apache.spark"))
libraryDependencies += "net.liftweb" % "lift-json_2.11" % "3.1.1"

resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"