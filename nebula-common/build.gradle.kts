import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

tasks {

    withType<ShadowJar> {
        archiveBaseName.set("${project.name}-nebula")
        archiveClassifier.set("nebula")
        dependencies {
            include(dependency("moe.forpleuvoir:nebula-common"))
        }
    }


}

