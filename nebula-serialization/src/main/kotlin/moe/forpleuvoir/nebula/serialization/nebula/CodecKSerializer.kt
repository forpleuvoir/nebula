@file:OptIn(InternalSerializationApi::class)

package moe.forpleuvoir.nebula.serialization.nebula

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeCollection
import kotlinx.serialization.encoding.encodeStructure
import moe.forpleuvoir.nebula.serialization.base.SerializeArray
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeNull
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import moe.forpleuvoir.nebula.serialization.codec.Codec

fun <T : Any> Codec<T>.toKSerializer(): KSerializer<T> = CodecKSerializer(this)

class CodecKSerializer<T : Any>(
    val codec: Codec<T>
) : KSerializer<T> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("nebula.codec.${codec::class.simpleName}", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: T) {
        val element = codec.serialization(value)
        encoder.write(element)
    }

    override fun deserialize(decoder: Decoder): T {
        val element = if (decoder is NebulaDecoder) decoder.element
        else error("CodecKSerializer currently only supports NebulaFormat for deserialization")
        return codec.deserialization(element).getOrThrow()
    }
}

@OptIn(ExperimentalSerializationApi::class)
private fun Encoder.write(element: SerializeElement) {
    when (element) {
        is SerializeNull    -> encodeNull()
        is SerializePrimitive -> writePrimitive(element)
        is SerializeObject  -> writeObject(element)
        is SerializeArray   -> writeArray(element)
    }
}

private fun Encoder.writePrimitive(p: SerializePrimitive) = when {
    p.isBoolean -> encodeBoolean(p.asBoolean!!)
    p.isString  -> encodeString(p.asString!!)
    p.isInt     -> encodeInt(p.asInt!!)
    p.isLong    -> encodeLong(p.asLong!!)
    p.isFloat   -> encodeFloat(p.asFloat!!)
    p.isDouble  -> encodeDouble(p.asDouble!!)
    p.isShort   -> encodeShort(p.asShort!!)
    p.isByte    -> encodeByte(p.asByte!!)
    else        -> encodeString(p.asString!!)
}

private fun Encoder.writeObject(obj: SerializeObject) {
    val desc = buildClassSerialDescriptor("object") {
        obj.entries.forEachIndexed { _, (key, value) ->
            element(key, value.toDescriptor())
        }
    }
    encodeStructure(desc) {
        obj.entries.forEachIndexed { i, (_, child) ->
            writeElement(desc, i, child)
        }
    }
}

private fun Encoder.writeArray(arr: SerializeArray) {
    val desc = buildSerialDescriptor("array", StructureKind.LIST) {
        element("element", arr.firstOrNull()?.toDescriptor()
            ?: buildSerialDescriptor("any", StructureKind.CLASS))
    }
    encodeCollection(desc, arr.size) {
        arr.forEachIndexed { i, child ->
            writeElement(desc, i, child)
        }
    }
}

private fun CompositeEncoder.writeElement(desc: SerialDescriptor, index: Int, element: SerializeElement) {
    when (element) {
        is SerializeNull    -> encodeSerializableElement(desc, index, ChildWriter, SerializeNull)
        is SerializePrimitive -> writePrimitiveElement(desc, index, element)
        is SerializeArray,
        is SerializeObject  -> encodeSerializableElement(desc, index, ChildWriter, element)
    }
}

private fun CompositeEncoder.writePrimitiveElement(
    desc: SerialDescriptor, index: Int, p: SerializePrimitive
) = when {
    p.isBoolean -> encodeBooleanElement(desc, index, p.asBoolean!!)
    p.isString  -> encodeStringElement(desc, index, p.asString!!)
    p.isInt     -> encodeIntElement(desc, index, p.asInt!!)
    p.isLong    -> encodeLongElement(desc, index, p.asLong!!)
    p.isFloat   -> encodeFloatElement(desc, index, p.asFloat!!)
    p.isDouble  -> encodeDoubleElement(desc, index, p.asDouble!!)
    p.isShort   -> encodeShortElement(desc, index, p.asShort!!)
    p.isByte    -> encodeByteElement(desc, index, p.asByte!!)
    else        -> encodeStringElement(desc, index, p.asString!!)
}

private object ChildWriter : KSerializer<SerializeElement> {
    override val descriptor: SerialDescriptor = buildSerialDescriptor("child", StructureKind.LIST)
    override fun serialize(encoder: Encoder, value: SerializeElement) { encoder.write(value) }
    override fun deserialize(decoder: Decoder): SerializeElement =
        error("ChildWriter does not support deserialization")
}

private fun SerializeElement.toDescriptor(): SerialDescriptor = when (this) {
    is SerializeNull    -> buildSerialDescriptor("null", StructureKind.CLASS)
    is SerializePrimitive -> PrimitiveSerialDescriptor("primitive", PrimitiveKind.STRING)
    is SerializeObject  -> buildClassSerialDescriptor("object") {
        entries.forEach { (key, value) -> element(key, value.toDescriptor()) }
    }
    is SerializeArray   -> buildSerialDescriptor("array", StructureKind.LIST) {
        element("element", firstOrNull()?.toDescriptor()
            ?: buildSerialDescriptor("any", StructureKind.CLASS))
    }
}
