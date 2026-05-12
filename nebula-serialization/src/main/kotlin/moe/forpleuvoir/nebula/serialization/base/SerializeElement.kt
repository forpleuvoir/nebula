package moe.forpleuvoir.nebula.serialization.base

import java.math.BigDecimal
import java.math.BigInteger

/**
 *

 * 项目名 nebula

 * 包名 moe.forpleuvoir.nebula.serialization.base

 * 文件名 SerializeElement

 * 创建时间 2022/12/8 0:43

 * @author forpleuvoir

 */
sealed interface SerializeElement {

    companion object;

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

    val asPrimitive: SerializePrimitive?
        get() = when (this) {
            is SerializePrimitive -> this
            else                  -> null
        }

    val asObject: SerializeObject?
        get() = when (this) {
            is SerializeObject -> this
            else               -> null
        }

    val asArray: SerializeArray?
        get() = when (this) {
            is SerializeArray -> this
            else              -> null
        }

    val asNull: SerializeNull?
        get() = when (this) {
            is SerializeNull -> this
            else             -> null
        }

    val asChar: Char? get() = null

    val asString: String? get() = null

    val asBoolean: Boolean? get() = null

    val asNumber: Number? get() = null

    val asInt: Int? get() = null

    val asLong: Long? get() = null

    val asShort: Short? get() = null

    val asByte: Byte? get() = null

    val asFloat: Float? get() = null

    val asDouble: Double? get() = null

    val asBigInteger: BigInteger? get() = null

    val asBigDecimal: BigDecimal? get() = null


}
