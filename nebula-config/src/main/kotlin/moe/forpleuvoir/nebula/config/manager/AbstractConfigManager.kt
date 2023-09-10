package moe.forpleuvoir.nebula.config.manager

import moe.forpleuvoir.nebula.config.category.ConfigCategory
import moe.forpleuvoir.nebula.config.category.ConfigCategoryImpl
import moe.forpleuvoir.nebula.config.persistence.ConfigManagerPersistence
import moe.forpleuvoir.nebula.config.util.ConfigThreadPool
import kotlin.reflect.full.isSubclassOf

abstract class AbstractConfigManager(
	key: String,
) : ConfigManager, ConfigCategoryImpl(key), ConfigManagerPersistence {

	override var needSave: Boolean = false
		get() {
			var ret = false
			allConfigSerializable().filter {
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
				allConfigSerializable().filter {
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

	override fun forceSaveAsync() {
		ConfigThreadPool.execute(::forceSave)
	}

	override fun loadAsync() {
		ConfigThreadPool.execute(::load)
	}

}