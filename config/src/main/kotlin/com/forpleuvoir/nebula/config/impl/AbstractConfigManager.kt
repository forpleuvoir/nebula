package com.forpleuvoir.nebula.config.impl

import com.forpleuvoir.nebula.config.ConfigCategory
import com.forpleuvoir.nebula.config.ConfigManager
import kotlin.reflect.full.isSubclassOf

abstract class AbstractConfigManager(
	key: String,
) : ConfigManager, ConfigCategoryImpl(key), ConfigManagerSerializer {

	override var needSave: Boolean = false
		get() {
			var ret = false
			configSerializes().filter {
				it::class.isSubclassOf(ConfigCategory::class)
			}.forEach {
				it as ConfigCategory
				if (it.needSave) ret = true
			}
			if (ret) return true
			return field
		}
		set(value) {
			field = value
			if (!value) {
				configSerializes().filter {
					it::class.isSubclassOf(ConfigCategory::class)
				}.forEach {
					it as ConfigCategory
					it.needSave = false
				}
			}
		}

	override fun saveAsync() {
		ConfigThreadPool.execute(::save)
	}

	override fun loadAsync() {
		ConfigThreadPool.execute(::load)
	}

}