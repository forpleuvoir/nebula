package moe.forpleuvoir.nebula.serialization.hjson

import moe.forpleuvoir.nebula.serialization.ast.SyntaxDecoder
import moe.forpleuvoir.nebula.serialization.ast.SyntaxReadException
import moe.forpleuvoir.nebula.serialization.ast.Token
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.EOF
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.Identifier
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.Literal
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.Symbol
import moe.forpleuvoir.nebula.serialization.ast.TokenPos
import moe.forpleuvoir.nebula.serialization.base.*

internal object HJsonDecoder : SyntaxDecoder {

    override fun decode(tokens: List<Token>): Result<SerializeElement> {
        return when {
            tokens.isEmpty() || tokens.first() is EOF                           -> Result.success(SerializeNull)

            tokens.first() is Symbol && (tokens.first() as Symbol).value == "{" ->
                parseObject(tokens.drop(1)).map { it.first }

            tokens.first() is Symbol && (tokens.first() as Symbol).value == "[" ->
                parseArray(tokens.drop(1)).map { it.first }

            else                                                                -> {
                if (isRootObject(tokens)) {
                    parseObject(tokens).map { it.first }
                } else {
                    parseElement(tokens).map { it.first }
                }
            }
        }
    }

    private fun isRootObject(tokens: List<Token>): Boolean {
        return tokens.takeWhile { token ->
            when (token) {
                is Symbol -> token.value != "}" && token.value != "]"
                is EOF    -> false
                else      -> true
            }
        }.any { it is Symbol && it.value == ":" }
    }

    private fun parseElement(tokens: List<Token>): Result<Pair<SerializeElement, List<Token>>> {
        val head = tokens.firstOrNull() ?: return Result.failure(IllegalArgumentException("Token stream is empty"))
        val tail = tokens.drop(1)

        return when (head) {
            is Symbol     -> when (head.value) {
                "{"  -> parseObject(tail)
                "["  -> parseArray(tail)
                else -> Result.failure(
                    SyntaxReadException("Unexpected token '$head' at element position", head.pos)
                )
            }

            is Literal    -> {
                val element = head.value.asNull ?: head.value.asSerialize
                ?: return Result.failure(SyntaxReadException("Invalid literal value", head.pos))
                Result.success(element to tail)
            }

            is Identifier -> {
                Result.success(SerializePrimitive(head.value) to tail)
            }

            is EOF        -> Result.failure(
                SyntaxReadException("Unexpected end of input: element expected", head.pos)
            )

            else          -> Result.failure(
                SyntaxReadException("Unexpected token '$head' at element position", head.pos)
            )
        }
    }

    private fun parseObject(tokens: List<Token>): Result<Pair<SerializeObject, List<Token>>> {
        val obj = SerializeObject()

        tailrec fun parseMembers(currentTokens: List<Token>): Result<Pair<SerializeObject, List<Token>>> {
            return when (val head = currentTokens.firstOrNull()) {
                null, is EOF                   -> Result.success(obj to currentTokens)
                is Symbol if head.value == "}" -> Result.success(obj to currentTokens.drop(1))
                is Symbol if head.value == "," -> parseMembers(currentTokens.drop(1))
                else                           -> {
                    if (head is Identifier || head is Literal) {
                        val keyStr = when (head) {
                            is Identifier -> head.value
                            is Literal    -> head.value.value?.toString() ?: ""
                        }
                        val afterKey = currentTokens.drop(1)
                        val colon = afterKey.firstOrNull()
                        if (colon !is Symbol || colon.value != ":") {
                            val pos = colon?.pos ?: TokenPos(0, 0, 0)
                            return Result.failure(
                                SyntaxReadException("Expected key or '}', but found: ${colon ?: "EOF"}", pos)
                            )
                        }

                        val (value, nextTail) = parseElement(afterKey.drop(1)).getOrElse { return Result.failure(it) }
                        obj.put(keyStr, value)

                        val sep = nextTail.firstOrNull()
                        if (sep is Symbol && sep.value == ",") {
                            parseMembers(nextTail.drop(1))
                        } else {
                            parseMembers(nextTail)
                        }
                    } else {
                        val pos = head.pos
                        Result.failure(
                            SyntaxReadException("Expected key or '}', but found: $head", pos)
                        )
                    }
                }
            }
        }

        return parseMembers(tokens)
    }

    private fun parseArray(tokens: List<Token>): Result<Pair<SerializeArray, List<Token>>> {
        val arr = SerializeArray()

        tailrec fun parseElements(currentTokens: List<Token>): Result<Pair<SerializeArray, List<Token>>> {
            return when (val head = currentTokens.firstOrNull()) {
                null, is EOF                   -> Result.success(arr to currentTokens)
                is Symbol if head.value == "]" -> Result.success(arr to currentTokens.drop(1))
                is Symbol if head.value == "," -> parseElements(currentTokens.drop(1))
                else                           -> {
                    val (value, nextTail) = parseElement(currentTokens).getOrElse { return Result.failure(it) }
                    arr.add(value)

                    val sep = nextTail.firstOrNull()
                    if (sep is Symbol && sep.value == ",") {
                        parseElements(nextTail.drop(1))
                    } else {
                        parseElements(nextTail)
                    }
                }
            }
        }

        return parseElements(tokens)
    }
}
