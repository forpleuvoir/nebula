package moe.forpleuvoir.nebula.serialization.hocon

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigRenderOptions
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.extensions.serializeObject
import moe.forpleuvoir.nebula.serialization.extensions.toMap

fun String.hoconStringToObject(): SerializeObject {
	val config = ConfigFactory.parseString(this)
	val map: Map<String, Any> = config.root().mapValues { it.value.unwrapped() }
	return serializeObject(map)
}

fun SerializeObject.toHoconString(): String {
	return ConfigFactory.parseMap(this.toMap()).root().render(ConfigRenderOptions.concise().setJson(false).setFormatted(true))
}