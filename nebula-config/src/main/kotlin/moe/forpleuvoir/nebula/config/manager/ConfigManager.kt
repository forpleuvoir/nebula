package moe.forpleuvoir.nebula.config.manager

import moe.forpleuvoir.nebula.config.category.ConfigCategory

interface ConfigManager : ConfigCategory {

	fun save()

	fun saveAsync()

	fun forceSave()

	fun forceSaveAsync()

	fun load()

	fun loadAsync()

}