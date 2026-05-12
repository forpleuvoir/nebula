@file:Suppress("UNUSED")
@file:OptIn(ExperimentalContracts::class)

package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.serialization.base.SerializeArray
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import kotlin.contracts.ExperimentalContracts



fun <T> SerializeObject.getOr(key: String, or: T, mapping: (SerializeElement) -> T): T {
    return runCatching { mapping(this[key]!!) }.getOrDefault(or)
}

fun SerializeObject.getOr(key: String, or: Number): Number {
    return runCatching { this[key]!!.asNumber!! }.getOrDefault(or)
}

fun SerializeObject.getOr(key: String, or: Boolean): Boolean {
    return runCatching { this[key]!!.asBoolean!! }.getOrDefault(or)
}

fun SerializeObject.getOr(key: String, or: String): String {
    return runCatching { this[key]!!.asString!! }.getOrDefault(or)
}

fun SerializeObject.getOr(key: String, or: Char): Char {
    return runCatching { this[key]!!.asString!![0] }.getOrDefault(or)
}

fun SerializeObject.getOr(key: String, or: SerializeObject): SerializeObject {
    return runCatching { this[key]!!.asObject!! }.getOrDefault(or)
}

fun SerializeObject.getOr(key: String, or: SerializeArray): SerializeArray {
    return runCatching { this[key]!!.asArray!! }.getOrDefault(or)
}

