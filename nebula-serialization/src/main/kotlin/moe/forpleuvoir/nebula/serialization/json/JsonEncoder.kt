package moe.forpleuvoir.nebula.serialization.json

import moe.forpleuvoir.nebula.serialization.ast.SyntaxEncoder
import moe.forpleuvoir.nebula.serialization.base.*

class JsonEncoder(
    private val useIndent: Boolean = true,
    private val indentSize: Int = 2,
) : SyntaxEncoder {

    override fun encode(element: SerializeElement): String = buildString {
        buildElement(element, this, 0)
    }

    private fun buildElement(element: SerializeElement, sb: StringBuilder, depth: Int) {
        when (element) {
            is SerializeObject    -> encodeObject(element, sb, depth)
            is SerializeArray     -> encodeArray(element, sb, depth)
            is SerializePrimitive -> encodePrimitive(element, sb)
            is SerializeNull      -> sb.append("null")
        }
    }

    private fun encodeObject(obj: SerializeObject, sb: StringBuilder, depth: Int) {
        sb.append("{")
        if (obj.isEmpty()) {
            sb.append("}")
            return
        }
        appendNewLine(sb, depth + 1)
        var first = true
        for ((key, value) in obj) {
            if (!first) {
                sb.append(",")
                appendNewLine(sb, depth + 1)
            }
            sb.append('"').append(key).append('"').append(':')
            if (useIndent) sb.append(' ')
            buildElement(value, sb, depth + 1)
            first = false
        }
        appendNewLine(sb, depth)
        sb.append("}")
    }

    private fun encodeArray(arr: SerializeArray, sb: StringBuilder, depth: Int) {
        sb.append("[")
        if (arr.isEmpty()) {
            sb.append("]")
            return
        }
        appendNewLine(sb, depth + 1)
        var first = true
        for (element in arr) {
            if (!first) {
                sb.append(",")
                appendNewLine(sb, depth + 1)
            }
            buildElement(element, sb, depth + 1)
            first = false
        }
        appendNewLine(sb, depth)
        sb.append("]")
    }

    private fun encodePrimitive(p: SerializePrimitive, sb: StringBuilder) {
        when (val value = p.value) {
            is String -> sb.append('"').append(escape(value)).append('"')
            is Char   -> sb.append('"').append(escape(value.toString())).append('"')
            else      -> sb.append(value.toString())
        }
    }

    private fun appendNewLine(sb: StringBuilder, depth: Int) {
        if (useIndent) {
            sb.append('\n')
            val total = depth * indentSize
            repeat(total) { sb.append(' ') }
        }
    }

    private fun escape(s: String): String = buildString {
        for (c in s) {
            when (c) {
                '"'      -> append("\\\"")
                '\\'     -> append("\\\\")
                '\b'     -> append("\\b")
                '\u000C' -> append("\\f")
                '\n'     -> append("\\n")
                '\r'     -> append("\\r")
                '\t'     -> append("\\t")
                else     -> if (c < ' ') {
                    append("\\u%04x".format(c.code))
                } else {
                    append(c)
                }
            }
        }
    }

    companion object : SyntaxEncoder {

        val Compress: JsonEncoder = JsonEncoder(useIndent = false)

        private val Default: JsonEncoder = JsonEncoder(useIndent = true)

        override fun encode(element: SerializeElement): String = Default.encode(element)
    }
}
