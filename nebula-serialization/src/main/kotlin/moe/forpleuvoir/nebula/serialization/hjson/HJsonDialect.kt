package moe.forpleuvoir.nebula.serialization.hjson

import moe.forpleuvoir.nebula.serialization.ast.SyntaxDialect
import moe.forpleuvoir.nebula.serialization.base.SerializeElement

class HJsonDialect : SyntaxDialect {

    override fun decode(input: String): Result<SerializeElement> =
        HJsonLexer.tokenize(input).fold(
            onSuccess = { HJsonDecoder.decode(it) },
            onFailure = { Result.failure(it) }
        )

    override fun encode(element: SerializeElement): String = HJsonEncoder.encode(element)

    companion object : SyntaxDialect by HJsonDialect()
}
