package moe.forpleuvoir.nebula.serialization.codec

import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import moe.forpleuvoir.nebula.serialization.extensions.checkType
import moe.forpleuvoir.nebula.serialization.extensions.getOr

val Color.Companion.CODEC: Codec<Color> by lazy {
    object : Codec<Color> {
        override fun serialization(target: Color): SerializeElement =
            SerializePrimitive(target.hexStr)

        override fun deserialization(element: SerializeElement): Result<Color> = decodeColor(element)
    }
}

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
                val alpha = obj.getOr("alpha", 1f).toFloat()
                val hue = obj["hue"]!!.asFloat!!
                val saturation = obj["saturation"]!!.asFloat!!
                val value = obj["value"]!!.asFloat!!
                Color.fromHSV(hue, saturation, value, alpha)
            } else if (obj.containsKey("red", "green", "blue")) {
                val alpha = obj.getOr("alpha", 255).toInt()
                val red = obj["red"]!!.asInt!!
                val green = obj["green"]!!.asInt!!
                val blue = obj["blue"]!!.asInt!!
                Color.fromARGB(red, green, blue, alpha)
            } else throw IllegalArgumentException("Invalid input: couldn't find either HSV (hue, saturation, value) or RGB (red, green, blue) color data in the provided object. Please ensure the input object contains the required keys.")
        }
    }.toResult()
