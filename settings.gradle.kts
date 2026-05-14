pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}

rootProject.name = "nebula"

include(":nebula-common")
include(":nebula-config")
include(":nebula-serialization")
include(":nebula-event")