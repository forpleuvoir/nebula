@file:OptIn(ExperimentalSerializationApi::class)

package moe.forpleuvoir.nebula.serialization.nebula

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import moe.forpleuvoir.nebula.serialization.base.*

class NebulaDecoder(
    val element: SerializeElement,
    override val serializersModule: SerializersModule = EmptySerializersModule()
) : Decoder {

    override fun decodeNotNullMark(): Boolean = element !is SerializeNull

    override fun decodeNull(): Nothing? = null

    override fun decodeBoolean(): Boolean = element.asBoolean!!
    override fun decodeByte(): Byte = element.asByte!!
    override fun decodeShort(): Short = element.asShort!!
    override fun decodeInt(): Int = element.asInt!!
    override fun decodeLong(): Long = element.asLong!!
    override fun decodeFloat(): Float = element.asFloat!!
    override fun decodeDouble(): Double = element.asDouble!!
    override fun decodeChar(): Char = element.asChar!!
    override fun decodeString(): String = element.asString!!

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int {
        val name = element.asString ?: error("Expected string for enum, got $element")
        return (0 until enumDescriptor.elementsCount).firstOrNull { idx ->
            enumDescriptor.getElementName(idx) == name
        } ?: error("Unknown enum name '$name' for ${enumDescriptor.serialName}")
    }

    override fun decodeInline(descriptor: SerialDescriptor): Decoder = this

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        return when (descriptor.kind) {
            is StructureKind.CLASS, is StructureKind.OBJECT ->
                NebulaObjectDecoder(element.asObject!!, serializersModule)
            is StructureKind.MAP ->
                NebulaMapDecoder(element.asObject!!, serializersModule)
            is StructureKind.LIST ->
                NebulaArrayDecoder(element.asArray!!, serializersModule)
            else -> error("NebulaFormat does not support structure kind: ${descriptor.kind}")
        }
    }

    override fun <T : Any?> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T =
        deserializer.deserialize(this)

    override fun <T : Any> decodeNullableSerializableValue(deserializer: DeserializationStrategy<T?>): T? =
        if (element is SerializeNull) decodeNull() else decodeSerializableValue(deserializer)
}

private class NebulaObjectDecoder(
    private val obj: SerializeObject,
    override val serializersModule: SerializersModule = EmptySerializersModule()
) : CompositeDecoder {

    private var currentIndex = -1

    private lateinit var currentDescriptor: SerialDescriptor

    override fun decodeSequentially(): Boolean = false

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        currentDescriptor = descriptor
        val names = (0 until descriptor.elementsCount).map { descriptor.getElementName(it) }
        while (++currentIndex < names.size) {
            if (obj.containsKey(names[currentIndex])) {
                return currentIndex
            }
        }
        return CompositeDecoder.DECODE_DONE
    }

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = -1

    override fun endStructure(descriptor: SerialDescriptor) {}

    private fun elementAt(index: Int): SerializeElement {
        val name = currentDescriptor.getElementName(index)
        return obj[name] ?: error("Field '$name' not found in $obj")
    }

    override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean =
        elementAt(index).asBoolean!!

    override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte =
        elementAt(index).asByte!!

    override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short =
        elementAt(index).asShort!!

    override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int =
        elementAt(index).asInt!!

    override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long =
        elementAt(index).asLong!!

    override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float =
        elementAt(index).asFloat!!

    override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double =
        elementAt(index).asDouble!!

    override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char =
        elementAt(index).asChar!!

    override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String =
        elementAt(index).asString!!

    override fun <T : Any?> decodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T>,
        previousValue: T?
    ): T {
        val element = elementAt(index)
        val subDecoder = NebulaDecoder(element, serializersModule)
        return deserializer.deserialize(subDecoder)
    }

    override fun <T : Any> decodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T?>,
        previousValue: T?
    ): T? {
        val element = elementAt(index)
        return if (element is SerializeNull) null
        else deserializer.deserialize(NebulaDecoder(element, serializersModule))
    }

    override fun decodeInlineElement(descriptor: SerialDescriptor, index: Int): Decoder =
        NebulaDecoder(elementAt(index), serializersModule)
}

