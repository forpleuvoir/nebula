package moe.forpleuvoir.nebula.config.persistence

import moe.forpleuvoir.nebula.config.manager.ConfigManager
import moe.forpleuvoir.nebula.serialization.base.SerializeObject

interface ConfigManagerPersistence {

	fun fileName(manager: ConfigManager): String

	fun serializeObjectToString(serializeObject: SerializeObject): String

	fun stringToSerializeObject(str: String): SerializeObject

}