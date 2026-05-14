import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    alias(libs.plugins.kotlin)
    alias(libs.plugins.shadow)
    alias(libs.plugins.kotlinSerialization) apply false
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
    project("nebula-serialization"),
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
        dependsOn(named("publishNebulaPublicationToSnapshotsRepository"))
        subprojects.forEach {
            dependsOn(it.tasks.named("publish${it.name.uppercaseFirstChar()}PublicationToSnapshotsRepository"))
        }
    }

    register("publishNebulaToReleases") {
        dependsOn(named("publishNebulaPublicationToReleasesRepository"))
        subprojects.forEach {
            dependsOn(it.tasks.named("publish${it.name.uppercaseFirstChar()}PublicationToReleasesRepository"))
        }
    }

    register("publishNebulaToLocal") {
        dependsOn(named("publishNebulaPublicationToMavenLocalRepository"))
        subprojects.forEach {
            dependsOn(it.tasks.named("publish${it.name.uppercaseFirstChar()}PublicationToMavenLocalRepository"))
        }
    }

    withType<JavaCompile>().configureEach {
        this.options.release
        this.options.encoding = "UTF-8"
        targetCompatibility = JavaVersion.VERSION_21.toString()
        sourceCompatibility = JavaVersion.VERSION_21.toString()
    }

    withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            freeCompilerArgs.addAll("-jvm-default=enable", "-Xcontext-parameters")
        }
    }

    withType<ShadowJar>().configureEach {
        archiveBaseName.set(project.name)
        subprojects.forEach { subproject ->
            from(subproject.sourceSets["main"].output)
        }
        dependencies {
            println("打包的子模块:${subprojects.joinToString(separator = ", ", "[", "]") { it.name }}")
            project.subprojects.forEach {
                include(dependency(":${it.name}"))
            }
        }
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    }

}

val nebulaSourcesJar = tasks.register<Jar>("nebulaSourcesJar") {
    description = "包含所有子模块的源码"
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
            artifact(tasks.named<ShadowJar>("shadowJar")) {
                classifier = ""
            }
            artifact(nebulaSourcesJar) {
                classifier = "sources"
            }
            pom {
                name.set(project.name)
                description.set("forpleuvoir的基础代码库")
                url.set("https://github.com/forpleuvoir/nebula")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
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
    apply(plugin = rootProject.libs.plugins.shadow.get().pluginId)
    apply(plugin = "maven-publish")
    apply(plugin = rootProject.libs.plugins.kotlinSerialization.get().pluginId)

    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        implementation(rootProject.libs.bundles.kotlin)
        testImplementation(kotlin("test-junit5"))
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
                jvmTarget.set(JvmTarget.JVM_21)
                freeCompilerArgs.addAll(
                    "-jvm-default=enable",
                    "-Xcontext-parameters"
                )
            }
        }

    }

    val sourcesJar by tasks.registering(Jar::class) {
        archiveClassifier.set("sources")
        from(project.sourceSets["main"].allSource)
    }

    val jar by tasks.named<Jar>("jar")

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
        publications {
            create<MavenPublication>(project.name) {
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()
                artifact(jar)
                artifact(sourcesJar.get())
                pom {
                    name.set(project.name)
                    description.set("forpleuvoir的基础代码库,${project.name}")
                    url.set("https://github.com/forpleuvoir/nebula")
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/licenses/MIT")
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