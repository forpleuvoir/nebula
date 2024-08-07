package moe.forpleuvoir.nebula.serialization.base

import moe.forpleuvoir.nebula.common.color.ARGBColor
import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.common.color.HSVColor
import moe.forpleuvoir.nebula.common.color.RGBColor
import moe.forpleuvoir.nebula.serialization.extensions.DateDeserializer
import moe.forpleuvoir.nebula.serialization.extensions.deserialization
import moe.forpleuvoir.nebula.serialization.extensions.serialization
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.reflect.KClass
import kotlin.time.Duration

/**
 *

 * 项目名 nebula

 * 包名 moe.forpleuvoir.nebula.serialization.base

 * 文件名 SerializeElement

 * 创建时间 2022/12/8 0:43

 * @author forpleuvoir

 */
sealed interface SerializeElement {

    companion object {

        internal val serializerCache: MutableMap<KClass<out Any>, (Any) -> SerializeElement> = mutableMapOf()

        internal val deserializerCache: MutableMap<KClass<out Any>, (SerializeElement) -> Any> = mutableMapOf()

        init {
            _register<Color>(Color::serialization, Color::deserialization)
            _register<HSVColor>(HSVColor::serialization, HSVColor::deserialization)
            _registerSerializer<RGBColor>(RGBColor::serialization)
            _registerSerializer<ARGBColor>(ARGBColor::serialization)
            _register<Duration>(Duration::serialization, Duration::deserialization)
            _register<Date>(Date::serialization, DateDeserializer::deserialization)
        }

        @Suppress("FunctionName")
        private inline fun <reified T : Any> _register(noinline serFunc: (T) -> SerializeElement, noinline desrFunc: (SerializeElement) -> T) {
            _registerSerializer<T>(serFunc)
            _registerDeserializer(desrFunc)
        }

        private inline fun <reified T : Any> register(noinline serFunc: (T) -> SerializeElement, noinline desrFunc: (SerializeElement) -> T) {
            registerSerializer<T>(serFunc)
            registerDeserializer(desrFunc)
        }

        @Suppress("UNCHECKED_CAST", "FunctionName")
        private inline fun <reified T : Any> _registerSerializer(noinline func: (T) -> SerializeElement) {
            serializerCache[T::class] = func as (Any) -> SerializeElement
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : Any> registerSerializer(type: KClass<T>, func: (T) -> SerializeElement) {
            serializerCache[type] = func as (Any) -> SerializeElement
        }

        inline fun <reified T : Any> registerSerializer(noinline func: (T) -> SerializeElement) {
            registerSerializer(T::class, func)
        }

        @Suppress("FunctionName")
        private inline fun <reified T : Any> _registerDeserializer(noinline func: (SerializeElement) -> T) {
            deserializerCache[T::class] = func
        }

        fun <T : Any> registerDeserializer(type: KClass<T>, func: (SerializeElement) -> T) {
            deserializerCache[type] = func
        }

        inline fun <reified T : Any> registerDeserializer(noinline func: (SerializeElement) -> T) {
            registerDeserializer(T::class, func)
        }

    }

    fun deepCopy(): SerializeElement

    fun copy(): SerializeElement

    /**
     * 是否为原始类型
     * @return Boolean
     */
    val isPrimitive: Boolean get() = this is SerializePrimitive

    /**
     * 是否为对象
     * @return Boolean
     */
    val isObject: Boolean get() = this is SerializeObject

    /**
     * 是否为数组
     * @return Boolean
     */
    val isArray: Boolean get() = this is SerializeArray

    /**
     * 是否为空
     * @return Boolean
     */
    val isNull: Boolean get() = this is SerializeNull

    val asPrimitive: SerializePrimitive
        get() {
            if (this.isPrimitive) {
                return this as SerializePrimitive
            }
            throw IllegalStateException("Not a serialization Primitive,type: ${this::class.simpleName}, '${toString()}' ")
        }

    val asObject: SerializeObject
        get() {
            if (this.isObject) {
                return this as SerializeObject
            }
            throw IllegalStateException("Not a serialization Object,type: ${this::class.simpleName}, '${toString()}' ")
        }

    val asArray: SerializeArray
        get() {
            if (this.isArray) {
                return this as SerializeArray
            }
            throw IllegalStateException("Not a serialization Array,type: ${this::class.simpleName}, '${toString()}' ")
        }

    val asNull: SerializeNull
        get() {
            if (this.isNull) {
                return this as SerializeNull
            }
            throw IllegalStateException("Not a serialization Null,type: ${this::class.simpleName}, '${toString()}' ")
        }

    val asString: String
        get() {
            throw UnsupportedOperationException(this::class.simpleName)
        }

    val asBoolean: Boolean
        get() {
            throw UnsupportedOperationException(this::class.simpleName)
        }

    val asNumber: Number
        get() {
            throw UnsupportedOperationException(this::class.simpleName)
        }

    val asInt: Int
        get() {
            throw UnsupportedOperationException(this::class.simpleName)
        }

    val asLong: Long
        get() {
            throw UnsupportedOperationException(this::class.simpleName)
        }

    val asShort: Short
        get() {
            throw UnsupportedOperationException(this::class.simpleName)
        }

    val asByte: Byte
        get() {
            throw UnsupportedOperationException(this::class.simpleName)
        }

    val asFloat: Float
        get() {
            throw UnsupportedOperationException(this::class.simpleName)
        }

    val asDouble: Double
        get() {
            throw UnsupportedOperationException(this::class.simpleName)
        }

    val asBigInteger: BigInteger
        get() {
            throw UnsupportedOperationException(this::class.simpleName)
        }

    val asBigDecimal: BigDecimal
        get() {
            throw UnsupportedOperationException(this::class.simpleName)
        }


}
