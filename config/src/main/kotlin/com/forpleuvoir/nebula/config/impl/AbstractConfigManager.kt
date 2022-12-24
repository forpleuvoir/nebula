package com.forpleuvoir.nebula.config.impl

import com.forpleuvoir.nebula.common.runAsync
import com.forpleuvoir.nebula.config.ConfigManager

abstract class AbstractConfigManager(
	key: String,
) : ConfigManager, ConfigCategoryImpl(key), ConfigManagerSerializer {

	override fun saveAsync() {
		runAsync { save() }
	}

	override fun loadAsync() {
		runAsync { load() }
	}

}