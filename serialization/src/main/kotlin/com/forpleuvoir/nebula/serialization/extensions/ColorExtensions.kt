package com.forpleuvoir.nebula.serialization.extensions

import com.forpleuvoir.nebula.common.color.Color
import com.forpleuvoir.nebula.serialization.base.SerializeElement
import com.forpleuvoir.nebula.serialization.base.SerializePrimitive

fun Color.serialization(): SerializeElement =
	SerializePrimitive(this.hexStr)

fun Color.deserialization(serializeElement: SerializeElement) {
	this.argb = Color.decode(serializeElement.asString)
}