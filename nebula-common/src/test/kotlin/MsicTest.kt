import moe.forpleuvoir.nebula.common.util.replace

fun main() {

    val a = "abc cc"
    val map = mapOf("ab" to "ac", "cc" to "123")
    val s = "acc 123"

    println(a.replace(map))
}


