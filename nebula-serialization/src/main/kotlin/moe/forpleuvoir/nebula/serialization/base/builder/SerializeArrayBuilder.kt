package moe.forpleuvoir.nebula.serialization.base.builder

import kotlinx.serialization.KSerializer
import moe.forpleuvoir.nebula.serialization.base.SerializeArray
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.codec.Codec
import moe.forpleuvoir.nebula.serialization.codec.serialization
import moe.forpleuvoir.nebula.serialization.nebula.NebulaFormat

inline fun SerializeArray.Companion.build(scope: SerializeArray.() -> Unit) =
    SerializeArray().apply(scope)

// region — context-based add

context(codec: Codec<T>)
fun <T> SerializeArray.add(value: T) {
    add(codec.serialization(value))
}

context(serializer: KSerializer<T>)
fun <T> SerializeArray.add(value: T) {
    add(NebulaFormat.encodeToElement(value, serializer))
}

// endregion

// region — nested builders

fun SerializeArray.obj(scope: SerializeObjectBuilder.() -> Unit) {
    add(SerializeObjectBuilder().apply(scope).obj)
}

fun SerializeArray.arr(scope: SerializeArray.() -> Unit) {
    add(SerializeArray().apply(scope))
}

// endregion
