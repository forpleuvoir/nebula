package moe.forpleuvoir.nebula.serialization.codec

import kotlinx.serialization.KSerializer
import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.common.util.requireType
import moe.forpleuvoir.nebula.common.util.expectedType
import moe.forpleuvoir.nebula.common.util.letNotNull
import moe.forpleuvoir.nebula.common.util.requireKeysOrNull
import moe.forpleuvoir.nebula.common.util.requireTypeOrNull
import moe.forpleuvoir.nebula.serialization.DeserializationException
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import moe.forpleuvoir.nebula.serialization.extensions.getOrElse
import moe.forpleuvoir.nebula.serialization.extensions.requireFloat
import moe.forpleuvoir.nebula.serialization.nebula.toKSerializer
import java.math.BigInteger

val Color.Companion.CODEC: Codec<Color> by lazy {
    object : Codec<Color> {
        override fun serialization(target: Color): SerializeElement =
            SerializePrimitive(target.hexStr)

        override fun deserialization(data: SerializeElement): Result<Color> = decodeColor(data)
    }
}

object ColorSerializer : KSerializer<Color> by Color.CODEC.toKSerializer()

inline val Codec.Companion.color: Codec<Color> get() = Color.CODEC

private fun decodeColor(data: SerializeElement): Result<Color> = DeserializationException.runCatching {
    data.requireTypeOrNull<SerializePrimitive>().letNotNull { primitive ->
        if (primitive.isString) {
            Color.fromHexString(primitive.asString!!)
        } else if (primitive.isInt || primitive.isLong || primitive.isBigInteger) {
            Color.fromARGB(primitive.asInt!!)
        } else throw expectedType(primitive.valueType, String::class, Int::class, Long::class, BigInteger::class, prefix = "Failed to decode the color.")
    } ?: data.requireTypeOrNull<SerializeObject>().letNotNull { obj ->
        val alpha = obj.getOrElse("alpha", 255)
        obj.requireKeysOrNull("red", "green", "blue").letNotNull { obj ->
            val red = obj["red"]!!.requireType<SerializePrimitive>("Color component red:")
                .let { it.asInt ?: it.asFloat ?: throw expectedType(it.valueType, Int::class, Float::class, prefix = "Color component red:") }
            val green = obj["green"]!!.requireType<SerializePrimitive>("Color component green:")
                .let { it.asInt ?: it.asFloat ?: throw expectedType(it.valueType, Int::class, Float::class, prefix = "Color component green:") }
            val blue = obj["green"]!!.requireType<SerializePrimitive>("Color component blue:")
                .let { it.asInt ?: it.asFloat ?: throw expectedType(it.valueType, Int::class, Float::class, prefix = "Color component blue:") }
            Color.fromARGB(red, green, blue, alpha)
        } ?: obj.requireKeysOrNull("hue", "saturation", "value").letNotNull {
            val hue = obj.requireFloat("hue")
            val saturation = obj.requireFloat("saturation")
            val value = obj.requireFloat("value")
            Color.fromHSV(hue, saturation, value, alpha)
        }
    }
    ?: throw IllegalStateException("Invalid input: couldn't find either HSV (hue, saturation, value) or RGB (red, green, blue) color data in the provided object. Please ensure the input object contains the required keys.")
}


