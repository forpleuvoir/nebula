package moe.forpleuvoir.nebula.serialization.annotation

import moe.forpleuvoir.nebula.serialization.Deserializer
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Deserializable(val value: KClass<out Deserializer<*>>) {
    companion object {

        @Suppress("UNCHECKED_CAST")
        fun <T> Deserializable.getDeserializer(): Deserializer<T> {
            return runCatching {
                this.value.createInstance() as Deserializer<T>
            }.getOrElse {
                this.value.objectInstance as Deserializer<T>
            }
        }

        @OptIn(ExperimentalContracts::class)
        inline fun <R> KClass<*>.findDeserializable(block: (Deserializable) -> R) {
            contract {
                callsInPlace(block, InvocationKind.AT_MOST_ONCE)
            }
            this.findAnnotation<Deserializable>()?.let {
                block(it)
            }
        }

    }

}