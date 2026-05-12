package moe.forpleuvoir.nebula.serialization.base.ops

import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.codec.Codec
import moe.forpleuvoir.nebula.serialization.codec.serialization


class SerializeObjectBuilder {

    val obj: SerializeObject = SerializeObject()

    context(codec: Codec<T>)
    operator fun <T> set(key: String, value: T) {
        obj.put(key, value.serialization)
    }

    fun <T> set(key: String, value: T, codec: Codec<T>) {
        obj.put(key, codec.serialization(value))
    }

    context(codec: Codec<T>)
    infix fun <T> String.to(value: T) {
        obj.put(this, value.serialization)
    }

    operator fun String.invoke(scope: SerializeObjectBuilder.() -> Unit) {
        obj[this] = SerializeObjectBuilder().apply(scope).obj
    }

}

inline fun SerializeObject.Companion.build(scope: SerializeObjectBuilder.() -> Unit) =
    SerializeObjectBuilder().apply(scope).obj
