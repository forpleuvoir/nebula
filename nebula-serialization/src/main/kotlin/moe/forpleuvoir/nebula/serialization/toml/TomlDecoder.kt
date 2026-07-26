package moe.forpleuvoir.nebula.serialization.toml

import moe.forpleuvoir.nebula.serialization.ast.SyntaxDecoder
import moe.forpleuvoir.nebula.serialization.ast.SyntaxReadException
import moe.forpleuvoir.nebula.serialization.ast.Token
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.ArrayOfTablesHeader
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.EOF
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.Identifier
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.Literal
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.Symbol
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.TableHeader
import moe.forpleuvoir.nebula.serialization.ast.TokenPos
import moe.forpleuvoir.nebula.serialization.base.*

object TomlDecoder : SyntaxDecoder {

    override fun decode(tokens: List<Token>): Result<SerializeElement> = try {
        val root = SerializeObject()
        var currentTable = root
        var i = 0

        while (i < tokens.size) {
            when (val token = tokens[i]) {
                is EOF                 -> break
                is TableHeader         -> {
                    currentTable = navigateTo(root, token.path)
                    i++
                }

                is ArrayOfTablesHeader -> {
                    currentTable = getOrCreateArrayTableElement(root, token.path)
                    i++
                }

                else                   -> {
                    val keyResult = consumeKey(tokens, i)
                    i = keyResult.second

                    checkToken<Symbol>(tokens, i, "=")
                    i++

                    val (value, afterValue) = consumeValue(tokens, i)
                    i = afterValue

                    setNestedValue(currentTable, keyResult.first, value)
                }
            }
        }

        Result.success(if (root.isEmpty()) SerializeNull else root)
    } catch (e: SyntaxReadException) {
        Result.failure(e)
    } catch (e: IllegalArgumentException) {
        Result.failure(e)
    }

    // ── Key consumption ───────────────────────────────────────────

    private fun consumeKey(tokens: List<Token>, start: Int): Pair<List<String>, Int> {
        val parts = mutableListOf<String>()
        var i = start
        var expectKey = true

        while (i < tokens.size) {
            val token = tokens[i]
            when {
                expectKey                             -> when (token) {
                    is Identifier -> {
                        parts.add(token.value); i++; expectKey = false
                    }

                    is Literal    -> {
                        val s = token.value.value?.toString()
                            ?: throw SyntaxReadException("Invalid key literal", token.pos)
                        parts.add(s); i++; expectKey = false
                    }

                    else          -> throw SyntaxReadException(
                        "Expected key, found $token", token.pos
                    )
                }

                token is Symbol && token.value == "." -> {
                    i++; expectKey = true
                }

                token is Symbol && token.value == "=" -> return parts to i
                else                                  -> throw SyntaxReadException(
                    "Expected '.' or '=', found $token", token.pos
                )
            }
        }

        throw SyntaxReadException("Unexpected EOF in key", TokenPos(0, 0, 0))
    }

    // ── Value consumption ─────────────────────────────────────────

    private fun consumeValue(tokens: List<Token>, start: Int): Pair<SerializeElement, Int> {
        val token = tokens.getOrNull(start)
            ?: throw SyntaxReadException("Expected value, found EOF", TokenPos(0, 0, 0))

        return when (token) {
            is Literal    -> {
                val element = token.value.asNull ?: token.value.asSerialize
                ?: throw SyntaxReadException("Invalid literal value", token.pos)
                element to (start + 1)
            }

            is Symbol     -> when (token.value) {
                "["  -> parseArray(tokens, start)
                "{"  -> parseInlineTable(tokens, start)
                else -> throw SyntaxReadException("Unexpected symbol '${token.value}'", token.pos)
            }

            is Identifier -> SerializePrimitive(token.value) to (start + 1)
            else          -> throw SyntaxReadException("Unexpected token '$token'", token.pos)
        }
    }

