package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.serialization.Serializable
import moe.forpleuvoir.nebula.serialization.annotation.Serializable.Companion.findSerializable
import moe.forpleuvoir.nebula.serialization.annotation.Serializable.Companion.getSerializer
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeNull
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import java.lang.reflect.Modifier
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility.INTERNAL
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

@Suppress("DuplicatedCode")
fun Any?.toSerializeElement(): SerializeElement {
    return when (this) {
        null                        -> SerializeNull
        is SerializeElement         -> this
        is String                   -> SerializePrimitive(this)
        is Char                     -> SerializePrimitive(this)
        is Boolean                  -> SerializePrimitive(this)
        is BigInteger               -> SerializePrimitive(this)
        is BigDecimal               -> SerializePrimitive(this)
        is Number                   -> SerializePrimitive(this)
        is Map<*, *>                -> this.toSerializeObject()
        is Array<*>, is Iterable<*> -> {//如果是一个数组或可迭代对象
            val iterable = if (this is Array<*>) this.asIterable() else (this as Iterable<*>).asIterable()
            with(iterable) {
                if (this.count() == 0) return@with serializeArray()
                if (this.all { it is Pair<*, *> }) {
                    serializeObject(this.map { (it as Pair<*, *>) }) { it.toString() }
                } else if (this.all { it is Map.Entry<*, *> }) {
                    serializeObject(this.map { (it as Map.Entry<*, *>) }) { it.toString() }
                } else serializeArray(this.iterator())
            }
        }

        else                        -> {
            //如果实现了[Serializable]接口，则调用其[serialization]方法
            if (this is Serializable) {
                return this.serialization()
            }
            //如果有[Serializable]注解，则调用其注解[Serializable.value]的对象实例的[serialization]方法
            this::class.findSerializable {
                return it.getSerializer<Any>().serialization(this)
            }
            //如果有缓存，则直接调用缓存的方法
            SerializeElement.serializerCache[this::class]?.let {
                return it(this)
            }
            //如果有方法名为[serialization]返回值类型为[SerializeElement]的无参方法，则调用该方法
            this::class.declaredFunctions.find {
                it.returnType.jvmErasure.isSubclassOf(SerializeElement::class)
                        && it.name == "serialization"
                        && it.parameters.size == 1
                        && it.parameters[0].type.jvmErasure == this::class
            }?.let {
                return it.call(this) as SerializeElement
            }
            //如果是枚举类型
            if (this::class.java.isEnum) {
                return (this as Enum<*>).toSerializeElement()
            }

            //如果是一个普通对象，则将其转换为[SerializeObject]
            return this.toSerializeObject()
        }
    }
}

fun Enum<*>.toSerializeElement(): SerializeElement {
    return SerializePrimitive(this.name)
}

@Suppress("UNCHECKED_CAST")
fun Any.toSerializeObject(): SerializeObject {
    //如果实现了[Serializable]接口，则调用其[serialization]方法
    if (this is Serializable) {
        return this.serialization().asObject
    }
    //如果有[Serializable]注解，则调用其注解[Serializable.value]的对象实例的[serialization]方法
    this::class.findSerializable {
        return it.getSerializer<Any>().serialization(this).asObject
    }
    //如果有缓存，则直接调用缓存的方法
    SerializeObject.serializerCache[this::class]?.let {
        return it(this)
    }
    //如果有方法名为[serialization]返回值类型为[SerializeObject]的无参方法，则调用该方法
    this::class.declaredFunctions.find {
        it.returnType.jvmErasure.isSubclassOf(SerializeObject::class)
                && it.name == "serialization"
                && it.parameters.size == 1
                && it.parameters[0].type.jvmErasure == this::class
    }?.let {
        return it.call(this) as SerializeObject
    }

    val obj = SerializeObject()
    this::class.apply {
        memberProperties.filter {
            //找到所有属性条件 属性可见性不为 INTERNAL 属性没有被Transient修饰
            it.visibility != INTERNAL && it.findAnnotation<Transient>() == null && it.javaField?.let { field -> !Modifier.isTransient(field.modifiers) } ?: true
        }.forEach { property ->
            property as KProperty1<Any, Any?>
            property.isAccessible = true
            //不能是委托属性
            if (property.getDelegate(this@toSerializeObject) == null) {
                val value = property.getter.call(this@toSerializeObject)
                val name = property.name
                obj.putAny(name, value)
            }
        }

    }
    return obj
}