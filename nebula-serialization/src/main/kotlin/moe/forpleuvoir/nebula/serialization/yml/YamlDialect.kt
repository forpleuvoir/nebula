package moe.forpleuvoir.nebula.serialization.yml

import moe.forpleuvoir.nebula.serialization.ast.SyntaxDialect
import moe.forpleuvoir.nebula.serialization.base.SerializeElement

class YamlDialect : SyntaxDialect {

    override fun decode(input: String): Result<SerializeElement> =
        YamlDecoder.decode(input)

    override fun encode(element: SerializeElement): String =
        YamlEncoder.encode(element)

    companion object : SyntaxDialect by YamlDialect()
}
