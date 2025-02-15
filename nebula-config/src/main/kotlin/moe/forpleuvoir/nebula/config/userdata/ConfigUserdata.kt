@file:Suppress("nothing_to_inline", "unused")

package moe.forpleuvoir.nebula.config.userdata

import moe.forpleuvoir.nebula.config.ConfigSerializable

//------------ Comment ------------\\

private const val COMMENT_KEY = "#comment"

val ConfigSerializable.comment: String?
    get() = getUserData(COMMENT_KEY) as? String

fun ConfigSerializable.setComment(comment: String) {
    setUserData(COMMENT_KEY, comment)
}

inline fun <T : ConfigSerializable> T.comment(comment: String): T = apply { setComment(comment) }

//------------ Order ------------\\

private const val ORDER_KEY = "#order"

const val DEFAULT_ORDER = 0x114514

/**
 * 用于获取配置实例的顺序。
 *
 * 该顺序决定了配置在容器中的排列顺序。
 * 排序应为由大到小
 * 如未显式设置，将返回默认顺序值[DEFAULT_ORDER]。
 */
val ConfigSerializable.order: Int
    get() = getUserData(ORDER_KEY) as? Int ?: DEFAULT_ORDER

fun ConfigSerializable.setOrder(order: Int) {
    setUserData(ORDER_KEY, order)
}

inline fun <T : ConfigSerializable> T.order(order: Int): T = apply { setOrder(order) }