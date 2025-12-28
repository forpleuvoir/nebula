package moe.forpleuvoir.nebula.serialization

import moe.forpleuvoir.nebula.serialization.base.SerializeElement

@Suppress("unused")
fun interface Serializable {

    fun serialization(): SerializeElement

}