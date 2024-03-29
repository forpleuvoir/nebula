import kotlinx.coroutines.runBlocking
import moe.forpleuvoir.nebula.common.runAsync
import moe.forpleuvoir.nebula.config.Config
import moe.forpleuvoir.nebula.config.item.impl.ConfigString
import moe.forpleuvoir.nebula.config.item.impl.ConfigStringList
import moe.forpleuvoir.nebula.config.item.impl.ConfigStringMap
import moe.forpleuvoir.nebula.serialization.extensions.serializeObject
import moe.forpleuvoir.nebula.serialization.gson.toJsonString
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.isAccessible

fun main() {
    runAsync {
        Thread.sleep(5000)
        println("睡了5000")
    }
    TestConfigs.onSaved {
        println("保存耗时$it")
    }
    TestConfigs.onLoaded {
        println("加载耗时$it")
    }
    TestConfigs.init()

    runBlocking {
        runCatching {
            TestConfigs.load()
        }.onFailure {
            TestConfigs.needSave = true
        }
        TestConfigs.save()
    }
}

fun t() {
    for (memberProperty in T::class.declaredMemberProperties) {
        memberProperty.isAccessible = true
        val delegate = memberProperty.getDelegate(T)
        if (delegate != null) {
            println(delegate::class.isSubclassOf(Config::class))
        }
        println(delegate)
    }
}

object T {
    val str by ConfigString("test", "test")
    val str2 = ConfigString("test2", "test2")
}

val list = ConfigStringList("list", listOf("t1", "t2"))
val map = ConfigStringMap("map", mapOf("k1" to "v1", "k2" to "v2"))


fun test1() {
    println(serializeObject {
        list.key - list.serialization()
        map.key - map.serialization()
    }.toJsonString())
}