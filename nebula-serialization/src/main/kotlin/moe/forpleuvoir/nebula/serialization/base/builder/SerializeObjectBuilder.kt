package moe.forpleuvoir.nebula.serialization.base.builder

import kotlinx.serialization.KSerializer
import moe.forpleuvoir.nebula.serialization.Serializable
import moe.forpleuvoir.nebula.serialization.base.*
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

    context(codec: Codec<T>)
    operator fun <T> String.invoke(value: T) {
        obj.put(this, value.serialization)
    }

    operator fun String.invoke(scope: SerializeObjectBuilder.() -> Unit) {
        obj[this] = SerializeObjectBuilder().apply(scope).obj
    }

    //region Primitive

    //region Set
    operator fun set(key: String, value: String) {
        obj[key] = SerializePrimitive(value)
    }

    operator fun set(key: String, value: Char) {
        obj[key] = SerializePrimitive(value)
    }

    operator fun set(key: String, value: Boolean) {
        obj[key] = SerializePrimitive(value)
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

    operator fun set(key: String, value: Serializable) {
        obj.put(key, value.serialization())
    }
    //endregion


    //region Invoke
    operator fun String.invoke(value: String) {
        obj.put(this, SerializePrimitive(value))
    }

    operator fun String.invoke(value: Char) {
        obj.put(this, SerializePrimitive(value))
    }

    operator fun String.invoke(value: Boolean) {
        obj.put(this, SerializePrimitive(value))
    }

    operator fun String.invoke(value: Number) {
        obj.put(this, SerializePrimitive(value))
    }

    operator fun String.invoke(value: BigInteger) {
        obj.put(this, SerializePrimitive(value))
    }

    operator fun String.invoke(value: BigDecimal) {
        obj.put(this, SerializePrimitive(value))
    }

    operator fun String.invoke(value: Serializable) {
        obj[this] = value
    }
    //endregion

    //region To
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

    infix fun String.to(value: Serializable) {
        obj[this] = value
    }

    //endregion

    //endregion

    operator fun set(key: String, value: SerializeElement) {
        obj.put(key, value)
    }

    infix fun String.to(value: SerializeElement) {
        obj.put(this, value)
    }

    //region Nullable

    infix fun String.to(value: Nothing?) {
        obj.put(this, SerializeNull)
    }

    operator fun String.invoke(value: Nothing?) {
        obj.put(this, SerializeNull)
    }

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

    infix fun String.arr(arr: SerializeArray) {
        obj[this] = arr
    }
    //endregion

}

inline fun SerializeObject.Companion.build(scope: SerializeObjectBuilder.() -> Unit) =
    SerializeObjectBuilder().apply(scope).obj
