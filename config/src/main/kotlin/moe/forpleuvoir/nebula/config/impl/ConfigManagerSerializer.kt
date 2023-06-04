package moe.forpleuvoir.nebula.config.impl

import moe.forpleuvoir.nebula.serialization.base.SerializeObject

interface ConfigManagerSerializer {

	fun fileName(key: String): String

	fun serializeObjectToString(serializeObject: SerializeObject): String

	fun stringToSerializeObject(str: String): SerializeObject

}