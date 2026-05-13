package moe.forpleuvoir.nebula.serialization.ast

import moe.forpleuvoir.nebula.serialization.base.SerializeElement

internal fun interface SyntaxDecoder {
    fun decode(tokens: List<Token>): Result<SerializeElement>
}