package moe.forpleuvoir.nebula.common.util

/**
 * 如果字符串长度小于[length],则填充字符[fillChar]至长度[length]
 * @receiver [String]
 * @param length [Int]
 * @param fillChar [Char]
 * @param before [Boolean] true:在原字符串前填充,false:在原字符串后填充
 * @return String
 */
fun String.fill(length: Int, fillChar: Char, before: Boolean): String {
	val i = length - this.length
	val sb = StringBuilder()
	if (i > 0) {
		for (j in 0 until i) {
			sb.append(fillChar)
		}
	}
	return if (before) sb.toString() + this else this + sb.toString()
}

/**
 * 如果字符串长度小于[length],则原字符串前填充字符[fillChar]至长度[length]
 * @receiver [String]
 * @param length [Int]
 * @param fillChar [Char]
 * @return String
 */
fun String.fillBefore(length: Int, fillChar: Char): String {
	return fill(length, fillChar, true)
}

/**
 * 如果字符串长度小于[length],则原字符串后填充字符[fillChar]至长度[length]
 * @receiver [String]
 * @param length [Int]
 * @param fillChar [Char]
 * @return String
 */
fun String.fillAfter(length: Int, fillChar: Char): String {
	return fill(length, fillChar, false)
}

fun String.replace(map: Map<String, String>): String {
	var temp: String = this
	map.forEach { (k, v) ->
		temp = temp.replace(k, v)
	}
	return temp
}

fun String.replace(origin: Iterable<String>, new: String): String {
	var temp: String = this
	origin.forEach {
		temp = temp.replace(it, new)
	}
	return temp
}

fun String.replace(origin: Array<String>, new: String): String {
	var temp: String = this
	origin.forEach {
		temp = temp.replace(it, new)
	}
	return temp
}

fun Iterable<String>.format(length: Long, ellipsis: String = "...", separator: String = ", ", prefix: String = "", suffix: String = ""): String {
	val sb = StringBuffer(prefix)
	for ((index, s) in this.withIndex()) {
		if (index > length) {
			sb.append(ellipsis)
			break
		}
		sb.append(s)
		if (this.last() != s) sb.append(separator)
	}
	sb.append(suffix)
	return sb.toString()
}
