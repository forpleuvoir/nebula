package com.forpleuvoir.nebula.config.impl

import com.forpleuvoir.nebula.common.runAsync
import com.forpleuvoir.nebula.config.ConfigManager
import com.forpleuvoir.nebula.serialization.base.SerializeObject

abstract class AbstractConfigManager(
	key: String,
	protected val serializerFunction: (SerializeObject) -> String,
	protected val deserializerFunction: (String) -> SerializeObject
) : ConfigManager, ConfigCategoryImpl(key) {

	override fun saveAsync() {
		runAsync { save() }
	}

	override fun loadAsync() {
		runAsync { load() }
	}

}