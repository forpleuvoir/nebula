package moe.forpleuvoir.nebula.serialization.json

import moe.forpleuvoir.nebula.serialization.ast.SyntaxDecoder
import moe.forpleuvoir.nebula.serialization.ast.SyntaxReadException
import moe.forpleuvoir.nebula.serialization.ast.Token
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.EOF
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.Identifier
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.Literal
import moe.forpleuvoir.nebula.serialization.ast.Token.Companion.Symbol
import moe.forpleuvoir.nebula.serialization.ast.TokenPos
import moe.forpleuvoir.nebula.serialization.base.*

internal object JsonDecoder : SyntaxDecoder {

    override fun decode(tokens: List<Token>): Result<SerializeElement> {
        return when {
            tokens.isEmpty() || tokens.first() is EOF ->
                Result.failure(IllegalArgumentException("Empty token stream"))

            else                                      -> parseElement(tokens).mapCatching { (element, remaining) ->
                when {
                    remaining.isEmpty() || remaining.first() is EOF -> element
                    else                                            -> throw SyntaxReadException(
                        "Unexpected token after JSON expression: ${remaining.first()}",
                        remaining.first().pos
                    )
                }
            }
        }
    }

    private fun parseElement(tokens: List<Token>): Result<Pair<SerializeElement, List<Token>>> {
        val head = tokens.firstOrNull() ?: return Result.failure(IllegalArgumentException("Unexpected end of input"))
        val tail = tokens.drop(1)

        return when (head) {
            is Symbol     -> when (head.value) {
                "{"  -> parseObject(tail)
                "["  -> parseArray(tail)
                else -> Result.failure(
                    SyntaxReadException("Unexpected symbol '${head.value}' at element position", head.pos)
                )
            }

            is Literal    -> {
                val element = head.value.asNull ?: head.value.asSerialize
                ?: return Result.failure(SyntaxReadException("Invalid literal value", head.pos))
                Result.success(element to tail)
            }

            is Identifier -> Result.success(convertIdentifier(head) to tail)

            else          -> Result.failure(
                SyntaxReadException("Unexpected token '$head' at element position", head.pos)
            )
        }
    }

    private fun convertIdentifier(token: Identifier): SerializeElement {
        return when (token.value) {
            "true"  -> SerializePrimitive(true)
            "false" -> SerializePrimitive(false)
            "null"  -> SerializeNull
            else    -> throw SyntaxReadException("Invalid identifier '${token.value}'", token.pos)
        }
    }

    private fun parseObject(tokens: List<Token>): Result<Pair<SerializeObject, List<Token>>> {
        val obj = SerializeObject()

        tailrec fun parseMembers(currentTokens: List<Token>): Result<Pair<SerializeObject, List<Token>>> {
            val head = currentTokens.firstOrNull()
            return when {
                head is Symbol && head.value == "}" -> Result.success(obj to currentTokens.drop(1))

                else                                -> {
                    if (head !is Literal) {
                        return Result.failure(
                            SyntaxReadException("Expected string key or '}', found ${head ?: "EOF"}", head?.pos ?: TokenPos(0, 0, 0))
                        )
                    }
                    val key = head.value.asString
                        ?: return Result.failure(SyntaxReadException("Expected string key", head.pos))
                    val afterKey = currentTokens.drop(1)

                    val colon = afterKey.firstOrNull()
                    if (colon !is Symbol || colon.value != ":") {
                        return Result.failure(
                            SyntaxReadException("Expected ':'", colon?.pos ?: TokenPos(0, 0, 0))
                        )
                    }

                    val (value, nextTail) = parseElement(afterKey.drop(1)).getOrElse { return Result.failure(it) }
                    obj[key] = value

                    val sep = nextTail.firstOrNull()
                    when {
                        sep is Symbol && sep.value == "," -> {
                            val afterComma = nextTail.drop(1)
                            val firstToken = afterComma.firstOrNull()
                            if (firstToken is Symbol && firstToken.value == "}") {
                                Result.failure(SyntaxReadException("Trailing comma is not allowed in JSON", sep.pos))
                            } else {
                                parseMembers(afterComma)
                            }
                        }

                        sep is Symbol && sep.value == "}" -> Result.success(obj to nextTail.drop(1))
                        sep != null                       -> Result.failure(
                            SyntaxReadException("Expected ',' or '}', found $sep", sep.pos)
                        )

                        else                              -> Result.failure(IllegalArgumentException("Unexpected EOF in object"))
                    }
                }
            }
        }

        return parseMembers(tokens)
    }

    private fun parseArray(tokens: List<Token>): Result<Pair<SerializeArray, List<Token>>> {
        val arr = SerializeArray()

        tailrec fun parseElements(currentTokens: List<Token>): Result<Pair<SerializeArray, List<Token>>> {
            val head = currentTokens.firstOrNull()
            return when {
                head is Symbol && head.value == "]" -> Result.success(arr to currentTokens.drop(1))

                else                                -> {
                    val (element, nextTail) = parseElement(currentTokens).getOrElse { return Result.failure(it) }
                    arr.add(element)

                    val sep = nextTail.firstOrNull()
                    when {
                        sep is Symbol && sep.value == "," -> {
                            val afterComma = nextTail.drop(1)
                            val firstToken = afterComma.firstOrNull()
                            if (firstToken is Symbol && firstToken.value == "]") {
                                Result.failure(SyntaxReadException("Trailing comma is not allowed in JSON array", sep.pos))
                            } else {
                                parseElements(afterComma)
                            }
                        }

                        sep is Symbol && sep.value == "]" -> Result.success(arr to nextTail.drop(1))
                        sep != null                       -> Result.failure(
                            SyntaxReadException("Expected ',' or ']', found $sep", sep.pos)
                        )

                        else                              -> Result.failure(IllegalArgumentException("Unexpected EOF in array"))
                    }
                }
            }
        }

        return parseElements(tokens)
    }
}
