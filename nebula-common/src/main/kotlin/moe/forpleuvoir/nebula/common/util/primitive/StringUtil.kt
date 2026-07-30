package moe.forpleuvoir.nebula.common.util.primitive

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

fun String.replace(map: Map<String, Any>): String {
	var temp: String = this
	map.forEach { (k, v) ->
		temp = temp.replace(k, v.toString())
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

fun Iterable<CharSequence>.merge(length: Long, ellipsis: String = "...", separator: String = ", ", prefix: String = "", suffix: String = ""): String {
    val list = this.toList()
    val last = list.lastOrNull()
    val sb = StringBuilder(prefix)
    for ((index, s) in list.withIndex()) {
        if (index >= length) {
			sb.append(ellipsis)
			break
		}
		sb.append(s)
        if (last != null && last != s) sb.append(separator)
	}
	sb.append(suffix)
	return sb.toString()
}

fun CharArray.subSequence(startIndex: Int, endIndex: Int): String {
    return buildString {
        for (index in startIndex..endIndex) {
            append(this@subSequence[index])
        }
    }
}


/**
 * 将字符串拆分为命名单词。
 *
 * 支持识别：
 * - camelCase
 * - PascalCase
 * - 连续大写缩写，例如 HTTPServer
 * - snake_case
 * - kebab-case
 * - dot.case
 * - 空格分隔
 *
 * 示例：
 * - "helloWorld"  -> ["hello", "world"]
 * - "HTTPServer"  -> ["http", "server"]
 * - "hello_world" -> ["hello", "world"]
 */
private fun String.toNamingWords(): List<String> =
	trim()
		// HTTPServer -> HTTP_Server
		.replace(Regex("([A-Z]+)([A-Z][a-z])"), "$1_$2")
		// helloWorld -> hello_World
		.replace(Regex("([a-z0-9])([A-Z])"), "$1_$2")
		// 将常见分隔符统一视为单词边界
		.split(Regex("[\\s._-]+"))
		.filter(String::isNotEmpty)
		.map(String::lowercase)

/**
 * 将字符串转换为小驼峰命名。
 *
 * 示例：
 * - "hello_world" -> "helloWorld"
 * - "Hello World" -> "helloWorld"
 * - "HTTPServer"  -> "httpServer"
 */
fun String.toCamelCase(): String {
	val words = toNamingWords()
	if (words.isEmpty()) return ""

	return buildString {
		append(words.first())

		words.drop(1).forEach { word ->
			append(word.replaceFirstChar { it.uppercaseChar() })
		}
	}
}

/**
 * 将字符串转换为大驼峰命名（PascalCase）。
 *
 * 示例：
 * - "hello_world" -> "HelloWorld"
 * - "hello-world" -> "HelloWorld"
 * - "http_server" -> "HttpServer"
 */
fun String.toPascalCase(): String =
	toNamingWords().joinToString("") { word ->
		word.replaceFirstChar { it.uppercaseChar() }
	}

/**
 * 将字符串转换为小写下划线命名（snake_case）。
 *
 * 示例：
 * - "helloWorld" -> "hello_world"
 * - "HTTPServer" -> "http_server"
 * - "Hello World" -> "hello_world"
 */
fun String.toSnakeCase(): String =
	toNamingWords().joinToString("_")

/**
 * 将字符串转换为大写下划线命名（SCREAMING_SNAKE_CASE）。
 *
 * 通常用于常量名称。
 *
 * 示例：
 * - "helloWorld" -> "HELLO_WORLD"
 * - "http_server" -> "HTTP_SERVER"
 */
fun String.toScreamingSnakeCase(): String =
	toNamingWords().joinToString("_").uppercase()

/**
 * 将字符串转换为小写短横线命名（kebab-case）。
 *
 * 示例：
 * - "helloWorld" -> "hello-world"
 * - "HTTPServer" -> "http-server"
 * - "hello_world" -> "hello-world"
 */
fun String.toKebabCase(): String =
	toNamingWords().joinToString("-")

/**
 * 将字符串转换为大写短横线命名（SCREAMING-KEBAB-CASE）。
 *
 * 示例：
 * - "helloWorld" -> "HELLO-WORLD"
 * - "http_server" -> "HTTP-SERVER"
 */
fun String.toScreamingKebabCase(): String =
	toNamingWords().joinToString("-").uppercase()

/**
 * 将字符串转换为点分隔命名（dot.case）。
 *
 * 常用于配置键、包路径或属性名称。
 *
 * 示例：
 * - "helloWorld" -> "hello.world"
 * - "HTTPServer" -> "http.server"
 */
fun String.toDotCase(): String =
	toNamingWords().joinToString(".")

/**
 * 将字符串转换为标题格式（Title Case）。
 *
 * 示例：
 * - "hello_world" -> "Hello World"
 * - "httpServer"  -> "Http Server"
 */
fun String.toTitleCase(): String =
	toNamingWords().joinToString(" ") { word ->
		word.replaceFirstChar { it.uppercaseChar() }
	}

/**
 * 将字符串转换为火车命名（Train-Case）。
 *
 * 示例：
 * - "hello_world" -> "Hello-World"
 * - "httpServer"  -> "Http-Server"
 */
fun String.toTrainCase(): String =
	toNamingWords().joinToString("-") { word ->
		word.replaceFirstChar { it.uppercaseChar() }
	}