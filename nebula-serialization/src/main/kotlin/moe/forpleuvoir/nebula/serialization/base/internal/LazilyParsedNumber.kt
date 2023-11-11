package moe.forpleuvoir.nebula.serialization.base.internal

import java.math.BigDecimal

internal class LazilyParsedNumber(private val value: String) : Number() {

    fun type(): Class<*> {
        return value::class.java
    }

    override fun toByte(): Byte {
        return value.toByte()
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun toChar(): Char {
        return value.toInt().toChar()
    }

    override fun toDouble(): Double {
        return value.toDouble()
    }

    override fun toFloat(): Float {
        return value.toFloat()
    }

    override fun toInt(): Int {
        return try {
            value.toInt()
        } catch (e: NumberFormatException) {
            try {
                value.toLong().toInt()
            } catch (e: NumberFormatException) {
                BigDecimal(value).toInt()
            }
        }
    }

    override fun toLong(): Long {
        return try {
            value.toLong()
        } catch (e: NumberFormatException) {
            BigDecimal(value).toLong()
        }
    }

    override fun toShort(): Short {
        return value.toShort()
    }

    override fun toString(): String {
        return value
    }
}
