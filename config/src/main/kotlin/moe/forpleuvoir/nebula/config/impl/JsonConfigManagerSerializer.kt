package moe.forpleuvoir.nebula.config.impl

import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.json.jsonStringToObject
import moe.forpleuvoir.nebula.serialization.json.toJsonString

interface JsonConfigManagerSerializer : ConfigManagerSerializer {

	override fun fileName(key: String): String {
		return if (key.endsWith(".json")) key
		else "$key.json"
	}

	override fun serializeObjectToString(serializeObject: SerializeObject): String {
		return serializeObject.toJsonString()
	}

	override fun stringToSerializeObject(str: String): SerializeObject {
		return str.jsonStringToObject()
	}
}