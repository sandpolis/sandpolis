//============================================================================//
//                                                                            //
//                Copyright © 2015 - 2020 Subterranean Security               //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation at:                                //
//                                                                            //
//    https://mozilla.org/MPL/2.0                                             //
//                                                                            //
//=========================================================S A N D P O L I S==//

buildscript {
	repositories {
		mavenCentral()
		jcenter()
	}

	dependencies {
		classpath(":com.sandpolis.gradle.plugin:")
		classpath(":com.sandpolis.gradle.codegen:")
	}
}

plugins {
	id("com.diffplug.spotless") version "5.8.2"
	id("org.ajoberstar.reckon") version "0.13.0"
}

spotless {
	cpp {
		target("**/*.cc", "**/*.hh")

		eclipseCdt()
		endWithNewline()
		indentWithTabs()
	}
	kotlin {
		target("**/*.kt")

		licenseHeaderFile(file("gradle/resources/header_gradle.txt"), "(plugins|import|package)")
	}
	kotlinGradle {
		target("**/*.kts")

		licenseHeaderFile(file("gradle/resources/header_gradle.txt"), "(plugins|import|package)")
	}
	java {
		target("**/*.java")

		// Exclude generated sources
		targetExclude("**/gen/main/java/**")

		eclipse().configFile("gradle/resources/EclipseConventions.xml")
		trimTrailingWhitespace()
		endWithNewline()

		licenseHeaderFile(file("gradle/resources/header_java.txt"), "package")
	}
	format("javaModules") {
		target("**/module-info.java")

		trimTrailingWhitespace()
		endWithNewline()

		licenseHeaderFile(file("gradle/resources/header_java.txt"), "(module|open module)")
	}
	format("proto") {
		target("**/*.proto")

		trimTrailingWhitespace()
		endWithNewline()
		indentWithSpaces()

		licenseHeaderFile(file("gradle/resources/header_java.txt"), "syntax")
	}
	format("swift") {
		target("**/*.swift")

		trimTrailingWhitespace()
		endWithNewline()
		indentWithTabs()

		licenseHeaderFile(file("gradle/resources/header_swift.txt"), "import")
	}
	format("css") {
		target("**/*.css")

		eclipseWtp(com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep.CSS)
	}
	format("json") {
		target("**/*.json")

		// Exclude iOS projects
		targetExclude("**/com.sandpolis.client.lockstone/**")

		eclipseWtp(com.diffplug.spotless.extra.wtp.EclipseWtpFormatterStep.JSON)
	}
}

// Apply repository configuration
allprojects {
	repositories {
		mavenLocal()
		mavenCentral()
	}

	buildscript {
		repositories {
			mavenCentral()
		}
	}
}

reckon {
	scopeFromProp()
	snapshotFromProp()
}
