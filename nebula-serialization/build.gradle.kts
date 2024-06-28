import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    implementation(project(":nebula-common"))
    testImplementation(project(":nebula-serialization-gson"))
    testImplementation("com.google.code.gson:gson:2.10")
}

tasks {

    withType<ShadowJar> {
        archiveBaseName.set("${project.name}-nebula")
        archiveClassifier.set("nebula")
        dependencies {
            include(dependency("moe.forpleuvoir:nebula-common"))
        }
    }

    named<Jar>("nebulaSourcesJar").configure {
        archiveClassifier.set("nebula-sources")
        from(sourceSets["main"].allSource)
        from(project(":nebula-common").sourceSets["main"].allSource)
    }
}