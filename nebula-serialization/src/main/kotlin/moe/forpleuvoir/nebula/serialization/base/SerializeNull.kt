package moe.forpleuvoir.nebula.serialization.base

/**
 *

 * 项目名 nebula

 * 包名 moe.forpleuvoir.nebula.serialization.base

 * 文件名 SerializeNull

 * 创建时间 2022/12/8 1:12

 * @author forpleuvoir

 */
object SerializeNull : SerializeElement {

    override fun deepCopy(): SerializeElement = this

    override fun copy(): SerializeElement = this

    override fun equals(other: Any?): Boolean {
        return if (other == null) false
        else other === this
    }

    override fun hashCode(): Int {
        return SerializeNull::class.hashCode()
    }

    override fun toString(): String {
        return "null"
    }
}