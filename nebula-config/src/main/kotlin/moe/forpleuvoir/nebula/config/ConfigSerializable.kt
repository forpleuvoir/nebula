package moe.forpleuvoir.nebula.config

import moe.forpleuvoir.nebula.common.api.Initializable
import moe.forpleuvoir.nebula.serialization.Deserializable
import moe.forpleuvoir.nebula.serialization.Serializable

interface ConfigSerializable : Initializable, Serializable, Deserializable {

	/**
	 * 配置的键
	 */
	val key: String

}