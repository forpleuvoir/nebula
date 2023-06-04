package moe.forpleuvoir.nebula.config.impl

import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.toml.toTomlString
import moe.forpleuvoir.nebula.serialization.toml.tomlStringToObject

interface TomlConfigManagerSerializer : ConfigManagerSerializer {

	override fun fileName(key: String): String {
		return if (key.endsWith(".toml")) key
		else "$key.toml"
	}

	override fun serializeObjectToString(serializeObject: SerializeObject): String {
		return serializeObject.toTomlString()
	}

	override fun stringToSerializeObject(str: String): SerializeObject {
		return str.tomlStringToObject()
	}
}