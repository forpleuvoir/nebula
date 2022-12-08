@file:Suppress("unused")

package com.forpleuvoir.nebula.common

/**
 *

 * 项目名 nebula

 * 包名 com.forpleuvoir.nebula.common

 * 文件名 Misc

 * 创建时间 2022/11/30 13:52

 * @author forpleuvoir

 */

inline fun Boolean?.ifc(action: () -> Unit) {
	if (this == true) {
		action.invoke()
	}
}

inline fun Boolean?.notc(action: () -> Unit) = if (this != null) {
	if (!this) action() else Unit
} else Unit

fun <T> Boolean?.ternary(v1: T, v2: T): T = if (this == true) v1 else v2