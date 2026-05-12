package moe.forpleuvoir.nebula.serialization.base.builder

import kotlinx.serialization.KSerializer
import moe.forpleuvoir.nebula.serialization.base.SerializeArray
import moe.forpleuvoir.nebula.serialization.base.SerializeNull
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import moe.forpleuvoir.nebula.serialization.codec.Codec
import moe.forpleuvoir.nebula.serialization.codec.serialization
import moe.forpleuvoir.nebula.serialization.nebula.NebulaFormat
import java.math.BigDecimal
import java.math.BigInteger

class SerializeObjectBuilder {

    val obj: SerializeObject = SerializeObject()

    context(codec: Codec<T>)
    operator fun <T> set(key: String, value: T) {
        obj.put(key, value.serialization)
    }

    fun <T> set(key: String, value: T, codec: Codec<T>) {
        obj.put(key, codec.serialization(value))
    }

    context(serializer: KSerializer<T>)
    operator fun <T> set(key: String, value: T) {
        obj.put(key, NebulaFormat.encodeToElement(value, serializer))
    }

    context(codec: Codec<T>)
    infix fun <T> String.to(value: T) {
        obj.put(this, value.serialization)
    }

    operator fun String.invoke(scope: SerializeObjectBuilder.() -> Unit) {
        obj[this] = SerializeObjectBuilder().apply(scope).obj
    }

    //region Primitive
    operator fun set(key: String, value: String) {
        obj.put(key, SerializePrimitive(value))
    }

    operator fun set(key: String, value: Char) {
        obj.put(key, SerializePrimitive(value))
    }

    operator fun set(key: String, value: Boolean) {
        obj.put(key, SerializePrimitive(value))
    }

    operator fun set(key: String, value: Number) {
        obj.put(key, SerializePrimitive(value))
    }

    operator fun set(key: String, value: BigInteger) {
        obj.put(key, SerializePrimitive(value))
    }

    operator fun set(key: String, value: BigDecimal) {
        obj.put(key, SerializePrimitive(value))
    }

    infix fun String.to(value: String) {
        obj.put(this, SerializePrimitive(value))
    }

    infix fun String.to(value: Char) {
        obj.put(this, SerializePrimitive(value))
    }

    infix fun String.to(value: Boolean) {
        obj.put(this, SerializePrimitive(value))
    }

    infix fun String.to(value: Number) {
        obj.put(this, SerializePrimitive(value))
    }

    infix fun String.to(value: BigInteger) {
        obj.put(this, SerializePrimitive(value))
    }

    infix fun String.to(value: BigDecimal) {
        obj.put(this, SerializePrimitive(value))
    }

    infix fun String.to(value: Nothing?) {
        obj.put(this, SerializeNull)
    }
    //endregion

    //region Nullable
    operator fun set(key: String, value: Nothing?) {
        obj.put(key, SerializeNull)
    }
    //endregion

    //region KSerializer
    context(serializer: KSerializer<T>)
    infix fun <T> String.to(value: T) {
        obj.put(this, NebulaFormat.encodeToElement(value, serializer))
    }
    //endregion

    //region Nested
    infix fun String.arr(scope: SerializeArray.() -> Unit) {
        obj[this] = SerializeArray().apply(scope)
    }
    //endregion

}

inline fun SerializeObject.Companion.build(scope: SerializeObjectBuilder.() -> Unit) =
    SerializeObjectBuilder().apply(scope).obj
