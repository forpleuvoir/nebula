@file:OptIn(ExperimentalContracts::class)
@file:Suppress("NOTHING_TO_INLINE","UNUSED")

package moe.forpleuvoir.nebula.common.util

import kotlin.contracts.ExperimentalContracts
import kotlin.reflect.KClass

/**
 * 尝试将接收者转换为指定类型 [T]，失败时返回 null。
 *
 * @param T 期望的类型
 * @return 类型转换后的值，如果类型不匹配则返回 null
 */
inline fun <reified T> Any?.requireTypeOrNull(): T? = this as? T

/**
 * 要求接收者必须为指定类型 [T]，否则抛出异常。
 *
 * 如果接收者的类型是 [T] 则返回类型转换后的值，否则抛出 [IllegalStateException]。
 *
 * @param T 期望的类型
 * @param prefix 异常信息中可选的错误前缀
 * @return 类型转换后的值
 * @throws IllegalStateException 如果接收者的类型不是 [T]
 */
inline fun <reified T> Any?.requireType(prefix: String? = null): T = this as? T ?: throw expectedType(this?.let { it::class }, T::class, prefix = prefix)

/**
 * 检查接收者是否为指定类型 [T]，是则对其执行 [block] 并返回结果，否则抛出异常。
 *
 * @param T 期望的类型
 * @param R 返回值类型
 * @param prefix 异常信息中可选的错误前缀
 * @param block 当接收者为类型 [T] 时执行的代码块
 * @return [block] 的执行结果
 * @throws IllegalStateException 如果接收者的类型不是 [T]
 */
inline fun <reified T, R> Any?.checkType(prefix: String? = null, block: (T) -> R): R =
    this.requireTypeOrNull<T>()?.let(block) ?: throw expectedType(this?.let { it::class }, T::class, prefix = prefix)


inline fun <T> T?.applyNotNull(block: T.() -> Unit): T? = this?.apply(block)
inline fun <T, R> T?.letNotNull(block: (T) -> R): R? = this?.let(block)
inline fun <T> T?.alsoNotNull(block: (T) -> Unit): T? = this?.also(block)

//region 内部工具方法
@PublishedApi
internal inline fun prefix(prefix: String?): String = if (prefix != null) "$prefix " else ""

@PublishedApi
internal fun expected(expected: Array<out KClass<*>>, separator: CharSequence = ", "): String = expected.joinToString(separator, "[", "]")
//endregion

/**
 * 检查 Map 中是否包含所有指定的键，不包含则抛出异常。
 *
 * 如果 Map 中包含所有指定的键则返回当前 Map；否则抛出 [IllegalStateException]，
 *
 * 其中包含缺失的键和当前可用的键信息。
 *
 * 当传入的 [keys] 为空数组时将抛出异常。
 *
 * @param K 键的类型
 * @param V 值的类型
 * @param M Map 的具体类型
 * @param keys 需要检查的键
 * @return 包含所有指定键时返回当前 Map
 * @throws IllegalArgumentException 如果 [keys] 为空
 * @throws IllegalStateException 如果存在缺失的键，异常信息中会列出缺失的键和当前可用的键
 */
fun <K, V, M : Map<K, V>> M.requireKeys(vararg keys: K): M = requireKeysOrNull(*keys) ?: run {
    val missingKeys = keys.filterNot { this.containsKey(it) }
    val mapKeysStr = this.keys.joinToString(
        separator = ", ",
        limit = 10,
        truncated = "... (${this.size} total)"
    ) { "'$it'" }
    throw IllegalStateException(
        "Missing required keys: [${missingKeys.joinToString { "'$it'" }}], available keys: [$mapKeysStr]"
    )
}

/**
 * 检查 Map 中是否包含所有指定的键。
 *
 * 如果 Map 中包含所有指定的键，则返回当前 Map；否则返回 null。
 *
 * 当传入的 [keys] 为空数组时将抛出异常。
 *
 * @param K 键的类型
 * @param V 值的类型
 * @param M Map 的具体类型
 * @param keys 需要检查的键
 * @return 包含所有指定键时返回当前 Map，否则返回 null
 * @throws IllegalArgumentException 如果 [keys] 为空
 */
fun <K, V, M : Map<K, V>> M.requireKeysOrNull(vararg keys: K): M? {
    if (keys.isEmpty()) throw IllegalArgumentException("keys must not be empty")
    return if (keys.all { key -> this.containsKey(key) }) this
    else null
}

/**
 * 生成类型不匹配的异常信息。
 *
 * @param actual   实际类型。
 * @param expected 期望的类型列表。
 * @return 返回一个 [IllegalStateException] 实例，提示期望的类型与实际类型不匹配。
 */
fun expectedType(actual: KClass<*>?, vararg expected: KClass<*>, prefix: String? = null) =
    IllegalStateException("${prefix(prefix)}Expected type: ${expected(expected)}, but was [$actual]")

/**
 * 生成缺失字段的异常信息。
 *
 * @param field 缺失的字段名称。
 * @return 返回一个 [IllegalStateException] 实例，提示缺少的字段。
 */
inline fun fieldMissing(field: String, prefix: String? = null) =
    IllegalStateException("${prefix(prefix)}Missing required field: [$field]")

/**
 * 生成字段类型不匹配的异常信息。
 *
 * @param fieldName 字段名称。
 * @param actual    实际类型。
 * @param expected  期望类型。
 * @return 返回一个 [IllegalStateException] 实例，提示字段类型不匹配。
 */
fun fieldTypeMismatch(fieldName: String, actual: KClass<*>?, vararg expected: KClass<*>, prefix: String? = null) =
    IllegalStateException("${prefix(prefix)}Field '$fieldName' type mismatch: expected ${expected(expected)}, but was [$actual]")


/**
 * 创建一个指示缺少必需元素的 [IllegalStateException]。
 *
 * @param field 缺失元素的索引
 * @return 包含缺失元素信息的 [IllegalStateException]
 */
inline fun elementMissing(field: Int) =
    IllegalStateException("Missing required element at index [$field]")

/**
 * 创建一个指示指定索引处元素类型不匹配的 [IllegalStateException]。
 *
 * @param field 元素的索引
 * @param actual 实际的元素类型，可能为 null
 * @param expected 期望的一个或多个元素类型
 * @return 包含类型不匹配信息的 [IllegalStateException]
 */
inline fun elementTypeMismatch(field: Int, actual: KClass<*>?, vararg expected: KClass<*>) =
    IllegalStateException("Element at index [$field] type mismatch: expected ${expected(expected)}, but was [$actual]")

