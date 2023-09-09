package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.common.color.HSVColor
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive

fun Color.serialization(): SerializeElement =
    SerializePrimitive(this.hexStr)

fun Color.deserialization(serializeElement: SerializeElement) {
    this.argb = decodeColor(serializeElement)
}

fun HSVColor.deserialization(serializeElement: SerializeElement) {
    this.argb = decodeColor(serializeElement)
}

fun HSVColor.serialization(): SerializeElement =
    SerializePrimitive(this.hexStr)

private fun decodeColor(serializeElement: SerializeElement): Int {
    var argb = 0
    if (serializeElement is SerializePrimitive) {
        if (serializeElement.isString)
            argb = Color.decode(serializeElement.asString)
        else if (serializeElement.isNumber)
            if (Color.isValidColor(serializeElement.asInt.toUInt()))
                argb = serializeElement.asInt
    } else if (serializeElement is SerializeObject) {
        val obj: SerializeObject = serializeElement
        if (obj.containsKey("hue", "saturation", "value")) {
            val alpha = obj.getOr("alpha", 1f).toFloat()
            val hue = obj["hue"]!!.asFloat
            val saturation = obj["saturation"]!!.asFloat
            val value = obj["value"]!!.asFloat
            argb = HSVColor(hue, saturation, value, alpha).argb
        } else if (obj.containsKey("red", "green", "blue")) {
            val alpha = obj.getOr("alpha", 255).toInt()
            val red = obj["red"]!!.asInt
            val green = obj["green"]!!.asInt
            val blue = obj["value"]!!.asInt
            argb = Color(red, green, blue, alpha).argb
        }
    }
    return argb
}