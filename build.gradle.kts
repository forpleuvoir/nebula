import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    signing
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
    id("maven-publish")
}

group = "moe.forpleuvoir"
version = libs.versions.nebulaVersion.get()

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    project.subprojects.forEach {
        api(it)
    }
}

val subprojectsOrder = listOf(
    project("nebula-common"),
    project("nebula-event"),
//    project("nebula-script"),
    project("nebula-serialization"),
    project("nebula-serialization-gson"),
    project("nebula-config")
)

sourceSets {
    getByName("test") {
        kotlin.srcDir("src/test/kotlin")
    }
}

java {
    withSourcesJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {

    register("publishNebulaToSnapshots") {
        subprojectsOrder.forEach {
            dependsOn(it.tasks.named("publish${it.name.uppercaseFirstChar()}PublicationToSnapshotsRepository"))
        }
        dependsOn(named("publishNebulaPublicationToSnapshotsRepository"))
    }

    register("publishNebulaToReleases") {
        subprojectsOrder.forEach {
            dependsOn(it.tasks.named("publish${it.name.uppercaseFirstChar()}PublicationToReleasesRepository"))
        }
        dependsOn(named("publishNebulaPublicationToReleasesRepository"))
    }

    register("publishNebulaToLocal") {
        subprojectsOrder.forEach {
            dependsOn(it.tasks.named("publish${it.name.uppercaseFirstChar()}PublicationToMavenLocalRepository"))
        }
        dependsOn(named("publishNebulaPublicationToMavenLocalRepository"))
    }

    withType<JavaCompile>().configureEach {
        this.options.release
        this.options.encoding = "UTF-8"
        targetCompatibility = JavaVersion.VERSION_21.toString()
        sourceCompatibility = JavaVersion.VERSION_21.toString()
    }

    withType<KotlinCompile>().configureEach {
        compilerOptions {
            suppressWarnings = true
            jvmTarget.set(JvmTarget.JVM_21)
            freeCompilerArgs.add("-Xjvm-default=all")
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

val nebulaSourcesJar = tasks.register<Jar>("nebulaSourcesJar") {
    subprojects.forEach {
        from(it.sourceSets["main"].allSource)
    }
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
            artifact(tasks.named("shadowJar"))
            artifact(tasks.named("shadowJar")) {
                classifier = ""
            }
            artifact(nebulaSourcesJar) {
                classifier = "sources"
            }
            artifact(nebulaSourcesJar) {
                classifier = "nebula-sources"
                extension = "jar"
            }
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

    apply(plugin = "java")
    apply(plugin = "kotlin")
    apply(plugin = "signing")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "maven-publish")

    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        implementation(rootProject.libs.bundles.kotlin)
        testImplementation(kotlin("test"))
    }

    tasks {

        test {
            useJUnitPlatform()
        }

        withType<JavaCompile>().configureEach {
            this.options.release
            this.options.encoding = "UTF-8"
            targetCompatibility = JavaVersion.VERSION_21.toString()
            sourceCompatibility = JavaVersion.VERSION_21.toString()
        }

        withType<KotlinCompile>().configureEach {
            compilerOptions {
                suppressWarnings = true
                jvmTarget.set(JvmTarget.JVM_21)
                freeCompilerArgs.add("-Xjvm-default=all")
            }
        }

        register<Jar>("nebulaSourcesJar") {
            archiveClassifier.set("nebula-sources")
            from(sourceSets["main"].allSource)
        }

    }

    sourceSets {
        getByName("test") {
            kotlin.srcDir("src/test/kotlin")
        }
    }


    java {
        withSourcesJar()
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
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
        publishing {
            publications {
                create<MavenPublication>(project.name) {
                    groupId = project.group.toString()
                    artifactId = project.name
                    version = project.version.toString()
                    from(components["java"])
                    artifact(tasks.getByName("nebulaSourcesJar")) {
                        classifier = "nebula-sources"
                        extension = "jar"
                    }
                    pom {
                        name.set(project.name)
                        description.set("forpleuvoir的基础代码库,${project.name}")
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

}