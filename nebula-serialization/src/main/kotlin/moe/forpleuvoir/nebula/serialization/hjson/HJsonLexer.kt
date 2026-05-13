package moe.forpleuvoir.nebula.serialization.hjson

import moe.forpleuvoir.nebula.serialization.ast.Lexer
import moe.forpleuvoir.nebula.serialization.ast.Primitive
import moe.forpleuvoir.nebula.serialization.ast.Token
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.EOF
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.Identifier
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.Literal
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.Symbol
import moe.forpleuvoir.nebula.serialization.ast.TokenPos
import moe.forpleuvoir.nebula.serialization.json.JsonLexer

internal object HJsonLexer : Lexer {

    override fun tokenize(input: String): List<Token> {
        val tokens = mutableListOf<Token>()
        scan(input, 0, 1, 1, tokens)
        return tokens
    }

    private tailrec fun scan(input: String, cursor: Int, line: Int, col: Int, acc: MutableList<Token>) {
        if (cursor >= input.length) {
            acc += EOF(TokenPos(line, col, cursor))
            return
        }

        val char = input[cursor]
        val currentPos = TokenPos(line, col, cursor)

        when (char) {
            '\n'                         -> scan(input, cursor + 1, line + 1, 1, acc)

            ' ', '\t', '\r'              -> scan(input, cursor + 1, line, col + 1, acc)

            '#'                          -> {
                val nextCursor = skipLine(input, cursor)
                scan(input, nextCursor, line + 1, 1, acc)
            }

            '/'                          -> when {
                isFollowedBy(input, cursor, '/') -> {
                    val nextCursor = skipLine(input, cursor)
                    scan(input, nextCursor, line + 1, 1, acc)
                }

                isFollowedBy(input, cursor, '*') -> {
                    val nextCursor = skipBlockComment(input, cursor + 2)
                    val (newLine, newCol) = calculatePos(input, cursor, nextCursor, line, col)
                    scan(input, nextCursor, newLine, newCol, acc)
                }

                else                             -> {
                    scan(input, cursor + 1, line, col + 1, acc)
                }
            }

            '{', '}', '[', ']', ',', ':' -> {
                acc += Symbol(char.toString(), currentPos)
                scan(input, cursor + 1, line, col + 1, acc)
            }

            '\''                         -> if (isTripleQuote(input, cursor)) {
                val (str, nextCursor) = readMultilineString(input, cursor + 3)
                val (newLine, newCol) = calculatePos(input, cursor, nextCursor, line, col)
                acc += Literal(Primitive.of(str), currentPos)
                scan(input, nextCursor, newLine, newCol, acc)
            } else {
                val (str, nextCursor) = readQuotedString(input, cursor)
                val (newLine, newCol) = calculatePos(input, cursor, nextCursor, line, col)
                acc += Literal(Primitive.of(str), currentPos)
                scan(input, nextCursor, newLine, newCol, acc)
            }

            '"'                          -> {
                val (str, nextCursor) = readQuotedString(input, cursor)
                val (newLine, newCol) = calculatePos(input, cursor, nextCursor, line, col)
                acc += Literal(Primitive.of(str), currentPos)
                scan(input, nextCursor, newLine, newCol, acc)
            }

            in '0'..'9', '-'             -> {
                val (content, nextCursor) = readUnquoted(input, cursor)
                val consumed = nextCursor - cursor
                if (content.isEmpty()) {
                    scan(input, nextCursor, line, col + consumed, acc)
                } else {
                    val token = parseLiteralOrIdentifier(content, currentPos)
                    acc += token
                    scan(input, nextCursor, line, col + consumed, acc)
                }
            }

            else                         -> if (isUnquotedCharStart(char)) {
                val (content, nextCursor) = readUnquoted(input, cursor)
                val consumed = nextCursor - cursor
                if (content.isEmpty()) {
                    scan(input, nextCursor, line, col + consumed, acc)
                } else {
                    val token = parseLiteralOrIdentifier(content, currentPos)
                    acc += token
                    scan(input, nextCursor, line, col + consumed, acc)
                }
            } else {
                scan(input, cursor + 1, line, col + 1, acc)
            }
        }
    }

    private fun isFollowedBy(input: String, cursor: Int, target: Char): Boolean =
        cursor + 1 < input.length && input[cursor + 1] == target

