import com.typesafe.sbt.SbtScalariform._

import scalariform.formatter.preferences._

name := "able_devices_auth"
organizationName := "able_io"
version := "1.0.0"

scalaVersion := "2.13.8"

resolvers += Resolver.jcenterRepo

resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

val playSilhouetteVersion = "7.0.7"
val slickVersion = "3.3.3"
val playSlickVersion = "5.0.0"

libraryDependencies ++= Seq(
  "io.github.honeycomb-cheesecake" %% "play-silhouette" % playSilhouetteVersion,
  "io.github.honeycomb-cheesecake" %% "play-silhouette-password-bcrypt" % playSilhouetteVersion,
  "io.github.honeycomb-cheesecake" %% "play-silhouette-persistence" % playSilhouetteVersion,
  "io.github.honeycomb-cheesecake" %% "play-silhouette-crypto-jca" % playSilhouetteVersion,
  "net.codingwell" %% "scala-guice" % "4.2.6",
  "com.typesafe.slick" %% "slick" % slickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
  "com.typesafe.play" %% "play-slick" % playSlickVersion,
  "com.typesafe.play" %% "play-slick-evolutions" % playSlickVersion,
  //it's org.postgresql.ds.PGSimpleDataSource dependency
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
  guice,
  filters
)

// Enable Play plugin
enablePlugins(PlayScala)

// Enable Docker plugin
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
enablePlugins(AshScriptPlugin)

val dockerAppPath = "/app"
val logsPath      = dockerAppPath + "/logs"
val jreVersion    = "8u181"
defaultLinuxInstallLocation in Docker := dockerAppPath

dockerBaseImage := "openjdk:" + jreVersion + "-jre-alpine"
packageName in Docker := "able_io/" + packageName.value
dockerUpdateLatest := true
dockerLabels := Map("maintainer" -> organizationName.value)
dockerEnvVars := Map("APP_DIR"   -> dockerAppPath)
dockerExposedPorts := Seq(8080)
dockerExposedVolumes := Seq(logsPath)
javaOptions in Universal ++= Seq(
  "-Dconfig.resource=application-docker.conf",
  "-Dlogback.configurationFile=logback-docker.xml",
  "-Djava.security.egd=file:/dev/./urandom",
  "-Dpidfile.path=/dev/null"
)

scalacOptions ++= Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-Xfatal-warnings", // Fail the compilation if there are any warnings.
  "-Xlint", // Enable recommended additional warnings.
  "-Ywarn-dead-code", // Warn when dead code is identified.
  "-Ywarn-numeric-widen", // Warn when numerics are widened.
  "-Xlint:-unused,_"
)

//********************************************************
// Scalariform settings
//********************************************************

scalariformAutoformat := true

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(FormatXml, false)
  .setPreference(DoubleIndentConstructorArguments, false)
  .setPreference(DanglingCloseParenthesis, Preserve)
