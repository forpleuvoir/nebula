package moe.forpleuvoir.nebula.serialization.hjson

import moe.forpleuvoir.nebula.serialization.ast.SyntaxEncoder
import moe.forpleuvoir.nebula.serialization.base.*
import moe.forpleuvoir.nebula.serialization.json.JsonLexer

abstract class HJsonEncoder : SyntaxEncoder {

    protected open val rootObjectQuote: Boolean get() = false

    override fun encode(element: SerializeElement): String {
        return when {
            element is SerializeObject && element.isNotEmpty() && !rootObjectQuote -> encodeRootObject(element)
            else                                                                   -> encodeWithIndent(element, 0)
        }
    }

    protected open fun encodeRootObject(obj: SerializeObject): String = buildString {
        val keys = obj.keys.iterator()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = obj[key]!!
            append(encodeEntry(key, value, 0, ""))
            if (keys.hasNext()) append("\n")
        }
    }

    protected open fun encodeWithIndent(
        element: SerializeElement,
        indent: Int,
        forceQuoteString: Boolean = false,
    ): String {
        return when (element) {
            is SerializeObject    -> encodeObject(element, indent)
            is SerializeArray     -> encodeArray(element, indent)
            is SerializePrimitive -> encodePrimitive(element.value, indent, forceQuoteString)
            is SerializeNull      -> "null"
        }
    }

    protected open fun encodeObject(obj: SerializeObject, indent: Int): String {
        if (obj.isEmpty()) return "{}"
        return buildString {
            append("{\n")
            val nextIndent = indent + 2
            val spacing = " ".repeat(nextIndent)
            for ((key, value) in obj) {
                append(encodeEntry(key, value, nextIndent, spacing))
                append("\n")
            }
            append(" ".repeat(indent))
            append("}")
        }
    }

    protected open fun encodeEntry(key: String, value: SerializeElement, indent: Int, spacing: String): String {
        val formattedKey = if (shouldQuote(key)) "\"${escapeString(key)}\"" else key
        return "$spacing$formattedKey: ${encodeWithIndent(value, indent)}"
    }

    protected open fun encodeArray(arr: SerializeArray, indent: Int): String {
        if (arr.isEmpty()) return "[]"

        val singleLine = arr.joinToString(", ") { encodeWithIndent(it, 0, true) }
        if (singleLine.length <= 40) return "[$singleLine]"

        return buildString {
            append("[\n")
            val nextIndent = indent + 2
            val spacing = " ".repeat(nextIndent)
            for (element in arr) {
                append(spacing)
                append(encodeWithIndent(element, nextIndent))
                append("\n")
            }
            append(" ".repeat(indent))
            append("]")
        }
    }

    protected open fun encodePrimitive(value: Any?, indent: Int, forceQuoteString: Boolean): String {
        return when (value) {
            is String -> when {
                value.contains("\n")                                               -> {
                    val spacing = " ".repeat(indent)
                    val indentedContent = value.lines().joinToString("\n") { "  $spacing$it" }
                    "'''\n$indentedContent\n$spacing'''"
                }

                forceQuoteString || shouldQuote(value) || isReservedKeyword(value) ->
                    "\"${escapeString(value)}\""

                else                                                               -> value
            }

            else      -> value.toString()
        }
    }

    protected open fun shouldQuote(s: String): Boolean {
        if (s.isEmpty()) return true
        val reserved = "{}[]:,#\"'"
        if (s.any { c -> c in reserved || c.isWhitespace() }) return true
        val first = s.first()
        if (first == '-' || first == '+' || first.isDigit()) {
            return runCatching { JsonLexer.parseNumber(s) }.isSuccess
        }
        return false
    }

    protected open fun isReservedKeyword(s: String): Boolean =
        s == "true" || s == "false" || s == "null"

    protected open fun escapeString(s: String): String = buildString {
        for (c in s) {
            when (c) {
                '"'      -> append("\\\"")
                '\\'     -> append("\\\\")
                '\b'     -> append("\\b")
                '\u000C' -> append("\\f")
                '\n'     -> append("\\n")
                '\r'     -> append("\\r")
                '\t'     -> append("\\t")
                else     -> if (c.isISOControl()) {
                    append("\\u%04x".format(c.code))
                } else {
                    append(c)
                }
            }
        }
    }

    companion object : SyntaxEncoder {

        private val Default: HJsonEncoder = object : HJsonEncoder() {}

        operator fun invoke(): HJsonEncoder = object : HJsonEncoder() {}

        override fun encode(element: SerializeElement): String = Default.encode(element)
    }
}
