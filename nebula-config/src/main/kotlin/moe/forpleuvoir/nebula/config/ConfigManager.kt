package moe.forpleuvoir.nebula.config

interface ConfigManager : ConfigCategory {

	fun save()

	fun saveAsync()

	fun forceSave()

	fun forceSaveAsync()

	fun load()

	fun loadAsync()

}