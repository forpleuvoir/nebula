val yamlVersion = "1.33"
val dom4jVersion = "2.1.3"
val guavaVersion = "31.0.1-jre"
val gsonVersion = "2.8.9"
val tomljVersion = "1.1.0"

dependencies {
	implementation("org.yaml:snakeyaml:$yamlVersion")
	implementation("org.dom4j:dom4j:$dom4jVersion")
	implementation("com.google.guava:guava:$guavaVersion")
	implementation("com.google.code.gson:gson:$gsonVersion")
	implementation("org.tomlj:tomlj:$tomljVersion")
}