private class NebulaMapDecoder(
    private val obj: SerializeObject,
    override val serializersModule: SerializersModule = EmptySerializersModule()
) : CompositeDecoder {

    private val entries = obj.entries.toList()
    private var expectKey = true
    private var currentEntry = -1

    override fun decodeSequentially(): Boolean = false

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (expectKey) {
            currentEntry++
            if (currentEntry >= entries.size) return CompositeDecoder.DECODE_DONE
            expectKey = false
            return currentEntry * 2
        } else {
            expectKey = true
            return currentEntry * 2 + 1
        }
    }

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = entries.size

    override fun endStructure(descriptor: SerialDescriptor) {}

    private fun keyAt(index: Int): SerializeElement = SerializePrimitive(entries[index / 2].key)

    private fun valueAt(index: Int): SerializeElement = entries[index / 2].value

    override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean =
        if (index % 2 == 0) keyAt(index).asBoolean!! else valueAt(index).asBoolean!!

    override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte =
        if (index % 2 == 0) keyAt(index).asByte!! else valueAt(index).asByte!!

    override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short =
        if (index % 2 == 0) keyAt(index).asShort!! else valueAt(index).asShort!!

    override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int =
        if (index % 2 == 0) keyAt(index).asInt!! else valueAt(index).asInt!!

    override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long =
        if (index % 2 == 0) keyAt(index).asLong!! else valueAt(index).asLong!!

    override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float =
        if (index % 2 == 0) keyAt(index).asFloat!! else valueAt(index).asFloat!!

    override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double =
        if (index % 2 == 0) keyAt(index).asDouble!! else valueAt(index).asDouble!!

    override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char =
        if (index % 2 == 0) keyAt(index).asChar!! else valueAt(index).asChar!!

    override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String =
        if (index % 2 == 0) keyAt(index).asString!! else valueAt(index).asString!!

    override fun <T : Any?> decodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T>,
        previousValue: T?
    ): T {
        val element = if (index % 2 == 0) keyAt(index) else valueAt(index)
        val subDecoder = NebulaDecoder(element, serializersModule)
        return deserializer.deserialize(subDecoder)
    }

    override fun <T : Any> decodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T?>,
        previousValue: T?
    ): T? {
        val element = if (index % 2 == 0) keyAt(index) else valueAt(index)
        return if (element is SerializeNull) null
        else deserializer.deserialize(NebulaDecoder(element, serializersModule))
    }

    override fun decodeInlineElement(descriptor: SerialDescriptor, index: Int): Decoder {
        val element = if (index % 2 == 0) keyAt(index) else valueAt(index)
        return NebulaDecoder(element, serializersModule)
    }
}

private class NebulaArrayDecoder(
    private val array: SerializeArray,
    override val serializersModule: SerializersModule = EmptySerializersModule()
) : CompositeDecoder {

    private var currentIndex = -1

    override fun decodeSequentially(): Boolean = false

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (currentIndex + 1 >= array.size) return CompositeDecoder.DECODE_DONE
        return ++currentIndex
    }

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = array.size

    override fun endStructure(descriptor: SerialDescriptor) {}

    private fun elementAt(index: Int): SerializeElement = array[index]

    override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean =
        elementAt(index).asBoolean!!

    override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte =
        elementAt(index).asByte!!

    override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short =
        elementAt(index).asShort!!

    override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int =
        elementAt(index).asInt!!

    override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long =
        elementAt(index).asLong!!

    override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float =
        elementAt(index).asFloat!!

    override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double =
        elementAt(index).asDouble!!

    override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char =
        elementAt(index).asChar!!

    override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String =
        elementAt(index).asString!!

    override fun <T : Any?> decodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T>,
        previousValue: T?
    ): T {
        val subDecoder = NebulaDecoder(elementAt(index), serializersModule)
        return deserializer.deserialize(subDecoder)
    }

    override fun <T : Any> decodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T?>,
        previousValue: T?
    ): T? {
        val element = elementAt(index)
        return if (element is SerializeNull) null
        else deserializer.deserialize(NebulaDecoder(element, serializersModule))
    }

    override fun decodeInlineElement(descriptor: SerialDescriptor, index: Int): Decoder =
        NebulaDecoder(elementAt(index), serializersModule)
}
