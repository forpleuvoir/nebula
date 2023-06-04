package moe.forpleuvoir.nebula.config.impl

import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.hocon.hoconStringToObject
import moe.forpleuvoir.nebula.serialization.hocon.toHoconString

interface HoconConfigManagerSerializer : ConfigManagerSerializer {

	override fun fileName(key: String): String {
		return if (key.endsWith(".conf")) key
		else "$key.conf"
	}

	override fun serializeObjectToString(serializeObject: SerializeObject): String {
		return serializeObject.toHoconString()
	}

	override fun stringToSerializeObject(str: String): SerializeObject {
		return str.hoconStringToObject()
	}
}