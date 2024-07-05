package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.serialization.Deserializer
import moe.forpleuvoir.nebula.serialization.annotation.Deserializable.Companion.findDeserializable
import moe.forpleuvoir.nebula.serialization.annotation.Deserializable.Companion.getDeserializer
import moe.forpleuvoir.nebula.serialization.annotation.SerializerName.Companion.getSerializerName
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeElement.Companion.deserializerCache
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.isSubclassOf

inline fun <reified T : Any> Deserializer.Companion.deserialization(serializeElement: SerializeElement): T {
    return deserialization(T::class, serializeElement)
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> Deserializer.Companion.deserialization(type: KClass<T>, serializeElement: SerializeElement): T {
    //找到伴生对象
    type.companionObjectInstance?.let { companionObject ->
        //检查是否实现了[Deserializer]接口
        if (companionObject::class.isSubclassOf(Deserializer::class)) {
            //直接调用[Deserializer.deserialization]方法
            return (companionObject as Deserializer<T>).deserialization(serializeElement)
        }
    }

    //如果有[Serializable]注解，则调用其注解[Serializable.value]的对象实例的[serialization]方法
    this::class.findDeserializable {
        return it.getDeserializer<T>().deserialization(serializeElement)
    }

    //如果在缓存中直接调用缓存中的方法
    deserializerCache[type]?.let {
        return it.invoke(serializeElement) as T
    }

    //寻找方法名为`deserialization`返回值类型为[type],参数只有一个[SerializeElement]的方法
    type.companionObjectInstance?.let { companionObject ->
        companionObject::class.declaredFunctions.find {
            it.parameters.size == 2
                    && it.parameters.last().type.classifier == SerializeElement::class
                    && it.name == "deserialization"
                    && it.returnType.classifier == type
        }?.let {
            //如果有则调用并返回
            return it.call(companionObject, serializeElement) as T
        }
    }

    //如果是枚举类型
    if (type.java.isEnum) {
        return Enum.deserialization(type as KClass<out Enum<*>>, serializeElement) as T
    }
    //如果是普通的类
    return serializeElement.checkType<SerializeObject, T> { obj ->
        type.constructors.find { it.parameters.size == obj.size }?.let { constructor ->
            for (parameter in constructor.parameters) {
                parameter.getSerializerName()
            }
            constructor
        }
        throw IllegalArgumentException("no suitable constructor found in $type")
    }.getOrThrow()
}
