package com.forpleuvoir.nebula.serialization.yaml

import com.forpleuvoir.nebula.serialization.base.SerializeObject
import com.forpleuvoir.nebula.serialization.extensions.serializeObject
import com.forpleuvoir.nebula.serialization.extensions.toMap
import org.yaml.snakeyaml.Yaml

fun String.yamlStringToObject(): SerializeObject {
	val map: Map<String, Any> = Yaml().load(this)
	return serializeObject(map)
}

fun SerializeObject.toYamlString(): String {
	return Yaml().dumpAsMap(this.toMap())
}