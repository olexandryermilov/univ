name := "algorithmscomplexity"

version := "0.1"

scalaVersion := "2.13.1"

resolvers += "Sonatype OSS Snapshots" at
  "https://oss.sonatype.org/content/repositories/snapshots"

// https://mvnrepository.com/artifact/com.storm-enroute/scalameter
libraryDependencies += "com.storm-enroute" %% "scalameter" % "0.19" % Test

