package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeNull
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import java.math.BigDecimal
import java.math.BigInteger

fun Any?.toSerializeElement(): SerializeElement {
   return when (this) {
        null                ->  SerializeNull
        is SerializeElement ->  this
        is String           ->  SerializePrimitive(this)
        is Char             ->  SerializePrimitive(this)
        is Boolean          ->  SerializePrimitive(this)
        is BigInteger       ->  SerializePrimitive(this)
        is BigDecimal       ->  SerializePrimitive(this)
        is Number           ->  SerializePrimitive(this)
        is Map<*, *>        ->  this.toSerializeObject()
        is Array<*>         ->  serializeArray(this.iterator())
        is Collection<*>    ->  serializeArray(this)
        else                ->  this.toSerializeObject()
    }
}