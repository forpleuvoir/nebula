package moe.forpleuvoir.nebula.config.impl

import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.yaml.toYamlString
import moe.forpleuvoir.nebula.serialization.yaml.yamlStringToObject

interface YamlConfigManagerSerializer : ConfigManagerSerializer {

	override fun fileName(key: String): String {
		return if (key.endsWith(".yaml")) key
		else "$key.yaml"
	}

	override fun serializeObjectToString(serializeObject: SerializeObject): String {
		return serializeObject.toYamlString()
	}

	override fun stringToSerializeObject(str: String): SerializeObject {
		return str.yamlStringToObject()
	}
}