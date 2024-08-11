package moe.forpleuvoir.nebula.common.util

import kotlin.reflect.KClass
import kotlin.reflect.full.functions

inline fun <reified E : Enum<*>> Enum.Companion.valueOf(name: String): E? {
    return valueOf(E::class, name)
}

@Suppress("UNCHECKED_CAST")
fun <E : Enum<*>> Enum.Companion.valueOf(enumType: KClass<out E>, name: String): E? {
    return enumType.functions.find { it.name == "valueOf" && it.returnType.classifier == enumType }?.let {
        it.call(name) as E
    }
}