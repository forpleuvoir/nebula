import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    implementation(project(":nebula-common"))
    implementation(project(":nebula-serialization"))
    implementation(libs.kotlinpoet)
    testImplementation(project(":nebula-serialization-gson"))
    testImplementation(libs.gson)
}

tasks {

    withType<ShadowJar> {
        archiveBaseName.set("${project.name}-nebula")
        archiveClassifier.set("nebula")
        dependencies {
            include(dependency("moe.forpleuvoir:nebula-common"))
            include(dependency("moe.forpleuvoir:nebula-serialization"))
        }
    }

    named<Jar>("nebulaSourcesJar").configure {
        archiveClassifier.set("nebula-sources")
        from(sourceSets["main"].allSource)
        from(project(":nebula-common").sourceSets["main"].allSource)
        from(project(":nebula-serialization").sourceSets["main"].allSource)
    }

}