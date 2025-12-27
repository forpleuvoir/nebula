@file:Suppress("UNUSED")

package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.common.net.HttpHelper
import moe.forpleuvoir.nebula.serialization.base.SerializeObject

fun <T> HttpHelper<T>.params(params: SerializeObject): HttpHelper<T> {
	return this.params(params.toJavaMap().mapValues { it.value.toString() })
}

fun <T> HttpHelper<T>.headers(headers: SerializeObject): HttpHelper<T> {
	return this.params(headers.toJavaMap().mapValues { it.value.toString() })
}