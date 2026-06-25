package moe.forpleuvoir.nebula.serialization.yml

import moe.forpleuvoir.nebula.serialization.ast.SyntaxEncoder
import moe.forpleuvoir.nebula.serialization.base.*

open class YamlEncoder(
    protected val indentSize: Int = 2,
) : SyntaxEncoder {

    override fun encode(element: SerializeElement): String = buildString {
        encodeTopLevel(element, this)
        if (!endsWith("\n")) appendLine()
    }

    protected open fun beforeEntry(path: List<String>, sb: StringBuilder) {}

    protected open fun beforeEntry(path: List<String>, indent: Int, sb: StringBuilder) {
        beforeEntry(path, sb)
    }

    // ── Top-level dispatch ──────────────────────────────────────────

    protected open fun encodeTopLevel(element: SerializeElement, sb: StringBuilder) {
        when (element) {
            is SerializeObject -> encodeObject(element, 0, emptyList(), sb)
            is SerializeArray  -> encodeArray(element, 0, emptyList(), sb)
            is SerializePrimitive -> sb.append(encodePrimitive(element, 0))
            is SerializeNull   -> sb.append("null")
        }
    }

    // ── Object encoding ─────────────────────────────────────────────

    protected open fun encodeObject(
        obj: SerializeObject,
        indent: Int,
        parentPath: List<String>,
        sb: StringBuilder,
    ) {
        if (obj.isEmpty()) {
            sb.append("{}")
            return
        }
        val iter = obj.entries.iterator()
        var first = true
        while (iter.hasNext()) {
            val (key, value) = iter.next()
            val fullPath = parentPath + key

            if (!first) sb.appendLine()
            first = false

            appendIndent(sb, indent)
            beforeEntry(fullPath, indent, sb)
            sb.append(encodeKey(key)).append(": ")
            encodeValue(value, fullPath, indent + indentSize, sb)
        }
    }

    // ── Value encoding (inline or block) ────────────────────────────

    protected open fun encodeValue(
        element: SerializeElement,
        parentPath: List<String>,
        nextIndent: Int,
        sb: StringBuilder,
    ) {
        when (element) {
            is SerializeObject -> {
                if (element.isEmpty()) {
                    sb.append("{}")
                } else {
                    sb.appendLine()
                    encodeObject(element, nextIndent, parentPath, sb)
                }
            }

            is SerializeArray -> {
                if (element.isEmpty()) {
                    sb.append("[]")
                } else {
                    sb.appendLine()
                    encodeArray(element, nextIndent, parentPath, sb)
                }
            }

            is SerializePrimitive -> sb.append(encodePrimitive(element, nextIndent))
            is SerializeNull      -> sb.append("null")
        }
    }

    // ── Array encoding ──────────────────────────────────────────────

    protected open fun encodeArray(
        arr: SerializeArray,
        indent: Int,
        parentPath: List<String>,
        sb: StringBuilder,
    ) {
        if (arr.isEmpty()) {
            sb.append("[]")
            return
        }
        val iter = arr.iterator()
        var first = true
        while (iter.hasNext()) {
            val element = iter.next()
            if (!first) sb.appendLine()
            first = false

            appendIndent(sb, indent)
            beforeEntry(parentPath, indent, sb)
            sb.append("- ")
            encodeInlineElement(element, parentPath, indent + indentSize, sb)
        }
    }

    protected open fun encodeInlineElement(
        element: SerializeElement,
        parentPath: List<String>,
        nextIndent: Int,
        sb: StringBuilder,
    ) {
        when (element) {
            is SerializeObject -> {
                if (element.isEmpty()) {
                    sb.append("{}")
                } else {
                    sb.appendLine()
                    encodeObject(element, nextIndent, parentPath, sb)
                }
            }

            is SerializeArray -> {
                if (element.isEmpty()) {
                    sb.append("[]")
                } else {
                    sb.appendLine()
                    encodeArray(element, nextIndent, parentPath, sb)
                }
            }

            is SerializePrimitive -> sb.append(encodePrimitive(element, nextIndent))
            is SerializeNull      -> sb.append("null")
        }
    }

    // ── Primitive encoding ──────────────────────────────────────────

    protected open fun encodePrimitive(primitive: SerializePrimitive, indent: Int): String {
        val value = primitive.value
        return when (value) {
            is String  -> encodeString(value, indent)
            is Char    -> encodeString(value.toString(), indent)
            is Boolean -> value.toString()
            is Number  -> value.toString()
            else       -> value.toString()
        }
    }

    protected open fun encodeString(s: String, indent: Int): String {
        if (s.isEmpty()) return "''"
        if (s.contains('\n')) return encodeMultilineString(s, indent)
        if (needsQuoting(s)) return "\"${escapeDoubleQuoted(s)}\""
        return s
    }

    protected open fun encodeMultilineString(s: String, indent: Int): String {
        val indented = s.lines().joinToString("\n") { line ->
            if (line.isEmpty()) "" else " ".repeat(indent) + line
        }
        return "|\n$indented"
    }

    protected open fun needsQuoting(s: String): Boolean {
        if (s.isEmpty()) return true
        val specialChars = setOf(':', '#', '{', '}', '[', ']', ',', '&', '*', '?', '|', '-', '<', '>', '=', '!', '%', '@', '`')
        if (s.any { it in specialChars || it.isWhitespace() }) return true
        val first = s.first()
        if (first in "0123456789+") return true
        if (s == "true" || s == "false" || s == "yes" || s == "no" || s == "on" || s == "off" || s == "null" || s == "~") return true
        return false
    }

    protected open fun escapeDoubleQuoted(s: String): String = buildString {
        for (c in s) {
            when (c) {
                '"'  -> append("\\\"")
                '\\' -> append("\\\\")
                '\b' -> append("\\b")
                '\u000C' -> append("\\f")
                '\n' -> append("\\n")
                '\r' -> append("\\r")
                '\t' -> append("\\t")
                else -> if (c.isISOControl()) {
                    append("\\u%04x".format(c.code))
                } else {
                    append(c)
                }
            }
        }
    }

    protected open fun encodeKey(key: String): String {
        if (key.isEmpty()) return "''"
        if (needsQuoting(key)) return "\"${escapeDoubleQuoted(key)}\""
        return key
    }

    protected open fun appendIndent(sb: StringBuilder, indent: Int) {
        if (indent > 0) sb.append(" ".repeat(indent))
    }

    companion object : SyntaxEncoder {
        private val Default = YamlEncoder()
        override fun encode(element: SerializeElement): String = Default.encode(element)
    }
}
