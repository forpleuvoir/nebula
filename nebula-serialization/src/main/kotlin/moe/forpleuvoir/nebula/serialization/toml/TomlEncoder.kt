package moe.forpleuvoir.nebula.serialization.toml

import moe.forpleuvoir.nebula.serialization.ast.SyntaxEncoder
import moe.forpleuvoir.nebula.serialization.base.*

open class TomlEncoder(
    protected val keyValueSeparator: String = " = ",
    protected val indent: String = "",
    protected val inlineMaxSize: Int = 4,
) : SyntaxEncoder {

    override fun encode(element: SerializeElement): String = buildString {
        val root = element.asObject
        if (root != null) {
            encodeObject(root, emptyList(), this)
        } else {
            append(encodeValue(element))
        }
    }

    // ── Hook methods (override for comment support) ─────────────

    protected open fun beforeTableHeader(path: List<String>, sb: StringBuilder) {}

    protected open fun beforeArrayTableHeader(path: List<String>, sb: StringBuilder) {}

    protected open fun beforeEntry(path: List<String>, sb: StringBuilder) {}

    // ── Object (table) encoding ───────────────────────────────────

    protected open fun encodeObject(obj: SerializeObject, parentPath: List<String>, sb: StringBuilder) {
        if (parentPath.isEmpty()) {
            encodeRootObject(obj, sb)
        } else {
            encodeTableObject(obj, parentPath, sb)
        }
    }

    private fun encodeRootObject(obj: SerializeObject, sb: StringBuilder) {
        val entries = obj.entries.toList()
        for ((key, value) in entries) {
            if (isSectionLike(key, value, emptyList())) continue
            emitLeafEntry(key, value, emptyList(), sb)
        }
        for ((key, value) in entries) {
            if (!isSectionLike(key, value, emptyList())) continue
            emitSectionEntry(key, value, emptyList(), sb)
        }
    }

    private fun encodeTableObject(obj: SerializeObject, parentPath: List<String>, sb: StringBuilder) {
        for ((key, value) in obj) {
            emitLeafEntry(key, value, parentPath, sb)
        }
        for ((key, value) in obj) {
            emitSectionEntry(key, value, parentPath, sb)
        }
    }

    private fun isSectionLike(key: String, element: SerializeElement, parentPath: List<String>): Boolean = when (element) {
        is SerializeObject -> shouldExpand(element, parentPath + key)
        is SerializeArray  -> element.isNotEmpty() && element.all { it is SerializeObject }
        else               -> false
    }

    private fun emitLeafEntry(key: String, value: SerializeElement, parentPath: List<String>, sb: StringBuilder) {
        when (value) {
            is SerializePrimitive -> {
                val fullPath = parentPath + key
                beforeEntry(fullPath, sb)
                sb.appendLine("${encodeKey(key)}$keyValueSeparator${encodePrimitive(value)}")
            }

            is SerializeNull      -> {}

            is SerializeArray     -> {
                if (!isSectionLike(key, value, parentPath)) {
                    val fullPath = parentPath + key
                    beforeEntry(fullPath, sb)
                    sb.appendLine("${encodeKey(key)}$keyValueSeparator${encodeArray(value)}")
                }
            }

            is SerializeObject    -> {
                if (!shouldExpand(value, parentPath + key)) {
                    val fullPath = parentPath + key
                    beforeEntry(fullPath, sb)
                    sb.appendLine("${encodeKey(key)}$keyValueSeparator${encodeInlineTable(value)}")
                }
            }
        }
    }

    private fun emitSectionEntry(key: String, value: SerializeElement, parentPath: List<String>, sb: StringBuilder) {
        when (value) {
            is SerializeObject -> {
                if (shouldExpand(value, parentPath + key)) {
                    val path = parentPath + key
                    ensureBlankLineSep(sb)
                    beforeTableHeader(path, sb)
                    sb.appendLine("[${path.joinToString(".")}]")
                    encodeTableObject(value, path, sb)
                }
            }

            is SerializeArray  -> if (isSectionLike(key, value, parentPath)) {
                val path = parentPath + key
                for (element in value) {
                    if (element is SerializeObject) {
                        ensureBlankLineSep(sb)
                        beforeArrayTableHeader(path, sb)
                        sb.appendLine("[[${path.joinToString(".")}]]")
                        encodeTableObject(element, path, sb)
                    }
                }
            }

            else               -> {}
        }
    }

    protected open fun ensureBlankLineSep(sb: StringBuilder) {
        if (sb.isEmpty()) return
        if (sb.endsWith("\n\n")) return
        if (sb.endsWith("\n")) sb.appendLine()
        sb.appendLine()
    }

    protected open fun shouldExpand(obj: SerializeObject, parentPath: List<String> = emptyList()): Boolean {
        if (recursiveEntryCount(obj) > inlineMaxSize) return true
        for ((_, v) in obj) {
            when {
                v is SerializeObject                                                     -> return true
                v is SerializeArray && v.isNotEmpty() && v.all { it is SerializeObject } -> return true
            }
        }
        return false
    }

    protected open fun recursiveEntryCount(element: SerializeElement): Int = when (element) {
        is SerializeObject -> element.entries.sumOf { (_, v) -> recursiveEntryCount(v) }
        is SerializeArray  -> element.sumOf { recursiveEntryCount(it) }
        else               -> 1
    }

    // ── Value encoding ────────────────────────────────────────────

    protected open fun encodeValue(element: SerializeElement): String = when (element) {
        is SerializePrimitive -> encodePrimitive(element)
        is SerializeArray     -> encodeArray(element)
        is SerializeObject    -> encodeInlineTable(element)
        is SerializeNull      -> ""
    }

    protected open fun encodePrimitive(primitive: SerializePrimitive): String {
        val value = primitive.value
        return when (value) {
            is String -> encodeString(value)
            is Char   -> encodeString(value.toString())
            else      -> value.toString()
        }
    }

    protected open fun encodeString(s: String): String {
        if (s.contains('\n') || s.contains("\r\n")) {
            return encodeMultilineBasicString(s)
        }
        return "\"${escapeBasicString(s)}\""
    }

    protected open fun encodeMultilineBasicString(s: String): String {
        val escaped = s.replace("\\", "\\\\")
            .replace("\"\"\"", "\\\"\\\"\\\"")
        return "\"\"\"\n$escaped\n\"\"\""
    }

    protected open fun escapeBasicString(s: String): String = buildString {
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

    protected open fun encodeArray(arr: SerializeArray): String {
        val parts = arr.filter { it !is SerializeNull }.joinToString(", ") { encodeValueInline(it) }
        return "[$parts]"
    }

    protected open fun encodeValueInline(element: SerializeElement): String = when (element) {
        is SerializePrimitive -> encodePrimitive(element)
        is SerializeArray     -> encodeArrayInlinesafe(element)
        is SerializeObject    -> encodeInlineTable(element)
        is SerializeNull      -> ""
    }

    protected open fun encodeArrayInlinesafe(arr: SerializeArray): String {
        val parts = arr.filter { it !is SerializeNull }.joinToString(", ") { encodeValueInline(it) }
        return "[$parts]"
    }

    protected open fun encodeInlineTable(obj: SerializeObject): String {
        if (obj.isEmpty()) return "{}"
        val entries = obj.entries
            .filter { (_, v) -> v !is SerializeNull }
            .joinToString(", ") { (key, value) ->
                "${encodeKey(key)} = ${encodeValueInline(value)}"
            }
        return if (entries.isEmpty()) "{}" else "{ $entries }"
    }

    protected open fun encodeKey(key: String): String {
        if (key.all { it in 'A'..'Z' || it in 'a'..'z' || it in '0'..'9' || it == '-' || it == '_' }) {
            return key
        }
        return "\"${escapeBasicString(key)}\""
    }

    // ── Array-of-tables detection ─────────────────────────────────

    protected open fun isTableArray(arr: SerializeArray): Boolean {
        if (arr.isEmpty()) return false
        return arr.all { it is SerializeObject }
    }

    companion object : SyntaxEncoder {

        private val Default = TomlEncoder()

        override fun encode(element: SerializeElement): String = Default.encode(element)
    }
}
