package moe.forpleuvoir.nebula.serialization.ast

import moe.forpleuvoir.nebula.serialization.base.SerializeNull
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive

internal data class TokenPos(val line: Int, val column: Int, val offset: Int)

internal sealed interface Token {
    val pos: TokenPos

    companion object {
        data class Symbol(val value: String, override val pos: TokenPos) : Token

        data class Literal(val value: Primitive, override val pos: TokenPos) : Token

        data class Identifier(val value: String, override val pos: TokenPos) : Token

        data class EOF(override val pos: TokenPos) : Token

        interface Special : Token

    }
}

@JvmInline
internal value class Primitive private constructor(val value: Any?) {

    companion object {
        fun of(value: Any?): Primitive = Primitive(value)
    }

    val isString get() = value is String

    val asString get() = if (isString) value as String else null

    val asSerialize
        get() = when (value!!) {
            is Number  -> SerializePrimitive(value)
            is Char    -> SerializePrimitive(value)
            is Boolean -> SerializePrimitive(value)
            is String  -> SerializePrimitive(value)
            else       -> null
        }

    val asNull get() = if (value == null) SerializeNull else null

}
