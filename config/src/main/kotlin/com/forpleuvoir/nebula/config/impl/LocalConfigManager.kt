package com.forpleuvoir.nebula.config.impl

import com.forpleuvoir.nebula.config.ConfigUtil
import java.nio.file.Path

abstract class LocalConfigManager(
	key: String,
) : AbstractConfigManager(key) {

	abstract val configPath: Path
	override fun save() {
		ConfigUtil.run {
			val file = configFile(fileName(key), configPath)
			writeStringToFile(serializeObjectToString(serialization().asObject), file)
		}
	}

	override fun load() {
		ConfigUtil.run {
			val file = configFile(fileName(key), configPath)
			stringToSerializeObject(readFileToString(file)).apply {
				deserialization(this)
			}
		}
	}

}