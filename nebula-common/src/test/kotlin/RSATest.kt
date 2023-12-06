import moe.forpleuvoir.nebula.common.scanPackage
import moe.forpleuvoir.nebula.common.util.RSAUtil

fun main() {
    for (clazz in scanPackage("moe.forpleuvoir.nebula.common") {
        true
    }) {
        println("${clazz.qualifiedName} : ${clazz.java.name}")
    }
//	test1()
}

