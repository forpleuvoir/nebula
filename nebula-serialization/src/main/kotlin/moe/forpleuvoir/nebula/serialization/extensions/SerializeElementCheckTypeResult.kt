package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.serialization.DeserializationException
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass

inline fun <reified T : SerializeElement, R : Any> SerializeElement.checkType(block: (T) -> R): Result<R> {
    return if (this::class == T::class) {
        Result.success(block(this as T))
    } else {
        Result.failure(DeserializationException.illegalType(this::class, T::class))
    }
}

inline fun <reified T : SerializeElement, R> SerializeElement.checkType(type: KClass<T>, block: (T) -> R): R {
    if (this::class == type) {
        return block(this as T)
    }
    throw DeserializationException.illegalType(this::class, type)
}

fun <R : Any> SerializeElement.checkType(block: SerializeElementCheckTypeResult<R>.() -> Unit = {}): SerializeElementCheckTypeResult<R> {
    return SerializeElementCheckTypeResult<R>(this).apply { block() }
}

class SerializeElementCheckTypeResult<R : Any> internal constructor(private val element: SerializeElement) {

    private var expectedTypes = mutableListOf<KClass<out SerializeElement>>()

    private lateinit var result: Any

    private val resultInitialized: Boolean
        get() = this::result.isInitialized

    private var exception: Throwable? = null

    inline fun <reified T : SerializeElement> check(noinline block: (T) -> R): SerializeElementCheckTypeResult<R> {
        return check(T::class, block)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : SerializeElement> check(type: KClass<T>, block: (T) -> R): SerializeElementCheckTypeResult<R> {
        expectedTypes += type
        if (element::class == type) {
            try {
                result = block(element as T)
            } catch (e: Throwable) {
                exception = e
            }
        }
        return this
    }

    fun throwOnFailure() {
        //如果已经初始化，则直无需抛出异常
        if (resultInitialized) return

        //如果已经捕获到异常，则直接抛出
        exception?.let { throw DeserializationException("Deserialize error,SerializeElement:\"$element\", message:\"${it.message}\"", it) }

        //如果没有任何期望类型，则抛出异常
        if (expectedTypes.isNotEmpty()) {
            throw DeserializationException.illegalType(element::class, *expectedTypes.toTypedArray())
        }

        throw DeserializationException("Result not initialized.", NullPointerException())
    }

    @Suppress("UNCHECKED_CAST")
    fun getOrThrow(): R {
        throwOnFailure()
        return result as R
    }

    @OptIn(ExperimentalContracts::class)
    inline fun getOrElse(onFailure: (exception: Throwable) -> R): R {
        contract {
            callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
        }
        return try {
            getOrThrow()
        } catch (e: Throwable) {
            onFailure(e)
        }
    }

    fun getOrDefault(defaultValue: R): R {
        return runCatching {
            getOrThrow()
        }.getOrDefault(defaultValue)
    }

}
