package com.forpleuvoir.nebula.serialization

import com.forpleuvoir.nebula.serialization.base.SerializeElement

@Suppress("unused")
interface Deserializable {

	fun deserialization(serializeElement: SerializeElement)

}