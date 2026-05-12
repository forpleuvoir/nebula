package moe.forpleuvoir.nebula.serialization.codec

import kotlinx.serialization.KSerializer
import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import moe.forpleuvoir.nebula.serialization.extensions.checkType
import moe.forpleuvoir.nebula.serialization.extensions.getAsFloat
import moe.forpleuvoir.nebula.serialization.extensions.getInt
import moe.forpleuvoir.nebula.serialization.extensions.getOrElse
import moe.forpleuvoir.nebula.serialization.nebula.toKSerializer

val Color.Companion.CODEC: Codec<Color> by lazy {
    object : Codec<Color> {
        override fun serialization(target: Color): SerializeElement =
            SerializePrimitive(target.hexStr)

        override fun deserialization(element: SerializeElement): Result<Color> = decodeColor(element)
    }
}

object ColorSerializer : KSerializer<Color> by Color.CODEC.toKSerializer()

inline val Codec.Companion.color: Codec<Color> get() = Color.CODEC

private fun decodeColor(serializeElement: SerializeElement): Result<Color> =
    serializeElement.checkType {
        check<SerializePrimitive> { primitive ->
            if (primitive.isString) {
                Color.fromHexString(primitive.asString!!)
            } else if (primitive.isNumber && Color.isValidColor(primitive.asInt!!)) {
                Color.fromARGB(primitive.asInt!!)
            } else throw IllegalArgumentException("Failed to decode the color. The input primitive should be a valid color string or number.")
        }
        check<SerializeObject> { obj ->
            if (obj.containsKey("hue", "saturation", "value")) {
                val alpha = obj.getOrElse("alpha", 255)
                val hue = obj.getAsFloat("hue")!!
                val saturation = obj.getAsFloat("saturation")!!
                val value = obj.getAsFloat("value")!!
                Color.fromHSV(hue, saturation, value, alpha)
            } else if (obj.containsKey("red", "green", "blue")) {
                val alpha = obj.getOrElse("alpha", 255)
                val red = obj.getInt("red") ?: obj.getAsFloat("red")!!
                val green = obj.getInt("green") ?: obj.getAsFloat("green")!!
                val blue = obj.getInt("blue") ?: obj.getAsFloat("blue")!!
                Color.fromARGB(red, green, blue, alpha)
            } else throw IllegalArgumentException("Invalid input: couldn't find either HSV (hue, saturation, value) or RGB (red, green, blue) color data in the provided object. Please ensure the input object contains the required keys.")
        }
    }.toResult()
