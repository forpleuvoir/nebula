package com.forpleuvoir.nebula.config.impl

import com.forpleuvoir.nebula.config.ConfigUtil
import com.forpleuvoir.nebula.serialization.base.SerializeObject
import java.nio.file.Path

abstract class LocalConfigManager(
	key: String,
	serializerFunction: (SerializeObject) -> String,
	deserializerFunction: (String) -> SerializeObject
) : AbstractConfigManager(key, serializerFunction, deserializerFunction) {

	abstract val configPath: Path
	override fun save() {
		ConfigUtil.run {
			val file = configFile(key, configPath)
			writeStringToFile(serializerFunction(serialization().asObject), file)
		}
	}

	override fun load() {
		ConfigUtil.run {
			val file = configFile(key, configPath)
			deserializerFunction(readFileToString(file)).apply {
				deserialization(this)
			}
		}
	}

}