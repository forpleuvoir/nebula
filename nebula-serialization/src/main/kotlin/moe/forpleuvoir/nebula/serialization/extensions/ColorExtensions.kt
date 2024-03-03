package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.common.color.ARGBColor
import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.common.color.HSVColor
import moe.forpleuvoir.nebula.common.color.RGBColor
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive

fun RGBColor.serialization(): SerializeElement =
    SerializePrimitive(this.hexStr)

fun ARGBColor.serialization(): SerializeElement =
    SerializePrimitive(this.hexStr)

fun RGBColor.serializationAsObject(): SerializeObject =
    serializeObject {
        "red" to red
        "green" to green
        "blue" to blue
    }

fun ARGBColor.serializationAsObject(): SerializeObject =
    serializeObject {
        "red" to red
        "green" to green
        "blue" to blue
        "alpha" to alpha
    }

fun Color.serialization(): SerializeElement =
    SerializePrimitive(this.hexStr)

fun Color.serializationAsObject(): SerializeObject =
    serializeObject {
        "red" to red
        "green" to green
        "blue" to blue
        "alpha" to alpha
    }

fun Color.deserialization(serializeElement: SerializeElement) {
    this.argb = decodeColor(serializeElement)
}

fun Color.Companion.deserialization(serializeElement: SerializeElement): Color {
    return Color(decodeColor(serializeElement))
}

fun HSVColor.serialization(): SerializeElement =
    SerializePrimitive(this.hexStr)

fun HSVColor.serializationAsObject(): SerializeObject =
    serializeObject {
        "hue" to hue
        "saturation" to saturation
        "value" to value
        "alpha" to alpha
    }

fun HSVColor.deserialization(serializeElement: SerializeElement) {
    this.argb = decodeColor(serializeElement)
}

fun HSVColor.Companion.deserialization(serializeElement: SerializeElement): HSVColor {
    return HSVColor(decodeColor(serializeElement))
}

private fun decodeColor(serializeElement: SerializeElement): Int {
    var argb = 0
    if (serializeElement is SerializePrimitive) {
        if (serializeElement.isString) {
            argb = Color.decode(serializeElement.asString)
        } else if (serializeElement.isNumber) {
            if (Color.isValidColor(serializeElement.asInt.toUInt())) {
                argb = serializeElement.asInt
            }
        }
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