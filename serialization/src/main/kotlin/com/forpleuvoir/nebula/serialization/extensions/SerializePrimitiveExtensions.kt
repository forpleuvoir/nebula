package com.forpleuvoir.nebula.serialization.extensions

import com.forpleuvoir.nebula.serialization.base.SerializePrimitive

fun SerializePrimitive.toObj(): Any {
	return if (!isNumber) value else {
		if (value.toString().contains(Regex("[-+]?[0-9]*\\.[0-9]+"))) {
			asDouble
		} else if (value.toString().contains(Regex("^-?[0-9]*\$"))) {
			asInt
		} else {
			asString
		}
	}
}