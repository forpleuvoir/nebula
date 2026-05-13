package moe.forpleuvoir.nebula.serialization.ast

import moe.forpleuvoir.nebula.serialization.base.SerializeElement

interface SyntaxDialect {
    fun decode(input: String): Result<SerializeElement>
    fun encode(element: SerializeElement): String
}