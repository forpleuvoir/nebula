package moe.forpleuvoir.nebula.config.persistence

import moe.forpleuvoir.nebula.serialization.base.SerializeObject

interface ConfigManagerPersistence {

	fun wrapFileName(fileName: String): String

	fun serializeObjectToString(serializeObject: SerializeObject): String

	fun stringToSerializeObject(str: String): SerializeObject

}