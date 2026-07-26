package moe.forpleuvoir.nebula.serialization.toml

import moe.forpleuvoir.nebula.serialization.ast.Lexer
import moe.forpleuvoir.nebula.serialization.ast.Primitive
import moe.forpleuvoir.nebula.serialization.ast.Token
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.ArrayOfTablesHeader
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.EOF
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.Identifier
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.Literal
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.Symbol
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.TableHeader
import moe.forpleuvoir.nebula.serialization.ast.TokenPos
import java.math.BigDecimal
import java.math.BigInteger

object TomlLexer : Lexer {

    override fun tokenize(input: String): Result<List<Token>> = runCatching {
        val tokens = mutableListOf<Token>()
        var i = 0
        var line = 1
        var col = 1

        while (i < input.length) {
            val c = input[i]
            when (c) {
                '\n'            -> {
                    i++; line++; col = 1
                }
                ' ', '\t', '\r' -> {
                    i++; col++
                }
                '#'             -> {
                    i = skipToEndOfLine(input, i)
                }
                '['             -> {
                    val pos = TokenPos(line, col, i)
                    if (i + 1 < input.length && input[i + 1] == '[') {
                        val r = readArrayOfTablesHeader(input, i + 2, line, col + 2)
                        tokens.add(ArrayOfTablesHeader(r.path, pos))
                        i = r.i; line = r.line; col = r.col
                    } else {
                        val r = readTableHeader(input, i + 1, line, col + 1)
                        tokens.add(TableHeader(r.path, pos))
                        i = r.i; line = r.line; col = r.col
                    }
                }
                else            -> {
                    val keyResult = readKey(input, i, line, col)
                    tokens.addAll(keyResult.tokens)
                    i = keyResult.i; line = keyResult.line; col = keyResult.col

                    val ws = skipWhitespaceInline(input, i)
                    i = ws.i; col = bumpCol(col, ws.delta)

                    if (i < input.length && input[i] == '=') {
                        tokens.add(Symbol("=", TokenPos(line, col, i)))
                        i++; col++

                        val ws2 = skipWhitespaceInline(input, i)
                        i = ws2.i; col = bumpCol(col, ws2.delta)

                        val valueResult = readValue(input, i, line, col)
                        tokens.addAll(valueResult.tokens)
                        i = valueResult.i; line = valueResult.line; col = valueResult.col
                    }

                    i = skipToEndOfLine(input, i)
                }
            }
        }

        tokens.add(EOF(TokenPos(line, col, i)))
        tokens
    }

    // ── helpers ──────────────────────────────────────────────────

    private fun skipToEndOfLine(input: String, cursor: Int): Int {
        var i = cursor; while (i < input.length && input[i] != '\n') i++; return i
    }

    private fun skipWhitespaceInline(input: String, cursor: Int): SimplePos {
        var i = cursor
        var d = 0
        while (i < input.length && (input[i] == ' ' || input[i] == '\t')) {
            i++; d++
        }
        return SimplePos(i, d)
    }

    private data class SimplePos(val i: Int, val delta: Int)

    private data class ReadKeyResult(
        val tokens: List<Token>,
        val i: Int, val line: Int, val col: Int,
    )

    private data class ReadValueResult(
        val tokens: List<Token>,
        val i: Int, val line: Int, val col: Int,
    )

    private data class HeaderResult(
        val path: List<String>,
        val i: Int, val line: Int, val col: Int,
    )

    private data class ReadStringResult(val value: String, val i: Int, val line: Int, val col: Int)

    private fun bumpCol(curr: Int, added: Int): Int = if (added == 0) curr else curr + added

    // ── Table headers ─────────────────────────────────────────────

    private fun readTableHeader(input: String, start: Int, startLine: Int, startCol: Int): HeaderResult {
        val parts = mutableListOf<String>()
        var i = start
        var line = startLine
        var col = startCol

        while (i < input.length) {
            when (input[i]) {
                ']'                   -> {
                    i++; col++
                    i = skipToEndOfLine(input, i)
                    return HeaderResult(parts, i, line, 1)
                }

                ' ', '\t', '\r', '\n' -> {
                    if (input[i] == '\n') {
                        line++; col = 1
                    } else {
                        i++; col++
                    }
                }

                '#'                   -> {
                    i = skipToEndOfLine(input, i)
                }

                else                  -> {
                    val r = readKeyPart(input, i, line, col)
                    parts.add(r.value)
                    i = r.i; line = r.line; col = r.col
                    val ws = skipWhitespaceInline(input, i)
                    i = ws.i; col = bumpCol(col, ws.delta)
                    if (i < input.length && input[i] == '.') {
                        i++; col++
                    }
                }
            }
        }
        throw IllegalArgumentException("Unclosed table header")
    }

