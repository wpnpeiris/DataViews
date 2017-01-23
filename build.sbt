organization  := "se.kth"

version       := "0.1"

scalaVersion  := "2.11.6"

libraryDependencies ++= {
  Seq(
    "com.typesafe.akka" %% "akka-http" % "10.0.1",
    "org.apache.spark" %% "spark-core" % "2.1.0" % "provided"
  )
}


