val gsonVersion = "2.10"

dependencies {
    implementation(project(":nebula-common"))
    api(project(":nebula-serialization"))
    api("com.google.code.gson:gson:$gsonVersion")
}