    private fun parseArray(tokens: List<Token>, start: Int): Pair<SerializeArray, Int> {
        val arr = SerializeArray()
        var i = start + 1

        while (i < tokens.size) {
            val token = tokens[i]
            when {
                token is Symbol && token.value == "]" -> return arr to (i + 1)
                token is Symbol && token.value == "," -> i++
                else                                  -> {
                    val (value, afterValue) = consumeValue(tokens, i)
                    arr.add(value)
                    i = afterValue
                }
            }
        }

        throw SyntaxReadException("Unclosed array", TokenPos(0, 0, 0))
    }

    private fun parseInlineTable(tokens: List<Token>, start: Int): Pair<SerializeObject, Int> {
        val obj = SerializeObject()
        var i = start + 1

        while (i < tokens.size) {
            val token = tokens[i]
            when {
                token is Symbol && token.value == "}" -> return obj to (i + 1)
                token is Symbol && token.value == "," -> i++
                token is EOF                          -> throw SyntaxReadException(
                    "Unclosed inline table", token.pos
                )

                else                                  -> {
                    val keyResult = consumeKey(tokens, i)
                    i = keyResult.second

                    checkToken<Symbol>(tokens, i, "=")
                    i++

                    val (value, afterValue) = consumeValue(tokens, i)
                    i = afterValue

                    setNestedValue(obj, keyResult.first, value)
                }
            }
        }

        throw SyntaxReadException("Unclosed inline table", TokenPos(0, 0, 0))
    }

    // ── Tree navigation ───────────────────────────────────────────

    private fun navigateTo(root: SerializeObject, path: List<String>): SerializeObject {
        var current = root
        for (part in path) {
            val existing = current[part]
            current = when {
                existing is SerializeObject -> existing
                existing == null            -> SerializeObject().also { current[part] = it }
                else                        -> throw IllegalArgumentException(
                    "Cannot create table '$part': existing value is not a table"
                )
            }
        }
        return current
    }

    private fun getOrCreateArrayTableElement(root: SerializeObject, path: List<String>): SerializeObject {
        var current = root
        for (i in 0 until path.size - 1) {
            val part = path[i]
            val existing = current[part]
            current = when {
                existing is SerializeObject -> existing
                existing == null            -> SerializeObject().also { current[part] = it }
                else                        -> throw IllegalArgumentException(
                    "Cannot create table '$part': existing value is not a table"
                )
            }
        }

        val lastPart = path.last()
        val arr = when (val existing = current[lastPart]) {
            is SerializeArray -> existing
            null              -> SerializeArray().also { current[lastPart] = it }
            else              -> throw IllegalArgumentException(
                "Cannot create array table '$lastPart': existing value is not an array"
            )
        }

        val newObj = SerializeObject()
        arr.add(newObj)
        return newObj
    }

    private fun setNestedValue(obj: SerializeObject, keyParts: List<String>, value: SerializeElement) {
        if (keyParts.isEmpty()) throw IllegalArgumentException("Empty key path")
        var current = obj
        for (i in 0 until keyParts.size - 1) {
            val part = keyParts[i]
            val existing = current[part]
            current = when {
                existing is SerializeObject -> existing
                existing == null            -> SerializeObject().also { current[part] = it }
                else                        -> throw IllegalArgumentException(
                    "Cannot create nested key '$part': existing value is not a table"
                )
            }
        }
        current[keyParts.last()] = value
    }

    // ── Helpers ───────────────────────────────────────────────────

    private inline fun <reified T : Token> checkToken(tokens: List<Token>, index: Int, expectedValue: String? = null) {
        val token = tokens.getOrNull(index)
            ?: throw SyntaxReadException("Unexpected EOF, expected token", TokenPos(0, 0, 0))
        if (token !is T) {
            throw SyntaxReadException("Expected $expectedValue, found $token", token.pos)
        }
        if (expectedValue != null && token is Symbol && token.value != expectedValue) {
            throw SyntaxReadException("Expected '$expectedValue', found '${token.value}'", token.pos)
        }
    }
}
