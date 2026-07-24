package moe.forpleuvoir.nebula.serialization.ast

fun interface Lexer {
    fun tokenize(input: String): Result<List<Token>>
}