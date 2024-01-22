import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

dependencies {
    implementation(project(":nebula-common"))
    testImplementation(project(":nebula-serialization-gson"))
}

tasks.withType<ShadowJar>().configureEach {
    archivesName.set("${project.name}-nebula")
    archiveClassifier.set("nebula")
    dependencies {
        include(dependency("moe.forpleuvoir:nebula-common"))
    }
}