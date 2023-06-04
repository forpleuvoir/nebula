package moe.forpleuvoir.nebula.serialization.yaml

import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.extensions.serializeObject
import moe.forpleuvoir.nebula.serialization.extensions.toMap
import org.yaml.snakeyaml.Yaml

fun String.yamlStringToObject(): SerializeObject {
	val map: Map<String, Any> = Yaml().load(this)
	return serializeObject(map)
}

fun SerializeObject.toYamlString(): String {
	return Yaml().dumpAsMap(this.toMap())
}