package moe.forpleuvoir.nebula.serialization.json

import moe.forpleuvoir.nebula.serialization.ast.SyntaxDialect
import moe.forpleuvoir.nebula.serialization.base.SerializeElement

class JsonDialect(
    useIndent: Boolean = true,
    indentSize: Int = 2
) : SyntaxDialect {

    private val encoder = JsonEncoder(useIndent, indentSize)

    override fun decode(input: String) = JsonDecoder.decode(JsonLexer.tokenize(input))

    override fun encode(element: SerializeElement) = encoder.encode(element)

    companion object : SyntaxDialect by JsonDialect() {

        val Compress = JsonDialect(false, 0)

    }
}