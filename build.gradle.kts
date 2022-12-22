plugins {
	java
	signing
	kotlin("jvm") version "1.7.22"
	id("maven-publish")
}

repositories {
	mavenCentral()
	mavenLocal()
	maven { url = uri("https://www.jitpack.io") }
}

subprojects {

	val isTest = this.name.contains("test")

	apply(plugin = "java")
	apply(plugin = "kotlin")
	apply(plugin = "signing")
	if (!isTest) {
		apply(plugin = "maven-publish")
	}

	group = "com.forpleuvoir.nebula"
	version = "0.1.0"

	repositories {
		mavenCentral()
		mavenLocal()
		maven { url = uri("https://www.jitpack.io") }
	}

	dependencies {
		implementation(kotlin("reflect"))
		implementation(kotlin("stdlib"))
	}

	tasks.withType<JavaCompile>().configureEach {
		this.options.release
		this.options.encoding = "UTF-8"
	}

	kotlin {
	}

	java {
		withSourcesJar()
		toolchain.languageVersion.set(JavaLanguageVersion.of(17))
	}

	publishing {
		//https://reposilite.com/guide/gradle
		repositories {
			maven {
				name = "releases"
				url = uri("https://maven.forpleuvoir.com/releases")
				credentials(PasswordCredentials::class)
				authentication {
					create<BasicAuthentication>("basic")
				}
			}
			maven {
				name = "snapshots"
				url = uri("https://maven.forpleuvoir.com/snapshots")
				credentials(PasswordCredentials::class)
				authentication {
					create<BasicAuthentication>("basic")
				}
			}
		}
		publications {
			create<MavenPublication>("maven") {
				groupId = project.group.toString()
				artifactId = project.artifacts.toString()
				version = project.version.toString()
				from(components["java"])
			}
		}
	}

}