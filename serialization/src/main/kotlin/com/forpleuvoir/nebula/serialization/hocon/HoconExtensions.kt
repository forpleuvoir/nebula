package com.forpleuvoir.nebula.serialization.hocon

import com.forpleuvoir.nebula.serialization.base.SerializeObject
import com.forpleuvoir.nebula.serialization.extensions.serializeObject
import com.forpleuvoir.nebula.serialization.extensions.toMap
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigRenderOptions

fun String.hoconStringToObject(): SerializeObject {
	val config = ConfigFactory.parseString(this)
	val map: Map<String, Any> = config.root().mapValues { it.value.unwrapped() }
	return serializeObject(map)
}

fun SerializeObject.toHoconString(): String {
	return ConfigFactory.parseMap(this.toMap()).root().render(ConfigRenderOptions.concise().setJson(false).setFormatted(true))
}