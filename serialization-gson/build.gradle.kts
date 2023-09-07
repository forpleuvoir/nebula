val gsonVersion = "2.10"

dependencies {
	implementation(project(":common"))
	implementation(project(":serialization"))
	implementation("com.google.code.gson:gson:$gsonVersion")
}

