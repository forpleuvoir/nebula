val snakeyaml = "2.0"
val gsonVersion = "2.10"
val forKomaVersion = "1.2.0"
val hoconVersion = "1.4.2"

dependencies {
	implementation(project(":common"))
	implementation("org.yaml:snakeyaml:$snakeyaml")
	implementation("com.google.code.gson:gson:$gsonVersion")
	implementation("cc.ekblad:4koma:$forKomaVersion")
	implementation("com.typesafe:config:$hoconVersion")
}

