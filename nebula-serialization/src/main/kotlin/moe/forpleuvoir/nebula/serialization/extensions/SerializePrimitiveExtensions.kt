package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.serialization.DeserializationException
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass

fun SerializePrimitive.toJavaPrimitive(): Any {
    return if (!isNumber) value else {
        if (value.toString().contains(Regex("[-+]?[0-9]*\\.[0-9]+"))) {
            asDouble
        } else if (value.toString().contains(Regex("^-?[0-9]*\$"))) {
            asInt
        } else {
            asString
        }
    }
}


fun <R : Any> SerializePrimitive.checkValue(block: SerializePrimitiveCheckTypeResult<R>.() -> Unit = {}): SerializePrimitiveCheckTypeResult<R> {
    return SerializePrimitiveCheckTypeResult<R>(this).apply { block() }
}

class SerializePrimitiveCheckTypeResult<R : Any> internal constructor(private val element: SerializePrimitive) {

    private var expectedTypes = mutableListOf<KClass<*>>()

    private lateinit var result: Any

    private val resultInitialized: Boolean
        get() = this::result.isInitialized

    private var exception: Throwable? = null

    inline fun <reified T : Any> check(noinline block: (T) -> R): SerializePrimitiveCheckTypeResult<R> {
        return check(T::class, block)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> check(type: KClass<T>, block: (T) -> R): SerializePrimitiveCheckTypeResult<R> {
        expectedTypes += type
        if (element.value::class == type) {
            try {
                result = block(element.value as T)
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
        exception?.let { throw DeserializationException("Deserialize error,SerializePrimitive:\"$element\", message:\"${it.message}\"", it) }

        //如果没有任何期望类型，则抛出异常
        if (expectedTypes.isNotEmpty()) {
            throw DeserializationException.illegalType(element.value::class, *expectedTypes.toTypedArray())
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