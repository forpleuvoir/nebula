package com.forpleuvoir.nebula.config

import com.forpleuvoir.nebula.common.api.Initializable
import com.forpleuvoir.nebula.serialization.Deserializable
import com.forpleuvoir.nebula.serialization.Serializable

interface ConfigSerializable : Initializable, Serializable, Deserializable {

	/**
	 * 配置的键
	 */
	val key: String

}