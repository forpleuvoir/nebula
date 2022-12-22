package com.forpleuvoir.nebula.config

interface ConfigManager : ConfigCategory {

	fun save()

	fun saveAsync()

	fun load()

	fun loadAsync()

}