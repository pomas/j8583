import Dependencies._

crossPaths := false
autoScalaLibrary := false

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
resolvers += "FF Releases" at "https://repository.ffc.internal/redone-releases"
resolvers += "FF Public" at "https://repository.ffc.internal/public"

publishTo := Some("releases" at "https://repository.ffc.internal/" + "redone-releases")

libraryDependencies ++= Seq(
  slf4jApi,
  slf4j,
  junit % Test
)
