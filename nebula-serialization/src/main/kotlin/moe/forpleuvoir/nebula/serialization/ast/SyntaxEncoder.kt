package moe.forpleuvoir.nebula.serialization.ast

import moe.forpleuvoir.nebula.serialization.base.SerializeElement

internal fun interface SyntaxEncoder {
    fun encode(element: SerializeElement): String
}