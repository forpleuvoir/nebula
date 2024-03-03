package moe.forpleuvoir.nebula.serialization

import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import kotlin.reflect.KClass

class SerializationException : RuntimeException {

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(cause: Throwable) : super(cause)

    constructor(message: String, cause: Throwable) : super(message, cause)

}

class DeserializationException : RuntimeException {

    companion object {

        fun checkType(element: SerializeElement, vararg expectedType: KClass<out SerializeElement>) {
            if (element::class !in expectedType) {
                illegalType(element::class, *expectedType)
            }
        }

        @Suppress("MemberVisibilityCanBePrivate")
        fun illegalType(element: KClass<*>, vararg expectedType: KClass<*>): DeserializationException {
            return DeserializationException("Deserialize type error, expected to be an ${expectedType.map { it.simpleName }}, but was [${element.simpleName}]")
        }

        fun unknownExpression(clazz: Class<*>, element: SerializeElement, throwable: Throwable): DeserializationException {
            return DeserializationException("Deserialize as [${clazz.simpleName}] error, serializeElement:[$element] message : ${throwable.message}", throwable)
        }

    }

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(cause: Throwable) : super(cause)

    constructor(message: String, cause: Throwable) : super(message, cause)

}