    private fun readArrayOfTablesHeader(input: String, start: Int, startLine: Int, startCol: Int): HeaderResult {
        val parts = mutableListOf<String>()
        var i = start
        var line = startLine
        var col = startCol

        while (i < input.length) {
            when (input[i]) {
                ']'                   -> {
                    if (i + 1 < input.length && input[i + 1] == ']') {
                        i += 2; col += 2
                        i = skipToEndOfLine(input, i)
                        return HeaderResult(parts, i, line, 1)
                    }
                    i++; col++
                }

                ' ', '\t', '\r', '\n' -> {
                    if (input[i] == '\n') {
                        line++; col = 1
                    } else {
                        i++; col++
                    }
                }

                '#'                   -> {
                    i = skipToEndOfLine(input, i)
                }

                else                  -> {
                    val r = readKeyPart(input, i, line, col)
                    parts.add(r.value)
                    i = r.i; line = r.line; col = r.col
                    val ws = skipWhitespaceInline(input, i)
                    i = ws.i; col = bumpCol(col, ws.delta)
                    if (i < input.length && input[i] == '.') {
                        i++; col++
                    }
                }
            }
        }
        throw IllegalArgumentException("Unclosed array-of-tables header")
    }

    private data class KeyPartResult(val value: String, val i: Int, val line: Int, val col: Int)

    private fun readKeyPart(input: String, start: Int, startLine: Int, startCol: Int): KeyPartResult {
        return if (input[start] == '"') {
            val r = readBasicString(input, start)
            KeyPartResult(r.value, r.i, r.line, r.col)
        } else if (input[start] == '\'') {
            val r = readLiteralString(input, start)
            KeyPartResult(r.value, r.i, r.line, r.col)
        } else {
            readBareKey(input, start, startLine, startCol)
        }
    }

    // ── Key reading ───────────────────────────────────────────────

    private fun readKey(input: String, start: Int, startLine: Int, startCol: Int): ReadKeyResult {
        val tokens = mutableListOf<Token>()
        var i = start
        var line = startLine
        var col = startCol

        while (i < input.length) {
            val ws = skipWhitespaceInline(input, i)
            i = ws.i; col = bumpCol(col, ws.delta)
            if (i >= input.length) break

            val c = input[i]
            if (c == '=' || c == '\n' || c == '#' || c == ']' || c == '}' || c == ',') break

            if (tokens.isNotEmpty() && c == '.') {
                tokens.add(Symbol(".", TokenPos(line, col, i)))
                i++; col++
                continue
            }

            val pos = TokenPos(line, col, i)
            when (c) {
                '"' if isMultiStart(input, i)  -> {
                    val r = readMultilineBasicString(input, i)
                    tokens.add(Literal(Primitive.of(r.value), pos))
                    i = r.i; line = r.line; col = r.col
                }
                '\'' if isMultiStart(input, i) -> {
                    val r = readMultilineLiteralString(input, i)
                    tokens.add(Literal(Primitive.of(r.value), pos))
                    i = r.i; line = r.line; col = r.col
                }
                '"'                            -> {
                    val r = readBasicString(input, i)
                    tokens.add(Literal(Primitive.of(r.value), pos))
                    i = r.i; line = r.line; col = r.col
                }
                '\''                           -> {
                    val r = readLiteralString(input, i)
                    tokens.add(Literal(Primitive.of(r.value), pos))
                    i = r.i; line = r.line; col = r.col
                }
                else                           -> {
                    val r = readBareKey(input, i, line, col)
                    tokens.add(Identifier(r.value, pos))
                    i = r.i; line = r.line; col = r.col
                }
            }
        }

        return ReadKeyResult(tokens, i, line, col)
    }

    private fun readBareKey(input: String, start: Int, startLine: Int, startCol: Int): KeyPartResult {
        val sb = StringBuilder()
        var i = start
        var col = startCol
        while (i < input.length && input[i].isBareKeyChar()) {
            sb.append(input[i]); i++; col++
        }
        return KeyPartResult(sb.toString(), i, startLine, col)
    }