    private fun isTripleQuote(input: String, cursor: Int): Boolean =
        cursor + 2 < input.length && input[cursor] == '\'' && input[cursor + 1] == '\'' && input[cursor + 2] == '\''

    private fun skipLine(input: String, cursor: Int): Int {
        val nextLine = input.indexOf('\n', cursor)
        return if (nextLine == -1) input.length else nextLine + 1
    }

    private fun skipBlockComment(input: String, cursor: Int): Int {
        val end = input.indexOf("*/", cursor)
        return if (end == -1) input.length else end + 2
    }

    private fun calculatePos(input: String, start: Int, end: Int, currLine: Int, currCol: Int): Pair<Int, Int> {
        var line = currLine
        var col = currCol
        var i = start
        while (i < end) {
            if (input[i] == '\n') {
                line++
                col = 1
            } else {
                col++
            }
            i++
        }
        return line to col
    }

    private fun readQuotedString(input: String, start: Int): Pair<String, Int> {
        val quote = input[start]
        val sb = StringBuilder()
        var i = start + 1
        var escaped = false
        while (i < input.length) {
            val c = input[i]
            if (escaped) {
                when (c) {
                    '"', '\'', '\\', '/' -> sb.append(c)
                    'b'                  -> sb.append('\b')
                    'f'                  -> sb.append('\u000C')
                    'n'                  -> sb.append('\n')
                    'r'                  -> sb.append('\r')
                    't'                  -> sb.append('\t')
                    'u'                  -> {
                        if (i + 4 < input.length) {
                            val hex = input.substring(i + 1, i + 5)
                            sb.append(Integer.parseInt(hex, 16).toChar())
                            i += 4
                        } else throw IllegalArgumentException("Invalid unicode escape at $i")
                    }

                    else                 -> sb.append(c)
                }
                escaped = false
            } else if (c == '\\') {
                escaped = true
            } else if (c == quote) {
                return sb.toString() to (i + 1)
            } else {
                sb.append(c)
            }
            i++
        }
        throw IllegalArgumentException("Unclosed string at $start")
    }

    private fun readMultilineString(input: String, start: Int): Pair<String, Int> {
        val end = input.indexOf("'''", start)
        val raw = input.substring(start, end)

        val lines = raw.replace("\r\n", "\n").split('\n')
        val contentLines = if (lines.isNotEmpty() && lines.first().trim().isEmpty()) lines.drop(1) else lines

        val nonEmpty = contentLines.filter { line -> line.trim().isNotEmpty() }
        val baseIndent = nonEmpty.minOfOrNull { line -> line.takeWhile { c -> c == ' ' }.length } ?: 0

        val finalContent = contentLines.joinToString("\n") { line ->
            if (line.length >= baseIndent) line.substring(baseIndent) else line.trimStart()
        }.trimEnd()

        return finalContent to (end + 3)
    }

    private fun readUnquoted(input: String, start: Int): Pair<String, Int> {
        var i = start
        while (i < input.length) {
            val c = input[i]
            when {
                c == '\n' || c == '\r' || c == ',' || c == '}' || c == ']'                             -> break
                c == ':'                                                                               -> {
                    if (i + 1 >= input.length || input[i + 1] in " \t\n\r") break
                    else i++
                }

                c == '#' || (c == '/' && (isFollowedBy(input, i, '/') || isFollowedBy(input, i, '*'))) -> break
                else                                                                                   -> i++
            }
        }
        return input.substring(start, i).trim() to i
    }

    private fun isUnquotedCharStart(c: Char): Boolean =
        !c.isISOControl() && c !in " \t\n\r{}[]:,#\"\'"

    private fun parseLiteralOrIdentifier(content: String, pos: TokenPos): Token {
        return when {
            content == "true"                                          -> Literal(Primitive.of(true), pos)
            content == "false"                                         -> Literal(Primitive.of(false), pos)
            content == "null"                                          -> Literal(Primitive.of(null), pos)
            content.matches(Regex("-?\\d+(\\.\\d+)?([eE][+-]?\\d+)?")) -> {
                try {
                    Literal(Primitive.of(JsonLexer.parseNumber(content)), pos)
                } catch (_: Exception) {
                    Identifier(content, pos)
                }
            }

            else                                                       -> Identifier(content, pos)
        }
    }
}
