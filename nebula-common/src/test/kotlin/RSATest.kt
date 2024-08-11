import moe.forpleuvoir.nebula.common.util.reflect.ClassScanner.scanPackage

fun main() {
    for (clazz in scanPackage("moe.forpleuvoir.nebula.common") {
        true
    }) {
        println("${clazz.qualifiedName} : ${clazz.java.name}")
    }
//	test1()
}

