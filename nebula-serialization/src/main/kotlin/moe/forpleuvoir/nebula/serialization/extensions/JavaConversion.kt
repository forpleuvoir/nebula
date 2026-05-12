package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.serialization.base.SerializeArray
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeNull
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive

fun SerializeElement.toJava(): Any? {
    return when (this) {
        is SerializeArray     -> this.toJava()
        is SerializeObject    -> this.toJava()
        is SerializePrimitive -> this.toJava()
        SerializeNull         -> null
    }
}

fun SerializePrimitive.toJava(): Any {
    return when {
        isString -> asString!!
        isBoolean -> asBoolean!!
        isBigDecimal -> asBigDecimal!!
        isBigInteger -> asBigInteger!!
        isDouble -> asDouble!!
        isFloat -> asFloat!!
        isLong -> asLong!!
        isInt -> asInt!!
        isShort -> asShort!!
        isByte -> asByte!!
        isNumber -> value
        else -> value
    }
}

fun SerializeArray.toJava(): List<Any?> =
    map { e -> e.toJava() }


fun SerializeObject.toJava(): Map<String, Any?> =
    mapValues { (_, v) -> v.toJava() }