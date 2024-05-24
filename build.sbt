/*
 * build.sbt
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

name := "DAOHelper"

normalizedName := "daohelper"

version := "1.1.12"

organization := "be.ugent.caagt"

versionScheme := Some("early-semver")

crossPaths := false // important for tests

libraryDependencies += "org.postgresql" % "postgresql" % "42.3.6" % Test
libraryDependencies += "junit" % "junit" % "4.13.2" % Test
libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test
libraryDependencies += "org.assertj" % "assertj-core" % "3.23.1" % Test

autoScalaLibrary := false

// Too many javadoc errors

Compile / doc / javacOptions := Seq("-encoding", "UTF-8", "-Xdoclint:-missing")

// Safer not to run database tests in parallel?
Test / parallelExecution := false
Test / fork := true
