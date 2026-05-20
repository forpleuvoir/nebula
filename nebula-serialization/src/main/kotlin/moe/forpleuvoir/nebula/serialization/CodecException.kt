package moe.forpleuvoir.nebula.serialization

import kotlin.reflect.KClass

class SerializationException : RuntimeException {

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(cause: Throwable) : super(cause)

    constructor(message: String, cause: Throwable) : super(message, cause)

    companion object{
        @Suppress("NOTHING_TO_INLINE")
        inline fun wrap(e: Throwable): SerializationException = e as? SerializationException ?: SerializationException(e)
    }
}

class DeserializationException : RuntimeException {

    companion object {

        @Suppress("NOTHING_TO_INLINE")
        inline fun wrap(e: Throwable): DeserializationException = e as? DeserializationException ?: DeserializationException(e)

        fun illegalType(element: KClass<*>, vararg expectedType: KClass<*>): DeserializationException {
            return DeserializationException("Deserialize type error, expected to be an ${expectedType.map { it.simpleName }}, but was [${element.simpleName}]")
        }

        fun <R> runCatching(block: () -> R): Result<R> =
            try {
                Result.success(block())
            } catch (e: Throwable) {
                Result.failure(wrap(e))
            }

    }

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(cause: Throwable) : super(cause)

    constructor(message: String, cause: Throwable) : super(message, cause)

}