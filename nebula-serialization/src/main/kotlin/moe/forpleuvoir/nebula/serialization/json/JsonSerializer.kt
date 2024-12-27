package moe.forpleuvoir.nebula.serialization.json

import moe.forpleuvoir.nebula.common.api.ExperimentalApi
import moe.forpleuvoir.nebula.serialization.base.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 将序列化元素[moe.forpleuvoir.nebula.serialization.base.SerializeElement]转换为json字符串
 * @param humanReadable 是否为人类能阅读的json字符串
 * @param indentationSize 缩进空格数 默认为2
 * @constructor
 */
@ExperimentalApi
class JsonSerializer(
    private val humanReadable: Boolean = false,
    private val indentationSize: Int = 2
) {

    companion object {

        @ExperimentalApi
        fun SerializeElement.dumpAsJson(humanReadable: Boolean = false, indentationSize: Int = 2): String {
            return JsonSerializer(humanReadable, indentationSize).elementDumpAsString(this)
        }
    }

    private var indentationLevel = 0

    private val indentation: String
        get() = buildString { for (index in 1..indentationLevel * indentationSize) append(' ') }

    @OptIn(ExperimentalContracts::class)
    private fun humanReadable(block: () -> Unit) {
        contract { callsInPlace(block, InvocationKind.AT_MOST_ONCE) }
        if (humanReadable) block()
    }


    private fun elementDumpAsString(serializeElement: SerializeElement): String {
        return when (serializeElement) {
            is SerializeObject    -> objectDumpAsString(serializeElement)
            is SerializeArray     -> arrayDumpAsString(serializeElement)
            is SerializeNull      -> nullDumpAsString(serializeElement)
            is SerializePrimitive -> primitiveDumpAsString(serializeElement)
        }
    }

    private fun objectDumpAsString(serializeObject: SerializeObject): String {
        return buildString {
            append('{')
            indentationLevel++
            humanReadable {
                append('\n')
                append(indentation)
            }
            serializeObject.apply {
                entries.forEachIndexed { index, mutableEntry ->
                    append("\"${mutableEntry.key}\"")
                    append(':')
                    humanReadable { append(' ') }
                    append(elementDumpAsString(mutableEntry.value))
                    if (index != this.size - 1) {
                        append(',')
                        humanReadable {
                            append('\n')
                            append(indentation)
                        }
                    }
                }
            }
            indentationLevel--
            humanReadable {
                append('\n')
                append(indentation)
            }
            append('}')
        }
    }

    private fun arrayDumpAsString(serializeArray: SerializeArray): String {
        return buildString {
            append('[')
            indentationLevel++
            serializeArray.forEachIndexed { index, value ->
                humanReadable {
                    append('\n')
                    append(indentation)
                }
                append(elementDumpAsString(value))
                if (index != serializeArray.size - 1) {
                    append(',')
                }
            }
            indentationLevel--
            humanReadable {
                append('\n')
                append(indentation)
            }
            append(']')
        }
    }

    private fun primitiveDumpAsString(serializePrimitive: SerializePrimitive): String {
        return when (val value = serializePrimitive.value) {  // 获取实际值
            is String -> "\"${escapeString(value)}\""         // 处理字符串值，添加转义
            else      -> value.toString()                         // 其他类型直接使用默认的 toString()
        }
    }

    private fun escapeString(input: String): String {
        return buildString {
            input.forEach { char ->
                when (char) {
                    '\"'     -> append("\\\"") // 转义双引号
                    '\\'     -> append("\\\\") // 转义反斜杠
                    '\n'     -> append("\\n")  // 转义换行符
                    '\r'     -> append("\\r")  // 转义回车符
                    '\t'     -> append("\\t")  // 转义制表符
                    '\b'     -> append("\\b")  // 转义退格符
                    '\u000C' -> append("\\f") // 转义换页符
                    else     -> append(char)    // 其他字符直接追加
                }
            }
        }
    }

    private fun nullDumpAsString(serializeNull: SerializeNull): String {
        return serializeNull.toString()
    }


}