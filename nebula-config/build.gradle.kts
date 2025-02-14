import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

dependencies {
    implementation(project(":nebula-common"))
    implementation(project(":nebula-serialization"))
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
val compileKotlin: KotlinCompile by tasks

compileKotlin.compilerOptions {
    freeCompilerArgs.set(listOf("-Xnon-local-break-continue"))
}