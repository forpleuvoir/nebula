package moe.forpleuvoir.nebula.config.persistence

import moe.forpleuvoir.nebula.serialization.base.SerializeElement

interface ConfigManagerPersistence {

	fun wrapFileName(fileName: String): String

	fun serializeToString(serializeObject: SerializeElement): String

	fun stringToSerialization(str: String): SerializeElement

}