package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.serialization.base.*
import java.math.BigDecimal
import java.math.BigInteger

fun Any?.toSerializeElement(): SerializeElement {
    return when (this) {
        null                -> SerializeNull
        is SerializeElement -> this
        is String           -> SerializePrimitive(this)
        is Char             -> SerializePrimitive(this)
        is Boolean          -> SerializePrimitive(this)
        is BigInteger       -> SerializePrimitive(this)
        is BigDecimal       -> SerializePrimitive(this)
        is Number           -> SerializePrimitive(this)
        is Map<*, *>        -> this.toSerializeObject()
        is Array<*>         -> serializeArray(this.iterator())
        is Collection<*>    -> serializeArray(this)
        else                -> this.toSerializeObject()
    }
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

infix fun SerializeArray.contains(target: SerializeElement): Boolean {
    if (contains(target)) return true
    return if (target is SerializeArray) {
        this.containsAll(target)
    } else {
        false
    }
}

infix fun SerializeObject.contains(target: SerializeElement): Boolean {
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
