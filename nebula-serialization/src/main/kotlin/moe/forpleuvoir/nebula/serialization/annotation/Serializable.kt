package moe.forpleuvoir.nebula.serialization.annotation

import moe.forpleuvoir.nebula.serialization.Serializer
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Serializable(val value: KClass<out Serializer<*>>) {
    companion object {

        @Suppress("UNCHECKED_CAST")
        fun <T> Serializable.getSerializer(): Serializer<T> {
            return runCatching {
                this.value.createInstance() as Serializer<T>
            }.getOrElse {
                this.value.objectInstance as Serializer<T>
            }
        }

        @OptIn(ExperimentalContracts::class)
        inline fun <R> KClass<*>.findSerializable(block: (Serializable) -> R) {
            contract {
                callsInPlace(block, InvocationKind.AT_MOST_ONCE)
            }
            this.findAnnotation<Serializable>()?.let {
                block(it)
            }
        }

    }

}