    private fun Char.isBareKeyChar(): Boolean = this in 'A'..'Z' || this in 'a'..'z' ||
            this in '0'..'9' || this == '-' || this == '_'

    // ── Value reading ─────────────────────────────────────────────

    private fun readValue(input: String, start: Int, startLine: Int, startCol: Int): ReadValueResult {
        if (start >= input.length) throw IllegalArgumentException("Unexpected end of input")
        val c = input[start]
        val pos = TokenPos(startLine, startCol, start)

        return when {
            c == '"'             -> {
                if (isMultiStart(input, start)) {
                    val r = readMultilineBasicString(input, start)
                    ReadValueResult(listOf(Literal(Primitive.of(r.value), pos)), r.i, r.line, r.col)
                } else {
                    val r = readBasicString(input, start)
                    ReadValueResult(listOf(Literal(Primitive.of(r.value), pos)), r.i, r.line, r.col)
                }
            }

            c == '\''            -> {
                if (isMultiStart(input, start)) {
                    val r = readMultilineLiteralString(input, start)
                    ReadValueResult(listOf(Literal(Primitive.of(r.value), pos)), r.i, r.line, r.col)
                } else {
                    val r = readLiteralString(input, start)
                    ReadValueResult(listOf(Literal(Primitive.of(r.value), pos)), r.i, r.line, r.col)
                }
            }

            c == '['             -> readArray(input, start, startLine, startCol)

            c == '{'             -> readInlineTable(input, start, startLine, startCol)

            c == 't' || c == 'f' -> {
                val r = readAlphaWord(input, start)
                when (r.word) {
                    "true"  -> ReadValueResult(listOf(Literal(Primitive.of(true), pos)), r.i, startLine, startCol + r.word.length)
                    "false" -> ReadValueResult(listOf(Literal(Primitive.of(false), pos)), r.i, startLine, startCol + r.word.length)
                    else    -> throw IllegalArgumentException("Invalid boolean '${r.word}'")
                }
            }

            c == '+' || c == '-' -> {
                val r = readValueWord(input, start)
                val lower = r.word.lowercase()
                if (lower in listOf("inf", "+inf", "-inf", "nan", "+nan", "-nan")) {
                    ReadValueResult(listOf(Literal(Primitive.of(parseSpecialFloat(r.word)), pos)), r.i, startLine, startCol + r.word.length)
                } else {
                    val n = parseTomlNumber(r.word)
                        ?: throw IllegalArgumentException("Invalid number '${r.word}'")
                    ReadValueResult(listOf(Literal(Primitive.of(n), pos)), r.i, startLine, startCol + r.word.length)
                }
            }

            c == 'n'             -> {
                val r = readAlphaWord(input, start)
                when (r.word) {
                    "nan" -> ReadValueResult(listOf(Literal(Primitive.of(Double.NaN), pos)), r.i, startLine, startCol + 3)
                    "inf" -> ReadValueResult(listOf(Literal(Primitive.of(Double.POSITIVE_INFINITY), pos)), r.i, startLine, startCol + 3)
                    else  -> throw IllegalArgumentException("Invalid value '${r.word}'")
                }
            }

            c == 'i'             -> {
                val r = readAlphaWord(input, start)
                if (r.word == "inf") {
                    ReadValueResult(listOf(Literal(Primitive.of(Double.POSITIVE_INFINITY), pos)), r.i, startLine, startCol + 3)
                } else throw IllegalArgumentException("Invalid value '${r.word}'")
            }

            c.isDigit()          -> {
                val r = readValueWord(input, start)
                val date = tryParseDateTime(r.word)
                if (date != null) {
                    ReadValueResult(listOf(Literal(Primitive.of(date), pos)), r.i, startLine, startCol + r.word.length)
                } else {
                    val n = parseTomlNumber(r.word)
                        ?: throw IllegalArgumentException("Invalid number '${r.word}'")
                    ReadValueResult(listOf(Literal(Primitive.of(n), pos)), r.i, startLine, startCol + r.word.length)
                }
            }

            else                 -> {
                val r = readAlphaWord(input, start)
                val date = tryParseDateTime(r.word)
                if (date != null) {
                    ReadValueResult(listOf(Literal(Primitive.of(date), pos)), r.i, startLine, startCol + r.word.length)
                } else {
                    val n = parseTomlNumber(r.word)
                    if (n != null) {
                        ReadValueResult(listOf(Literal(Primitive.of(n), pos)), r.i, startLine, startCol + r.word.length)
                    } else throw IllegalArgumentException("Invalid value '${r.word}'")
                }
            }
        }
    }

