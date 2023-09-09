package moe.forpleuvoir.nebula.serialization.json

import moe.forpleuvoir.nebula.serialization.base.*

/**
 * 将序列化元素[moe.forpleuvoir.nebula.serialization.base.SerializeElement]转换为json字符串
 * @param element SerializeElement
 */
class JsonSerializer(
    private val element: SerializeElement,
    private val humanReadable: Boolean = false,
    private val indentationCharacter: String = "  "
) {

    companion object {


    }

    private var indentationLevel = 0


    fun dumpAsString(): String {
        return buildString {

        }
    }

    fun objectDumpAsString(serializeObject: SerializeObject): String {
        return buildString {

        }
    }

    fun arrayDumpAsString(serializeArray: SerializeArray): String {
        return buildString {
            append("[")
            if (humanReadable) {
                append("\n")

            }
            append("]")
        }
    }

    fun primitiveDumpAsString(serializePrimitive: SerializePrimitive): String {
        return serializePrimitive.toString()
    }

    fun nullDumpAsString(serializeNull: SerializeNull): String {
        return serializeNull.toString()
    }


}