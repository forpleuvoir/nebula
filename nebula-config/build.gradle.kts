import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    implementation(project(":nebula-common"))
    implementation(project(":nebula-serialization"))
    testImplementation(project(":nebula-serialization-gson"))
    testImplementation("com.google.code.gson:gson:2.10")
}

tasks.withType<ShadowJar>().configureEach {
    archiveBaseName.set("${project.name}-nebula")
    archiveClassifier.set("nebula")
    dependencies {
        include(dependency("moe.forpleuvoir:nebula-common"))
        include(dependency("moe.forpleuvoir:nebula-serialization"))
    }
}