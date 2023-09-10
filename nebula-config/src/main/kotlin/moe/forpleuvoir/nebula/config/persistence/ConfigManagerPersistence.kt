package moe.forpleuvoir.nebula.config.persistence

import moe.forpleuvoir.nebula.serialization.base.SerializeObject

interface ConfigManagerPersistence {

	fun fileName(key: String): String

	fun serializeObjectToString(serializeObject: SerializeObject): String

	fun stringToSerializeObject(str: String): SerializeObject

}