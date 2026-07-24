package moe.forpleuvoir.nebula.serialization.json

import moe.forpleuvoir.nebula.serialization.ast.SyntaxDialect
import moe.forpleuvoir.nebula.serialization.base.SerializeElement

class JsonDialect(
    useIndent: Boolean = true,
    indentSize: Int = 2
) : SyntaxDialect {

    private val encoder = JsonEncoder(useIndent, indentSize)

    override fun decode(input: String) = JsonLexer.tokenize(input).fold(
            onSuccess = { JsonDecoder.decode(it) },
            onFailure = { Result.failure(it) }
        )

    override fun encode(element: SerializeElement) = encoder.encode(element)

    companion object : SyntaxDialect by JsonDialect() {

        val Compress = JsonDialect(false, 0)

    }
}