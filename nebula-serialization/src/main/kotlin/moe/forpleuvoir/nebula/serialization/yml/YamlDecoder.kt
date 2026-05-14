package moe.forpleuvoir.nebula.serialization.yml

import moe.forpleuvoir.nebula.serialization.base.SerializeArray
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeNull
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import moe.forpleuvoir.nebula.serialization.json.JsonLexer

internal object YamlDecoder {

    fun decode(input: String): Result<SerializeElement> = try {
        val lines = preprocessLines(input)
        if (lines.isEmpty()) return Result.success(SerializeNull)
        val result = parseNode(lines, 0, lines.size, 0).first
        if (result is SerializeObject && result.isEmpty()) Result.success(SerializeNull)
        else Result.success(result)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private data class YamlLine(
        val indent: Int,
        val raw: String,
        val lineNumber: Int,
    )

    private fun preprocessLines(input: String): List<YamlLine> {
        val result = mutableListOf<YamlLine>()
        var lineNumber = 0
        for (rawLine in input.split("\n", "\r\n")) {
            lineNumber++
            val line = rawLine.trimEnd { it == '\n' || it == '\r' }
            if (line.isBlank()) continue
            val trimmedContent = line.trimStart()
            if (trimmedContent.isEmpty() || trimmedContent.startsWith("#")) continue
            if (trimmedContent.startsWith("---") || trimmedContent.startsWith("...")) continue
            val indent = line.length - trimmedContent.length
            result.add(YamlLine(indent, trimmedContent, lineNumber))
        }
        return result
    }

    private fun stripComment(line: String): String {
        var inSingleQuote = false
        var inDoubleQuote = false
        var i = 0
        while (i < line.length) {
            val c = line[i]
            when {
                c == '"' && !inSingleQuote -> inDoubleQuote = !inDoubleQuote
                c == '\'' && !inDoubleQuote -> inSingleQuote = !inSingleQuote
                c == '#' && !inSingleQuote && !inDoubleQuote -> {
                    return line.substring(0, i).trimEnd()
                }
            }
            i++
        }
        return line.trimEnd()
    }

    private fun findUnquotedColon(line: String): Int {
        var inSingleQuote = false
        var inDoubleQuote = false
        for (i in line.indices) {
            val c = line[i]
            when {
                c == '"' && !inSingleQuote -> inDoubleQuote = !inDoubleQuote
                c == '\'' && !inDoubleQuote -> inSingleQuote = !inSingleQuote
                c == ':' && !inSingleQuote && !inDoubleQuote -> return i
            }
        }
        return -1
    }

    /**
     * Parse a node starting at [start] and ending at [end] (exclusive), with the given [parentIndent].
     * Returns the parsed element and the index after the last consumed line.
     */
    private fun parseNode(
        lines: List<YamlLine>,
        start: Int,
        end: Int,
        parentIndent: Int,
    ): Pair<SerializeElement, Int> {
        if (start >= end) return SerializeNull to start

        val firstLine = lines[start]

        if (firstLine.raw.trimStart().startsWith("- ")) {
            return parseSequence(lines, start, end, parentIndent)
        }

        val content = stripComment(firstLine.raw)
        val colonIdx = findUnquotedColon(content)
        if (colonIdx >= 0) {
            return parseMapping(lines, start, end, parentIndent)
        }

        val value = parseScalar(content)
        return value to (start + 1)
    }

    /**
     * Parse a sequence (list) of items starting with "- ".
     */
    private fun parseSequence(
        lines: List<YamlLine>,
        start: Int,
        end: Int,
        parentIndent: Int,
    ): Pair<SerializeArray, Int> {
        val arr = SerializeArray()
        var i = start

        while (i < end) {
            val line = lines[i]
            if (line.indent < parentIndent) break
            val trimmed = line.raw.trimStart()
            if (!trimmed.startsWith("- ")) {
                if (line.indent == parentIndent) break
                i++
                continue
            }

            val afterDash = trimmed.substring(2).trimStart()
            val listItemIndent = line.indent + 2

            if (afterDash.isEmpty()) {
                val nextEnd = findBlockEnd(lines, i + 1, end, listItemIndent)
                if (nextEnd > i + 1) {
                    val (element, _) = parseNode(lines, i + 1, nextEnd, line.indent)
                    arr.add(element)
                    i = nextEnd
                } else {
                    arr.add(SerializeNull)
                    i++
                }
            } else if (afterDash.startsWith("- ")) {
                val (element, consumed) = parseSequence(
                    listOf(YamlLine(listItemIndent, afterDash, line.lineNumber)),
                    0, 1, parentIndent
                )
                arr.add(element)
                i++
            } else if (findUnquotedColon(afterDash) >= 0) {
                val combinedLines = mutableListOf(
                    YamlLine(listItemIndent, afterDash, line.lineNumber)
                )
                val nextEnd = findBlockEnd(lines, i + 1, end, listItemIndent)
                combinedLines.addAll(lines.subList(i + 1, nextEnd))
                val (element, _) = parseMapping(combinedLines, 0, combinedLines.size, parentIndent)
                arr.add(element)
                i = nextEnd
            } else {
                arr.add(parseScalar(afterDash))
                i++
            }
        }

        return arr to i
    }

    /**
     * Parse a mapping (key-value pairs).
     */
    private fun parseMapping(
        lines: List<YamlLine>,
        start: Int,
        end: Int,
        parentIndent: Int,
    ): Pair<SerializeObject, Int> {
        val obj = SerializeObject()
        var i = start

        while (i < end) {
            val line = lines[i]
            if (line.indent < parentIndent) break

            val content = stripComment(line.raw)
            if (content.trimStart().startsWith("- ")) {
                if (line.indent == parentIndent) break
                i++
                continue
            }

            val colonIdx = findUnquotedColon(content)
            if (colonIdx < 0) {
                if (line.indent == parentIndent) break
                i++
                continue
            }

            val key = content.substring(0, colonIdx).trimEnd()
            val keyStr = parseKeyValueString(key)
            val afterColon = content.substring(colonIdx + 1).trimStart()

            if (afterColon.isEmpty()) {
                val childIndent = line.indent + 2
                val nextEnd = findBlockEnd(lines, i + 1, end, childIndent)
                if (nextEnd > i + 1) {
                    val (element, _) = parseNode(lines, i + 1, nextEnd, line.indent)
                    obj[keyStr] = element
                } else {
                    obj[keyStr] = SerializeNull
                }
                i = nextEnd
            } else if (afterColon == "{}") {
                obj[keyStr] = SerializeObject()
                i++
            } else if (afterColon == "[]") {
                obj[keyStr] = SerializeArray()
                i++
            } else if (afterColon.startsWith("- ")) {
                val nestedIndent = line.indent + 2
                val fakeLines = mutableListOf(
                    YamlLine(nestedIndent, afterColon, line.lineNumber)
                )
                val nextEnd = findBlockEnd(lines, i + 1, end, nestedIndent)
                fakeLines.addAll(lines.subList(i + 1, nextEnd))
                val (element, _) = parseSequence(fakeLines, 0, fakeLines.size, parentIndent)
                obj[keyStr] = element
                i = nextEnd
            } else {
                obj[keyStr] = parseScalar(afterColon)
                i++
            }
        }

        return obj to i
    }

    /**
     * Find the end index of a block of lines starting at [start] that are indented at least [minIndent].
     * Stops when a line has indent less than [minIndent], or when a line at [minIndent]
     * starts a new sibling node (a new key-value pair at the same indent as the parent).
     */
    private fun findBlockEnd(
        lines: List<YamlLine>,
        start: Int,
        end: Int,
        minIndent: Int,
    ): Int {
        var i = start
        while (i < end) {
            val line = lines[i]
            if (line.indent < minIndent) break
            i++
        }
        return i
    }

    private fun parseKeyValueString(s: String): String {
        if (s.startsWith("\"") && s.endsWith("\"") && s.length >= 2) {
            return unescapeDoubleQuoted(s.substring(1, s.length - 1))
        }
        if (s.startsWith("'") && s.endsWith("'") && s.length >= 2) {
            return s.substring(1, s.length - 1)
        }
        return s
    }

    private fun parseScalar(s: String): SerializeElement {
        val trimmed = s.trim()
        if (trimmed.isEmpty()) return SerializeNull
        if (trimmed == "null" || trimmed == "~" || trimmed == "NULL") return SerializeNull
        if (trimmed == "{}") return SerializeObject()
        if (trimmed == "[]") return SerializeArray()

        return when {
            trimmed == "true" || trimmed == "True" || trimmed == "TRUE" || trimmed == "yes" || trimmed == "Yes" || trimmed == "YES" || trimmed == "on" || trimmed == "On" || trimmed == "ON" -> SerializePrimitive(true)
            trimmed == "false" || trimmed == "False" || trimmed == "FALSE" || trimmed == "no" || trimmed == "No" || trimmed == "NO" || trimmed == "off" || trimmed == "Off" || trimmed == "OFF" -> SerializePrimitive(false)
            trimmed.startsWith("\"") && trimmed.endsWith("\"") && trimmed.length >= 2 -> SerializePrimitive(unescapeDoubleQuoted(trimmed.substring(1, trimmed.length - 1)))
            trimmed.startsWith("'") && trimmed.endsWith("'") && trimmed.length >= 2 -> SerializePrimitive(trimmed.substring(1, trimmed.length - 1))
            trimmed.matches(Regex("-?\\d+(\\.\\d+)?([eE][+-]?\\d+)?")) -> try {
                val num = JsonLexer.parseNumber(trimmed)
                if (num is Number) SerializePrimitive(num as Number) else SerializePrimitive(num.toString())
            } catch (_: Exception) {
                SerializePrimitive(trimmed)
            }
            else -> SerializePrimitive(trimmed)
        }
    }

    private fun unescapeDoubleQuoted(s: String): String = buildString {
        var i = 0
        var escaped = false
        while (i < s.length) {
            val c = s[i]
            if (escaped) {
                when (c) {
                    '"', '\\', '/' -> append(c)
                    'b' -> append('\b')
                    'f' -> append('\u000C')
                    'n' -> append('\n')
                    'r' -> append('\r')
                    't' -> append('\t')
                    'u' -> {
                        if (i + 4 < s.length) {
                            append(Integer.parseInt(s.substring(i + 1, i + 5), 16).toChar())
                            i += 4
                        } else append(c)
                    }
                    else -> append(c)
                }
                escaped = false
            } else if (c == '\\') {
                escaped = true
            } else {
                append(c)
            }
            i++
        }
    }
}
