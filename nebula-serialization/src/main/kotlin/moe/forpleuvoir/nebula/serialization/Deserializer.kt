package moe.forpleuvoir.nebula.serialization

import moe.forpleuvoir.nebula.serialization.base.SerializeElement

/**
 *

 * 项目名 nebula

 * 包名 moe.forpleuvoir.nebula.serialization

 * 文件名 Deserializer

 * 创建时间 2022/12/6 0:19

 * @author forpleuvoir

 */
@Suppress("unused")
fun interface Deserializer<T> {
    companion object

    fun deserialization(serializeElement: SerializeElement): T

    fun deserialization(serializeElement: SerializeElement?, default: T): T {
        return kotlin.runCatching { deserialization(serializeElement!!) }.getOrDefault(default)
    }

}