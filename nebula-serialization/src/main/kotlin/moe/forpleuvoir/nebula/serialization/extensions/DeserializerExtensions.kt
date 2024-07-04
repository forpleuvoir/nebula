package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.serialization.Deserializer
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeElement.Companion.deserializerCache
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.isSubclassOf

inline fun <reified T : Any> Deserializer.Companion.deserialization(serializeElement: SerializeElement): T {
    return deserialization(T::class, serializeElement)
}

fun <T : Any> Deserializer.Companion.deserialization(type: KClass<out T>, serializeElement: SerializeElement): T {
    //找到伴生对象
    type.companionObject?.let { companionObject ->
        val instance = type.companionObjectInstance!!
        //检查是否实现了[Deserializer]接口
        if (companionObject.isSubclassOf(Deserializer::class)) {
            //直接调用[Deserializer.deserialization]方法
            return (instance as Deserializer<T>).deserialization(serializeElement)
        }
    }

    //如果在缓存中直接调用缓存中的方法
    deserializerCache[type]?.let {
        return it.invoke(serializeElement) as T
    }

    type.companionObject?.let { companionObject ->
        val instance = type.companionObjectInstance!!
        companionObject.declaredFunctions.find {//寻找方法名为`deserialization`返回值类型为[type],参数只有一个serializeElement的方法
            it.parameters.size == 2
                    && it.parameters.last().type.classifier == SerializeElement::class
                    && it.name == "deserialization"
                    && it.returnType.classifier == type
        }?.let {
            //如果有则调用并返回
            return it.call(instance, serializeElement) as T
        }
    }
    TODO()
}
