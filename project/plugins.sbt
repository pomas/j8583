credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
resolvers += "FF Releases" at "https://repository.ffc.internal/sbt-plugins"

addSbtPlugin("com.leveris.sbt.release" % "sc_sbt-plugin-release" % "1.3")
