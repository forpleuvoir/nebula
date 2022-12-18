val yamlVersion = "1.33"
val guavaVersion = "31.0.1-jre"
val gsonVersion = "2.8.9"
val forKomaVersion = "1.1.0"

dependencies {
	implementation(project(":common"))
	implementation("org.yaml:snakeyaml:$yamlVersion")
	implementation("com.google.code.gson:gson:$gsonVersion")
	implementation("cc.ekblad:4koma:$forKomaVersion")
}