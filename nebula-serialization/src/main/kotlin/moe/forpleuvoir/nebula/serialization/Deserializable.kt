package moe.forpleuvoir.nebula.serialization

import moe.forpleuvoir.nebula.serialization.base.SerializeElement

@Suppress("unused")
interface Deserializable {

	fun deserialization(serializeElement: SerializeElement)

}