    private data class AlphaWordResult(val word: String, val i: Int)

    private fun readAlphaWord(input: String, start: Int): AlphaWordResult {
        var i = start
        while (i < input.length && (input[i].isLetter() || input[i] == '_')) i++
        return AlphaWordResult(input.substring(start, i), i)
    }

    private fun readValueWord(input: String, start: Int): AlphaWordResult {
        var i = start
        while (i < input.length) {
            val c = input[i]
            if (c.isLetterOrDigit() || c == '+' || c == '-' || c == '_' || c == '.' || c == ':' || c == 'T' || c == 'Z') i++
            else break
        }
        return AlphaWordResult(input.substring(start, i), i)
    }

    // ── String parsing ────────────────────────────────────────────

    private fun isMultiStart(input: String, start: Int): Boolean =
        start + 2 < input.length && input[start] == input[start + 1] && input[start] == input[start + 2]

    private fun readBasicString(input: String, start: Int): ReadStringResult {
        val sb = StringBuilder()
        var i = start + 1
        var escaped = false
        while (i < input.length) {
            val c = input[i]
            if (escaped) {
                when (c) {
                    '"', '\\'  -> sb.append(c)
                    'b'        -> sb.append('\b')
                    'f'        -> sb.append('\u000C')
                    'n'        -> sb.append('\n')
                    'r'        -> sb.append('\r')
                    't'        -> sb.append('\t')
                    'u'        -> {
                        if (i + 4 < input.length) {
                            sb.append(Integer.parseInt(input.substring(i + 1, i + 5), 16).toChar()); i += 4
                        } else throw IllegalArgumentException("Invalid unicode escape")
                    }

                    'U'        -> {
                        if (i + 8 < input.length) {
                            sb.append(Character.toChars(Integer.parseInt(input.substring(i + 1, i + 9), 16)).joinToString("")); i += 8
                        } else throw IllegalArgumentException("Invalid unicode escape")
                    }

                    '\n', '\r' -> {}
                    else       -> sb.append(c)
                }
                escaped = false
            } else if (c == '\\') {
                escaped = true
            } else if (c == '"') {
                return ReadStringResult(sb.toString(), i + 1, 1, 1)
            } else if (c == '\n' || c == '\r') {
                throw IllegalArgumentException("Newline in basic string")
            } else {
                sb.append(c)
            }
            i++
        }
        throw IllegalArgumentException("Unclosed basic string")
    }

    private fun readMultilineBasicString(input: String, start: Int): ReadStringResult {
        var i = start + 3
        var line = 1
        var col = 1
        if (i < input.length && input[i] == '\n') {
            i++; line++; col = 1
        } else if (i + 1 < input.length && input[i] == '\r' && input[i + 1] == '\n') {
            i += 2; line++; col = 1
        }

        val sb = StringBuilder()
        var escaped = false
        var consecutiveQuotes = 0

        while (i < input.length) {
            val c = input[i]
            if (escaped) {
                when (c) {
                    '\n'      -> {
                        escaped = false; i++; line++; col = 1; continue
                    }

                    '\r'      -> {
                        escaped = false; i++; continue
                    }

                    ' ', '\t' -> {
                        while (i < input.length && input[i] != '\n' && input[i] != '\r') i++
                        escaped = false; continue
                    }

                    '"', '\\' -> sb.append(c)
                    'b'       -> sb.append('\b')
                    'f'       -> sb.append('\u000C')
                    'n'       -> sb.append('\n')
                    'r'       -> sb.append('\r')
                    't'       -> sb.append('\t')
                    'u'       -> {
                        if (i + 4 < input.length) {
                            sb.append(Integer.parseInt(input.substring(i + 1, i + 5), 16).toChar()); i += 4
                        } else throw IllegalArgumentException("Invalid unicode escape")
                    }

                    'U'       -> {
                        if (i + 8 < input.length) {
                            sb.append(Character.toChars(Integer.parseInt(input.substring(i + 1, i + 9), 16)).joinToString("")); i += 8
                        } else throw IllegalArgumentException("Invalid unicode escape")
                    }

                    else      -> sb.append(c)
                }
                escaped = false; consecutiveQuotes = 0
            } else if (c == '\\') {
                escaped = true; consecutiveQuotes = 0
            } else if (c == '"') {
                consecutiveQuotes++
                if (consecutiveQuotes >= 3) return ReadStringResult(sb.toString(), i + 1, line, col + 1)
                sb.append(c)
            } else {
                consecutiveQuotes = 0; sb.append(c)
            }
            i++; col++
            if (c == '\n') {
                line++; col = 1
            }
        }
        throw IllegalArgumentException("Unclosed multi-line basic string")
    }

