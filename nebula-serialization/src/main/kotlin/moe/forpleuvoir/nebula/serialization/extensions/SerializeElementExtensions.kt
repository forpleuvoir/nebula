package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.common.valueOf
import moe.forpleuvoir.nebula.serialization.Serializable
import moe.forpleuvoir.nebula.serialization.annotation.Serializable.Companion.findSerializable
import moe.forpleuvoir.nebula.serialization.annotation.Serializable.Companion.getSerializer
import moe.forpleuvoir.nebula.serialization.base.*
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

@Suppress("DuplicatedCode")
fun Any?.toSerializeElement(): SerializeElement {
    return when (this) {
        null                        -> SerializeNull
        is SerializeElement         -> this
        is String                   -> SerializePrimitive(this)
        is Char                     -> SerializePrimitive(this)
        is Boolean                  -> SerializePrimitive(this)
        is BigInteger               -> SerializePrimitive(this)
        is BigDecimal               -> SerializePrimitive(this)
        is Number                   -> SerializePrimitive(this)
        is Map<*, *>                -> this.toSerializeObject()
        is Array<*>, is Iterable<*> -> {//如果是一个数组或可迭代对象
            val iterable = if (this is Array<*>) this.asIterable() else (this as Iterable<*>).asIterable()
            with(iterable) {
                if (this.all { it is Pair<*, *> }) {
                    serializeObject(this.map { (it as Pair<*, *>) }) { it.toString() }
                } else if (this.all { it is Map.Entry<*, *> }) {
                    serializeObject(this.map { (it as Map.Entry<*, *>) }) { it.toString() }
                } else serializeArray(this.iterator())
            }
        }

        else                        -> {
            //如果实现了[Serializable]接口，则调用其[serialization]方法
            if (this is Serializable) {
                return this.serialization()
            }
            //如果有[Serializable]注解，则调用其注解[Serializable.value]的对象实例的[serialization]方法
            this::class.findSerializable {
                return it.getSerializer<Any>().serialization(this)
            }
            //如果有缓存，则直接调用缓存的方法
            SerializeElement.serializerCache[this::class]?.let {
                return it(this)
            }
            //如果有方法名为[serialization]返回值类型为[SerializeElement]的无参方法，则调用该方法
            this::class.declaredFunctions.find {
                it.returnType.jvmErasure.isSubclassOf(SerializeElement::class)
                        && it.name == "serialization"
                        && it.parameters.size == 1
                        && it.parameters[0].type.jvmErasure == this::class
            }?.let {
                return it.call(this) as SerializeElement
            }
            //如果是枚举类型
            if (this::class.java.isEnum) {
                return (this as Enum<*>).toSerializeElement()
            }

            //如果是一个普通对象，则将其转换为[SerializeObject]
            return this.toSerializeObject()
        }
    }
}

fun Enum<*>.toSerializeElement(): SerializeElement {
    return SerializePrimitive(this.name)
}

inline fun <reified E : Enum<*>> Enum.Companion.deserialization(serializeElement: SerializeElement): E {
    return deserialization(E::class, serializeElement)
}

fun <E : Enum<*>> Enum.Companion.deserialization(enumType: KClass<out E>, serializeElement: SerializeElement): E {
    return serializeElement.checkType<SerializePrimitive,E> {
        Enum.valueOf(enumType, it.asString)?:throw IllegalArgumentException("cannot deserialize $enumType,no instance named ${it.asString} found")
    }.getOrThrow()
}

infix fun SerializeElement.completeEquals(target: SerializeElement): Boolean {
    if (this.hashCode() != target.hashCode() || this.javaClass != target.javaClass) return false
    if (this is SerializePrimitive) {
        return this == target
    } else if (this is SerializeNull) {
        return true
    } else if (this is SerializeArray && this.size == (target as SerializeArray).size) {
        var result = false
        forEachIndexed { index, element ->
            result = element completeEquals target[index]
        }
        return result
    } else if (this is SerializeObject && this.size == (target as SerializeObject).size && this.keys == target.keys) {
        var result = false
        forEach { k, v ->
            result = v completeEquals target[k]!!
        }
        return result
    } else return false
}

infix operator fun SerializeArray.contains(target: SerializeElement): Boolean {
    if (contains(target)) return true
    return if (target is SerializeArray) {
        this.containsAll(target)
    } else {
        false
    }
}

infix operator fun SerializeObject.contains(target: SerializeElement): Boolean {
    if (target !is SerializeObject) return false
    return if (keys.containsAll(target.keys)) {
        var result = false
        target.forEach { k, v ->
            result = if (this[k] is SerializeArray) {
                (this[k] as SerializeArray) contains v
            } else if (this[k] is SerializeObject) {
                (this[k] as SerializeObject) contains v
            } else this[k] == v
        }
        result
    } else {
        false
    }
}
