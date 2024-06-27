import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    signing
    kotlin("jvm") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("maven-publish")
}

group = "moe.forpleuvoir"
version = "0.2.9d"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    project.subprojects.forEach {
        api(it)
    }
}

tasks {
    withType<JavaCompile>().configureEach {
        this.options.release
        this.options.encoding = "UTF-8"
        targetCompatibility = JavaVersion.VERSION_17.toString()
        sourceCompatibility = JavaVersion.VERSION_17.toString()
    }
    withType<KotlinCompile>().configureEach {
        compilerOptions {
            suppressWarnings = true
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    withType<ShadowJar>().configureEach {
        archiveBaseName.set("${project.name}-nebula")
        archiveClassifier.set("nebula")
        dependencies {
            project.subprojects.forEach {
                include(dependency("${it.group}:${it.name}"))
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
        mavenLocal()
        maven {
            name = "releases"
            url = uri("https://maven.forpleuvoir.moe/releases")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
        maven {
            name = "snapshots"
            url = uri("https://maven.forpleuvoir.moe/snapshots")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>(project.name) {
            groupId = project.group.toString()
            artifactId = project.name
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

subprojects {

    val isTest = this.name.contains("test")
    val isCommon = this.name == "nebula-common"

    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "signing")
    if (!isTest) {
        if (!isCommon)
            apply(plugin = "com.github.johnrengelman.shadow")
        apply(plugin = "maven-publish")
    }

    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        implementation(kotlin("reflect"))
        implementation(kotlin("stdlib"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
        testImplementation(kotlin("test"))
    }

    tasks {
        test {
            useJUnitPlatform()
        }
        withType<JavaCompile>().configureEach {
            this.options.release
            this.options.encoding = "UTF-8"
            targetCompatibility = JavaVersion.VERSION_17.toString()
            sourceCompatibility = JavaVersion.VERSION_17.toString()
        }
        withType<KotlinCompile>().configureEach {
            compilerOptions {
                suppressWarnings = true
                jvmTarget.set(JvmTarget.JVM_17)
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
            mavenLocal()
            maven {
                name = "releases"
                url = uri("https://maven.forpleuvoir.moe/releases")
                credentials(PasswordCredentials::class)
                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
            maven {
                name = "snapshots"
                url = uri("https://maven.forpleuvoir.moe/snapshots")
                credentials(PasswordCredentials::class)
                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
        }
        publications {
            create<MavenPublication>(project.name) {
                groupId = project.group.toString()
                artifactId = project.name
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