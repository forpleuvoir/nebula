package moe.forpleuvoir.nebula.serialization.annotation

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

@Target(AnnotationTarget.PROPERTY,AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class SerializerName(val name: String) {
    companion object {

        fun KParameter.getSerializerName(): String {
            return this.findSerializerName { it.name } ?: this.name!!
        }

        @OptIn(ExperimentalContracts::class)
        inline fun <R> KParameter.findSerializerName(block: (SerializerName) -> R): R? {
            contract {
                callsInPlace(block, InvocationKind.AT_MOST_ONCE)
            }
            return this.findAnnotation<SerializerName>()?.let {
                block(it)
            }
        }

    }
}