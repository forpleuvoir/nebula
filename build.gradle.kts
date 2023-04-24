import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	java
	signing
	kotlin("jvm") version "1.8.20"
	id("com.github.johnrengelman.shadow") version "7.1.2"
	id("maven-publish")
}

repositories {
	mavenCentral()
	mavenLocal()
	maven { url = uri("https://www.jitpack.io") }
}

subprojects {

	val isTest = this.name.contains("test")
	val isCommon = this.name == "common"

	apply(plugin = "java")
	apply(plugin = "kotlin")
	apply(plugin = "signing")
	if (!isTest) {
		if (!isCommon)
			apply(plugin = "com.github.johnrengelman.shadow")
		apply(plugin = "maven-publish")
	}

	group = "com.forpleuvoir.nebula"
	version = "0.2.2b"

	repositories {
		mavenCentral()
		mavenLocal()
		maven { url = uri("https://www.jitpack.io") }
	}

	dependencies {
		implementation(kotlin("reflect"))
		implementation(kotlin("stdlib"))
	}

	tasks.apply {
		withType<JavaCompile>().configureEach {
			this.options.release
			this.options.encoding = "UTF-8"
			targetCompatibility = JavaVersion.VERSION_17.toString()
			sourceCompatibility = JavaVersion.VERSION_17.toString()
		}
		withType<KotlinCompile>().configureEach {
			kotlinOptions.suppressWarnings = true
			kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()
		}
		if (!isCommon) {
			withType<ShadowJar>().configureEach {
				archivesName.set("${project.name}-shadow")
				archiveClassifier.set("shadow")
				dependencies {
					exclude(dependency("org.jetbrains.kotlin:"))
					exclude(dependency("org.jetbrains:"))
				}
			}
		}
	}

	sourceSets {
		getByName("test") {
			kotlin.srcDir("src/test/kotlin")
		}
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
				url = uri("https://maven.forpleuvoir.com:11443/releases")
				credentials(PasswordCredentials::class)
				authentication {
					create<BasicAuthentication>("basic")
				}
			}
			maven {
				name = "snapshots"
				url = uri("https://maven.forpleuvoir.com:11443/snapshots")
				credentials(PasswordCredentials::class)
				authentication {
					create<BasicAuthentication>("basic")
				}
			}
		}
		publications {
			create<MavenPublication>(project.name) {
				groupId = project.group.toString()
				artifactId = project.archivesName.get()
				version = project.version.toString()
				from(components["java"])
				pom {
					name.set(project.name)
					description.set("forpleuvoir的基础代码库")
					url.set("https://github.com/forpleuvoir/nebula")
					licenses {
						license {
							name.set("GNU General Public License, version 3 (GPLv3)")
							url.set("https://www.gnu.org/licenses/gpl-3.0.txt")
						}
					}
					developers {
						developer {
							id.set("forpleuvoir")
							name.set("forpleuvoir")
							email.set("forpleuvoir@gmail.com")
						}
					}
				}
			}
		}
	}

}