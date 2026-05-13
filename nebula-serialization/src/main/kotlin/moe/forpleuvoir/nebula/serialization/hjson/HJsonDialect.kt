package moe.forpleuvoir.nebula.serialization.hjson

import moe.forpleuvoir.nebula.serialization.ast.SyntaxDialect
import moe.forpleuvoir.nebula.serialization.base.SerializeElement

class HJsonDialect : SyntaxDialect {

    override fun decode(input: String): Result<SerializeElement> =
        HJsonDecoder.decode(HJsonLexer.tokenize(input))

    override fun encode(element: SerializeElement): String = HJsonEncoder.encode(element)

    companion object : SyntaxDialect by HJsonDialect()
}