    private fun readLiteralString(input: String, start: Int): ReadStringResult {
        val sb = StringBuilder()
        var i = start + 1
        while (i < input.length) {
            val c = input[i]
            if (c == '\'') return ReadStringResult(sb.toString(), i + 1, 1, 1)
            if (c == '\n' || c == '\r') throw IllegalArgumentException("Newline in literal string")
            sb.append(c); i++
        }
        throw IllegalArgumentException("Unclosed literal string")
    }

    private fun readMultilineLiteralString(input: String, start: Int): ReadStringResult {
        var i = start + 3
        var line = 1
        var col = 1
        if (i < input.length && input[i] == '\n') {
            i++; line++; col = 1
        } else if (i + 1 < input.length && input[i] == '\r' && input[i + 1] == '\n') {
            i += 2; line++; col = 1
        }

        val sb = StringBuilder()
        var consecutiveQuotes = 0
        while (i < input.length) {
            val c = input[i]
            if (c == '\'') {
                consecutiveQuotes++
                if (consecutiveQuotes >= 3) return ReadStringResult(sb.toString(), i + 1, line, col + 1)
                sb.append(c)
            } else {
                consecutiveQuotes = 0; sb.append(c)
            }
            i++; col++
            if (c == '\n') {
                line++; col = 1
            }
        }
        throw IllegalArgumentException("Unclosed multi-line literal string")
    }

    // ── Array parsing ─────────────────────────────────────────────

    private fun readArray(input: String, start: Int, startLine: Int, startCol: Int): ReadValueResult {
        val tokens = mutableListOf<Token>()
        tokens.add(Symbol("[", TokenPos(startLine, startCol, start)))
        var i = start + 1
        var line = startLine
        var col = startCol + 1

        while (i < input.length) {
            when (input[i]) {
                '\n'            -> {
                    i++; line++; col = 1
                }

                ' ', '\t', '\r' -> {
                    i++; col++
                }

                '#'             -> {
                    i = skipToEndOfLine(input, i)
                }

                ']'             -> {
                    tokens.add(Symbol("]", TokenPos(line, col, i))); i++; col++
                    return ReadValueResult(tokens, i, line, col)
                }

                ','             -> {
                    tokens.add(Symbol(",", TokenPos(line, col, i))); i++; col++
                }

                else            -> {
                    val vr = readValue(input, i, line, col)
                    tokens.addAll(vr.tokens)
                    i = vr.i; line = vr.line; col = vr.col
                }
            }
        }
        throw IllegalArgumentException("Unclosed array")
    }

    // ── Inline table parsing ──────────────────────────────────────

    private fun readInlineTable(input: String, start: Int, startLine: Int, startCol: Int): ReadValueResult {
        val tokens = mutableListOf<Token>()
        tokens.add(Symbol("{", TokenPos(startLine, startCol, start)))
        var i = start + 1
        var line = startLine
        var col = startCol + 1

        while (i < input.length) {
            when (input[i]) {
                '\n'            -> {
                    i++; line++; col = 1; continue
                }

                ' ', '\t', '\r' -> {
                    i++; col++; continue
                }

                '#'             -> {
                    i = skipToEndOfLine(input, i); continue
                }

                '}'             -> {
                    tokens.add(Symbol("}", TokenPos(line, col, i))); i++; col++
                    return ReadValueResult(tokens, i, line, col)
                }

                ','             -> {
                    tokens.add(Symbol(",", TokenPos(line, col, i))); i++; col++; continue
                }

                else            -> {
                    val kr = readKey(input, i, line, col)
                    tokens.addAll(kr.tokens)
                    i = kr.i; line = kr.line; col = kr.col

                    val ws = skipWhitespaceInline(input, i)
                    i = ws.i; col = bumpCol(col, ws.delta)

                    if (i < input.length && input[i] == '=') {
                        tokens.add(Symbol("=", TokenPos(line, col, i))); i++; col++
                        val ws2 = skipWhitespaceInline(input, i)
                        i = ws2.i; col = bumpCol(col, ws2.delta)

                        val vr = readValue(input, i, line, col)
                        tokens.addAll(vr.tokens)
                        i = vr.i; line = vr.line; col = vr.col
                    }
                }
            }
        }
        throw IllegalArgumentException("Unclosed inline table")
    }

