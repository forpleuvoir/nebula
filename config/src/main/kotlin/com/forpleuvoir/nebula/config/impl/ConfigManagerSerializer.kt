package com.forpleuvoir.nebula.config.impl

import com.forpleuvoir.nebula.serialization.base.SerializeObject

interface ConfigManagerSerializer {

	fun fileName(key: String): String

	fun serializeObjectToString(serializeObject: SerializeObject): String

	fun stringToSerializeObject(str: String): SerializeObject

}