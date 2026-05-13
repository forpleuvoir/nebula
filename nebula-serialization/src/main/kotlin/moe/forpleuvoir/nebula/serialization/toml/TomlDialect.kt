package moe.forpleuvoir.nebula.serialization.toml

import moe.forpleuvoir.nebula.serialization.ast.SyntaxDialect
import moe.forpleuvoir.nebula.serialization.base.SerializeElement

class TomlDialect : SyntaxDialect {

    override fun decode(input: String): Result<SerializeElement> =
        TomlDecoder.decode(TomlLexer.tokenize(input))

    override fun encode(element: SerializeElement): String =
        TomlEncoder.encode(element)

    companion object : SyntaxDialect by TomlDialect()
}
