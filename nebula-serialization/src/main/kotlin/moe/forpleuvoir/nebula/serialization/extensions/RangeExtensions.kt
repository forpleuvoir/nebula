package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive

fun <T : Comparable<T>> ClosedRange<T>.serialization(): SerializeElement {
    return SerializePrimitive("${this.start}..${this.endInclusive}")
}

object ClosedRangeDeserializer {

    inline fun <T : Comparable<T>> deserialization(serializeElement: SerializeElement, supplier: (String) -> T): ClosedRange<T> {
        serializeElement.asString.let {
            val pair = it.split("..")
            return supplier(pair[0])..supplier(pair[1])
        }
    }

}

fun IntRange.Companion.deserialization(serializeElement: SerializeElement): IntRange {
    serializeElement.asString.let {
        val pair = it.split("..")
        return pair[0].toInt()..pair[1].toInt()
    }
}

fun UIntRange.Companion.deserialization(serializeElement: SerializeElement): UIntRange {
    serializeElement.asString.let {
        val pair = it.split("..")
        return pair[0].toUInt()..pair[1].toUInt()
    }
}

fun LongRange.Companion.deserialization(serializeElement: SerializeElement): LongRange {
    serializeElement.asString.let {
        val pair = it.split("..")
        return pair[0].toLong()..pair[1].toLong()
    }
}

fun ULongRange.Companion.deserialization(serializeElement: SerializeElement): ULongRange {
    serializeElement.asString.let {
        val pair = it.split("..")
        return pair[0].toULong()..pair[1].toULong()
    }
}

fun CharRange.Companion.deserialization(serializeElement: SerializeElement): CharRange {
    serializeElement.asString.let {
        val pair = it.split("..")
        return pair[0].toCharArray()[0]..pair[1].toCharArray()[0]
    }
}
