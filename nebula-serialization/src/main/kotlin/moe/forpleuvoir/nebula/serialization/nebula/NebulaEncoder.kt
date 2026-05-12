@file:OptIn(ExperimentalSerializationApi::class)

package moe.forpleuvoir.nebula.serialization.nebula

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import moe.forpleuvoir.nebula.serialization.base.SerializeArray
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeNull
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive

class NebulaEncoder(
    override val serializersModule: SerializersModule = EmptySerializersModule()
) : Encoder, CompositeEncoder {

    private val containerStack = ArrayDeque<SerializeElement>()

    private var pendingMapKey: String? = null

    var result: SerializeElement? = null
        private set

    override fun encodeBoolean(value: Boolean) { result = SerializePrimitive(value) }
    override fun encodeByte(value: Byte) { result = SerializePrimitive(value) }
    override fun encodeShort(value: Short) { result = SerializePrimitive(value) }
    override fun encodeInt(value: Int) { result = SerializePrimitive(value) }
    override fun encodeLong(value: Long) { result = SerializePrimitive(value) }
    override fun encodeFloat(value: Float) { result = SerializePrimitive(value) }
    override fun encodeDouble(value: Double) { result = SerializePrimitive(value) }
    override fun encodeChar(value: Char) { result = SerializePrimitive(value) }
    override fun encodeString(value: String) { result = SerializePrimitive(value) }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        result = SerializePrimitive(enumDescriptor.getElementName(index))
    }

    override fun encodeNull() { result = SerializeNull }

    override fun encodeInline(descriptor: SerialDescriptor): Encoder = this

    override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder =
        beginStructure(descriptor)

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        pendingMapKey = null
        val container = when (descriptor.kind) {
            is StructureKind.CLASS, is StructureKind.OBJECT, is StructureKind.MAP -> SerializeObject()
            is StructureKind.LIST -> SerializeArray()
            else -> error("NebulaFormat does not support structure kind: ${descriptor.kind}")
        }
        containerStack.addLast(container)
        return this
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        result = containerStack.removeLast()
    }

    override fun shouldEncodeElementDefault(descriptor: SerialDescriptor, index: Int): Boolean = true

    override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean) {
        encodeElement(descriptor, index, SerializePrimitive(value))
    }
    override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte) {
        encodeElement(descriptor, index, SerializePrimitive(value))
    }
    override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short) {
        encodeElement(descriptor, index, SerializePrimitive(value))
    }
    override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) {
        encodeElement(descriptor, index, SerializePrimitive(value))
    }
    override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long) {
        encodeElement(descriptor, index, SerializePrimitive(value))
    }
    override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float) {
        encodeElement(descriptor, index, SerializePrimitive(value))
    }
    override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double) {
        encodeElement(descriptor, index, SerializePrimitive(value))
    }
    override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char) {
        encodeElement(descriptor, index, SerializePrimitive(value))
    }
    override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) {
        encodeElement(descriptor, index, SerializePrimitive(value))
    }

    override fun <T> encodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T
    ) {
        val subEncoder = NebulaEncoder(serializersModule)
        serializer.serialize(subEncoder, value)
        val element = subEncoder.result ?: error("NebulaEncoder: nested serialization produced null result")
        encodeElement(descriptor, index, element)
    }

    override fun <T : Any> encodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T?
    ) {
        if (value != null) {
            encodeSerializableElement(descriptor, index, serializer, value)
        } else {
            encodeElement(descriptor, index, SerializeNull)
        }
    }

    override fun encodeInlineElement(descriptor: SerialDescriptor, index: Int): Encoder {
        return InlineEncoder(this, descriptor, index)
    }

    private class InlineEncoder(
        private val parent: NebulaEncoder,
        private val descriptor: SerialDescriptor,
        private val index: Int
    ) : Encoder {
        override val serializersModule: SerializersModule get() = parent.serializersModule
        override fun encodeBoolean(value: Boolean) { parent.encodeElement(descriptor, index, SerializePrimitive(value)) }
        override fun encodeByte(value: Byte) { parent.encodeElement(descriptor, index, SerializePrimitive(value)) }
        override fun encodeShort(value: Short) { parent.encodeElement(descriptor, index, SerializePrimitive(value)) }
        override fun encodeInt(value: Int) { parent.encodeElement(descriptor, index, SerializePrimitive(value)) }
        override fun encodeLong(value: Long) { parent.encodeElement(descriptor, index, SerializePrimitive(value)) }
        override fun encodeFloat(value: Float) { parent.encodeElement(descriptor, index, SerializePrimitive(value)) }
        override fun encodeDouble(value: Double) { parent.encodeElement(descriptor, index, SerializePrimitive(value)) }
        override fun encodeChar(value: Char) { parent.encodeElement(descriptor, index, SerializePrimitive(value)) }
        override fun encodeString(value: String) { parent.encodeElement(descriptor, index, SerializePrimitive(value)) }
        override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
            parent.encodeElement(descriptor, this.index, SerializePrimitive(enumDescriptor.getElementName(index)))
        }
        override fun encodeNull() { parent.encodeElement(descriptor, index, SerializeNull) }
        override fun encodeInline(descriptor: SerialDescriptor): Encoder = this
        override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder =
            beginStructure(descriptor)
        override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder = parent
    }

    private fun encodeElement(descriptor: SerialDescriptor, index: Int, element: SerializeElement) {
        if (descriptor.kind is StructureKind.MAP) {
            if (index % 2 == 0) {
                pendingMapKey = when (element) {
                    is SerializePrimitive -> element.asString
                    else -> element.toString()
                } ?: element.toString()
            } else {
                val container = containerStack.last() as SerializeObject
                container[pendingMapKey ?: error("Map value without preceding key")] = element
                pendingMapKey = null
            }
            return
        }
        val container = containerStack.last()
        when (container) {
            is SerializeObject -> container[descriptor.getElementName(index)] = element
            is SerializeArray -> container.add(element)
            else -> error("NebulaEncoder: unexpected container type ${container::class.simpleName}")
        }
    }
}
