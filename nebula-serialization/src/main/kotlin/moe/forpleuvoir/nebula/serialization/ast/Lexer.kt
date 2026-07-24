package moe.forpleuvoir.nebula.serialization.ast

internal fun interface Lexer {
    fun tokenize(input: String): Result<List<Token>>
}