    // ── Number parsing ────────────────────────────────────────────

    private fun parseTomlNumber(s: String): Any? {
        val cleaned = s.replace("_", "")
        if (cleaned.isEmpty()) return null
        return when {
            cleaned.startsWith("0x") || cleaned.startsWith("0X")                    -> parseRadix(cleaned.substring(2), 16)
            cleaned.startsWith("0o") || cleaned.startsWith("0O")                    -> parseRadix(cleaned.substring(2), 8)
            cleaned.startsWith("0b") || cleaned.startsWith("0B")                    -> parseRadix(cleaned.substring(2), 2)
            cleaned.contains('.') || cleaned.contains('e') || cleaned.contains('E') -> parseDecimalFloat(cleaned)
            else                                                                    -> parseDecimalInt(cleaned)
        }
    }

    private fun parseRadix(s: String, radix: Int): Any? = try {
        s.toLong(radix).let { if (it in Int.MIN_VALUE..Int.MAX_VALUE) it.toInt() else it }
    } catch (_: NumberFormatException) {
        null
    }

    private fun parseDecimalInt(s: String): Any? = try {
        val bi = BigInteger(s)
        when {
            bi >= BigInteger.valueOf(Int.MIN_VALUE.toLong()) && bi <= BigInteger.valueOf(Int.MAX_VALUE.toLong()) -> bi.toInt()
            bi >= BigInteger.valueOf(Long.MIN_VALUE) && bi <= BigInteger.valueOf(Long.MAX_VALUE)                 -> bi.toLong()
            else                                                                                                 -> bi
        }
    } catch (_: NumberFormatException) {
        null
    }

    private fun parseDecimalFloat(s: String): Any? = try {
        val bd = BigDecimal(s)
        if (bd.inRangeOfFloat && bd.fitsFloat()) bd.toFloat()
        else if (bd.inRangeOfDouble && bd.fitsDouble()) bd.toDouble()
        else bd
    } catch (_: NumberFormatException) {
        null
    }

    private fun parseSpecialFloat(s: String): Double {
        val lower = s.lowercase().replace("+", "")
        return when (lower) {
            "inf"  -> Double.POSITIVE_INFINITY
            "-inf" -> Double.NEGATIVE_INFINITY
            "nan"  -> Double.NaN
            "-nan" -> Double.NaN
            else   -> throw IllegalArgumentException("Invalid special float: $s")
        }
    }

    // ── Date/time parsing ─────────────────────────────────────────

    private val dateRegex = Regex(
        """^(\d{4})-(\d{2})-(\d{2})[T ](\d{2}):(\d{2}):(\d{2})(\.\d+)?(Z|[+-]\d{2}:\d{2})?$"""
    )
    private val localDateRegex = Regex("""^(\d{4})-(\d{2})-(\d{2})$""")
    private val localTimeRegex = Regex("""^(\d{2}):(\d{2}):(\d{2})(\.\d+)?$""")

    private fun tryParseDateTime(s: String): String? = when {
        dateRegex.matches(s)      -> s
        localDateRegex.matches(s) -> s
        localTimeRegex.matches(s) -> s
        else                      -> null
    }

    // ── Float fit helpers ─────────────────────────────────────────

    private val BigDecimal.inRangeOfFloat: Boolean
        get() = this >= BigDecimal.valueOf(-Float.MAX_VALUE.toDouble()) && this <= BigDecimal.valueOf(Float.MAX_VALUE.toDouble())

    private val BigDecimal.inRangeOfDouble: Boolean
        get() = this >= BigDecimal.valueOf(-Double.MAX_VALUE) && this <= BigDecimal.valueOf(Double.MAX_VALUE)

    private fun BigDecimal.fitsFloat(): Boolean = try {
        val f = this.toFloat()
        !f.isInfinite() && !f.isNaN() && this.compareTo(BigDecimal(f.toDouble())) == 0
    } catch (_: NumberFormatException) {
        false
    }

    private fun BigDecimal.fitsDouble(): Boolean = try {
        val d = this.toDouble()
        !d.isInfinite() && !d.isNaN() && this.compareTo(BigDecimal(d)) == 0
    } catch (_: NumberFormatException) {
        false
    }
}
