package moe.forpleuvoir.nebula.config

import moe.forpleuvoir.nebula.common.api.Initializable
import moe.forpleuvoir.nebula.serialization.Deserializable
import moe.forpleuvoir.nebula.serialization.Serializable

/**
 * 可序列化的配置
 */
interface ConfigSerializable : Initializable, Serializable, Deserializable {

	/**
	 * 配置的键
	 */
	val key: String

}