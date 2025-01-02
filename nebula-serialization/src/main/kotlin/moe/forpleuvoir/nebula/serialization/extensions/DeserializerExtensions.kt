package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.common.api.ExperimentalApi
import moe.forpleuvoir.nebula.common.util.valueOf
import moe.forpleuvoir.nebula.serialization.Deserializer
import moe.forpleuvoir.nebula.serialization.annotation.Deserializable.Companion.findDeserializable
import moe.forpleuvoir.nebula.serialization.annotation.Deserializable.Companion.getDeserializer
import moe.forpleuvoir.nebula.serialization.annotation.SerializerName.Companion.getSerializerName
import moe.forpleuvoir.nebula.serialization.base.*
import moe.forpleuvoir.nebula.serialization.base.SerializeElement.Companion.deserializerCache
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.jvmErasure

@ExperimentalApi
inline fun <reified T : Any> Deserializer.Companion.deserialization(serializeElement: SerializeElement): T {
    return deserialization(T::class, serializeElement)
}

@Suppress("UNCHECKED_CAST")
@ExperimentalApi
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
            val parameters = mutableListOf<Any?>()
            buildList {
                constructor.parameters.forEach {
                    it.annotations
                    val name = it.getSerializerName()
                    add(obj[name]!!)
                }
            }.forEachIndexed { index, parameter ->
                val a = parameter.deserialization(constructor.parameters[index].type.jvmErasure)
                parameters.add(a)
            }
            return constructor.call(*parameters.toTypedArray())
        }
        throw IllegalArgumentException("no suitable constructor found in $type")
    }.getOrThrow()
}

@ExperimentalApi
inline fun <reified T : Any> SerializeArray.deserialization(): T =
    deserialization(T::class) as T

@ExperimentalApi
fun SerializeArray.deserialization(kClass: KClass<*>): Any {
    return when (kClass.javaObjectType) {
        LinkedList::class.java                  -> LinkedList<Any?>().apply {
            this@deserialization.forEach {
                add(it.deserialization(kClass))
            }
        }

        ArrayList::class.java, List::class.java -> ArrayList<Any?>().apply {
            this@deserialization.forEach {
                add(it.deserialization(kClass))
            }
        }

        LinkedHashSet::class.java               -> LinkedHashSet<Any?>().apply {
            this@deserialization.forEach {
                add(it.deserialization(kClass))
            }
        }

        HashSet::class.java, Set::class.java    -> HashSet<Any?>().apply {
            this@deserialization.forEach {
                add(it.deserialization(kClass))
            }
        }

        else                                    -> {
            if (kClass.java.isArray) {
                buildList {
                    this@deserialization.forEach {
                        add(it.deserialization(kClass))
                    }
                }.toTypedArray()
            } else throw IllegalArgumentException("$kClass is not supported array")
        }
    }
}


@OptIn(ExperimentalApi::class)
inline fun <reified T : Any> SerializeElement.deserialization(): T? =
    deserialization(T::class) as T


@OptIn(ExperimentalApi::class)
fun SerializeElement.deserialization(kClass: KClass<*>): Any? {
    return when (val s = this) {
        is SerializePrimitive -> s.deserialization(kClass)
        is SerializeArray     -> s.deserialization(kClass)
        is SerializeObject    -> s.deserialization(kClass)
        SerializeNull         -> null
    }
}

inline fun <reified T : Any> SerializePrimitive.deserialization(): T =
    deserialization(T::class) as T

fun SerializePrimitive.deserialization(kClass: KClass<*>): Any {
    return when (kClass) {
        String::class     -> asString
        Float::class      -> asFloat
        Double::class     -> asDouble
        Short::class      -> asShort
        Int::class        -> asInt
        Long::class       -> asLong
        Boolean::class    -> asBoolean
        Byte::class       -> asByte
        BigInteger::class -> asBigInteger
        BigDecimal::class -> asBigDecimal
        Number::class     -> asNumber
        else              -> value
    }
}

inline fun <reified E : Enum<*>> Enum.Companion.deserialization(serializeElement: SerializeElement): E {
    return deserialization(E::class, serializeElement)
}

fun <E : Enum<*>> Enum.Companion.deserialization(enumType: KClass<out E>, serializeElement: SerializeElement): E {
    return serializeElement.checkType<SerializePrimitive, E> {
        Enum.valueOf(enumType, it.asString) ?: throw IllegalArgumentException("cannot deserialize $enumType,no instance named ${it.asString} found")
    }.getOrThrow()
}

inline fun <reified T : Any> SerializeObject.deserialization(): T =
    deserialization(T::class) as T

@OptIn(ExperimentalApi::class)
fun SerializeObject.deserialization(kClass: KClass<*>): Any {
    return when (kClass.javaObjectType) {
        ConcurrentHashMap::class.java -> {
            ConcurrentHashMap<String, Any?>().apply {
                this@deserialization.forEach { k, v ->
                    this[k] = v.deserialization(kClass)
                }
            }
        }

        LinkedHashMap::class.java            -> {
            LinkedHashMap<String, Any?>().apply {
                this@deserialization.forEach { k, v ->
                    this[k] = v.deserialization(kClass)
                }
            }
        }

        HashMap::class.java, Map::class.java -> {
            HashMap<String, Any?>().apply {
                this@deserialization.forEach { k, v ->
                    this[k] = v.deserialization(kClass)
                }
            }
        }

        else                                 -> {
            Deserializer.deserialization(kClass, this)
        }

    }
}