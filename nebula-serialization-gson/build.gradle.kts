import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

val gsonVersion = "2.10"

dependencies {
    implementation(project(":nebula-common"))
    implementation(project(":nebula-serialization"))
    implementation("com.google.code.gson:gson:$gsonVersion")
}

tasks.withType<ShadowJar>().configureEach {
    archivesName.set("${project.name}-nebula")
    archiveClassifier.set("nebula")
    dependencies {
        include(dependency("moe.forpleuvoir:nebula-common"))
        include(dependency("moe.forpleuvoir:nebula-serialization"))
    }
}