package moe.forpleuvoir.nebula.serialization.toml

import moe.forpleuvoir.nebula.serialization.ast.SyntaxDialect
import moe.forpleuvoir.nebula.serialization.base.SerializeElement

class TomlDialect : SyntaxDialect {

    override fun decode(input: String): Result<SerializeElement> =
        TomlLexer.tokenize(input).fold(
            onSuccess = { TomlDecoder.decode(it) },
            onFailure = { Result.failure(it) }
        )

    override fun encode(element: SerializeElement): String =
        TomlEncoder.encode(element)

    companion object : SyntaxDialect by TomlDialect()
}
