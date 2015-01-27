import Dependencies._

val dictionaryBuilder = project.in(file(".")).enablePlugins(WebappPlugin)

name := "dictionary-builder"

description := "buildin' them electric dictionaries"

libraryDependencies ++= Seq(
    allenAiCommon exclude("com.typesafe", "config"),
    allenAiTestkit,
    allenAiDatastore,
    lucene("core"),
    lucene("analyzers-common"),
    lucene("highlighter"),
    lucene("queries"),
    lucene("queryparser")
)

fork in run := true

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

dependencyOverrides ++= Set(allenAiCommon)
