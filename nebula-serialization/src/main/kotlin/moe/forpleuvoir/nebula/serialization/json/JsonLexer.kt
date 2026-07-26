package moe.forpleuvoir.nebula.serialization.json

import moe.forpleuvoir.nebula.serialization.ast.*
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.EOF
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.Identifier
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.Literal
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.Symbol
import java.math.BigDecimal
import java.math.BigInteger

object JsonLexer : Lexer {

    override fun tokenize(input: String): Result<List<Token>> = runCatching {
        val tokens = mutableListOf<Token>()
        scan(input, 0, 1, 1, tokens)
        tokens
    }

    private tailrec fun scan(
        input: String,
        cursor: Int,
        line: Int,
        col: Int,
        acc: MutableList<Token>,
    ) {
        if (cursor >= input.length) {
            acc += EOF(TokenPos(line, col, cursor))
            return
        }

        val char = input[cursor]
        val currentPos = TokenPos(line, col, cursor)

        when (char) {
            '\n'                         -> scan(input, cursor + 1, line + 1, 1, acc)

            ' ', '\t', '\r'              -> scan(input, cursor + 1, line, col + 1, acc)

            '{', '}', '[', ']', ',', ':' -> {
                acc += Symbol(char.toString(), currentPos)
                scan(input, cursor + 1, line, col + 1, acc)
            }

            '"'                          -> {
                val (str, nextCursor) = readString(input, cursor, currentPos)
                val (newLine, newCol) = calculatePos(input, cursor, nextCursor, line, col)
                acc += Literal(Primitive.of(str), currentPos)
                scan(input, nextCursor, newLine, newCol, acc)
            }

            in '0'..'9', '-'             -> {
                val (numStr, nextCursor) = readNumber(input, cursor)
                val consumed = nextCursor - cursor
                acc += Literal(Primitive.of(parseNumber(numStr)), currentPos)
                scan(input, nextCursor, line, col + consumed, acc)
            }

            't', 'f', 'n'                -> {
                val (word, nextCursor) = readWord(input, cursor)
                val consumed = nextCursor - cursor
                acc += Identifier(word, currentPos)
                scan(input, nextCursor, line, col + consumed, acc)
            }

            else                         -> throw SyntaxReadException("Unexpected character: '$char'", currentPos)
        }
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

    private fun readString(input: String, start: Int, startPos: TokenPos): Pair<String, Int> {
        val sb = StringBuilder()
        var i = start + 1
        var escaped = false
        while (i < input.length) {
            val c = input[i]
            if (escaped) {
                when (c) {
                    '"', '\\', '/' -> sb.append(c)
                    'b'            -> sb.append('\b')
                    'f'            -> sb.append('\u000C')
                    'n'            -> sb.append('\n')
                    'r'            -> sb.append('\r')
                    't'            -> sb.append('\t')
                    'u'            -> {
                        if (i + 4 < input.length) {
                            val hex = input.substring(i + 1, i + 5)
                            sb.append(Integer.parseInt(hex, 16).toChar())
                            i += 4
                        } else {
                            throw SyntaxReadException(
                                "Invalid unicode escape sequence",
                                TokenPos(startPos.line, startPos.column + (i - start), i)
                            )
                        }
                    }

                    else           -> throw SyntaxReadException(
                        "Invalid escape sequence: \\$c",
                        TokenPos(startPos.line, startPos.column + (i - start), i)
                    )
                }
                escaped = false
            } else if (c == '\\') {
                escaped = true
            } else if (c == '"') {
                return sb.toString() to (i + 1)
            } else if (c == '\n' || c == '\r') {
                throw SyntaxReadException(
                    "Unclosed string literal (JSON strings cannot contain raw newlines)",
                    TokenPos(startPos.line, startPos.column + (i - start), i)
                )
            } else {
                sb.append(c)
            }
            i++
        }
        throw SyntaxReadException("Unclosed string literal", startPos)
    }

    private fun readNumber(input: String, start: Int): Pair<String, Int> {
        var i = start
        while (i < input.length && input[i] in "0123456789.+-eE") {
            i++
        }
        return input.substring(start, i) to i
    }

    fun parseNumber(numStr: String): Any {
        return if (numStr.contains('.') || numStr.contains('e') || numStr.contains('E')) {
            val bd = BigDecimal(numStr)
            if (bd.inRangeOfFloat && bd.fitsFloat()) {
                bd.toFloat()
            } else if (bd.inRangeOfDouble && bd.fitsDouble()) {
                bd.toDouble()
            } else {
                bd
            }
        } else {
            val bi = BigInteger(numStr)
            when {
                bi >= BigInteger.valueOf(Int.MIN_VALUE.toLong()) && bi <= BigInteger.valueOf(Int.MAX_VALUE.toLong()) -> bi.toInt()
                bi >= BigInteger.valueOf(Long.MIN_VALUE) && bi <= BigInteger.valueOf(Long.MAX_VALUE)                 -> bi.toLong()
                else                                                                                                 -> bi
            }
        }
    }

    private fun readWord(input: String, start: Int): Pair<String, Int> {
        var i = start
        while (i < input.length && input[i].isLetter()) {
            i++
        }
        return input.substring(start, i) to i
    }

    private val BigDecimal.inRangeOfFloat: Boolean
        get() = this >= BigDecimal.valueOf(-Float.MAX_VALUE.toDouble()) &&
                this <= BigDecimal.valueOf(Float.MAX_VALUE.toDouble())

    private val BigDecimal.inRangeOfDouble: Boolean
        get() = this >= BigDecimal.valueOf(-Double.MAX_VALUE) &&
                this <= BigDecimal.valueOf(Double.MAX_VALUE)

    private fun BigDecimal.fitsFloat(): Boolean {
        val f = this.toFloat()
        return try {
            !f.isInfinite() && !f.isNaN() && this.compareTo(BigDecimal(f.toDouble())) == 0
        } catch (_: NumberFormatException) {
            false
        }
    }

    private fun BigDecimal.fitsDouble(): Boolean {
        val d = this.toDouble()
        return try {
            !d.isInfinite() && !d.isNaN() && this.compareTo(BigDecimal(d)) == 0
        } catch (_: NumberFormatException) {
            false
        }
    }
}
