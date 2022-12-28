package com.forpleuvoir.nebula.config

import com.forpleuvoir.nebula.common.api.Initializable

interface ConfigCategory : Initializable, ConfigSerializable {

	override val key: String

	var needSave: Boolean

	fun configSerializes(): Iterable<ConfigSerializable>

	fun addConfigSerializable(configSerializable: ConfigSerializable): ConfigSerializable

}