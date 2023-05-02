package com.forpleuvoir.nebula.serialization

import com.forpleuvoir.nebula.serialization.base.SerializeElement

@Suppress("unused")
interface Serializable {

	fun serialization(): SerializeElement

}