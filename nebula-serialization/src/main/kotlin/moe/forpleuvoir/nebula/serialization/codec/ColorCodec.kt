package moe.forpleuvoir.nebula.serialization.codec

import kotlinx.serialization.KSerializer
import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.serialization.DeserializationException
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
            } else if (primitive.isNumber) {
                Color.fromARGB(DeserializationException.require(primitive.asInt) { "Failed to decode the color. Input primitive int is null" })
            } else throw DeserializationException("Failed to decode the color. The input primitive should be a valid color string or number.")
        }
        check<SerializeObject> { obj ->
            if (obj.containsKey("hue", "saturation", "value")) {
                val alpha = obj.getOrElse("alpha", 255)
                val hue = obj.getAsFloat("hue") ?: throw DeserializationException("Failed to decode 'hue' as float from $obj")
                val saturation = obj.getAsFloat("saturation") ?: throw DeserializationException("Failed to decode 'saturation' as float from $obj")
                val value = obj.getAsFloat("value") ?: throw DeserializationException("Failed to decode 'value' as float from $obj")
                Color.fromHSV(hue, saturation, value, alpha)
            } else if (obj.containsKey("red", "green", "blue")) {
                val alpha = obj.getOrElse("alpha", 255)
                val red =
                    obj.getInt("red") ?: obj.getAsFloat("red") ?: throw DeserializationException("Failed to decode 'red' as int or float from $obj")
                val green =
                    obj.getInt("green") ?: obj.getAsFloat("green") ?: throw DeserializationException("Failed to decode 'green' as int or float from $obj")
                val blue =
                    obj.getInt("blue") ?: obj.getAsFloat("blue") ?: throw DeserializationException("Failed to decode 'blue' as int or float from $obj")
                Color.fromARGB(red, green, blue, alpha)
            } else throw DeserializationException("Invalid input: couldn't find either HSV (hue, saturation, value) or RGB (red, green, blue) color data in the provided object. Please ensure the input object contains the required keys.")
        }
    }.toResult()
