name := "services-check"

version := "1.0"

libraryDependencies ++= {
  val specs2V = "2.3.12"
  Seq(
    "org.specs2"                %%  "specs2-core"     % specs2V   % "test",
    "org.specs2"                %%  "specs2-html"     % specs2V   % "test",
    "org.scalaj"                %%  "scalaj-http"     % "1.1.4"   % "test",
    "com.typesafe"              %   "config"          % "1.2.1"   % "test"
  )
}
