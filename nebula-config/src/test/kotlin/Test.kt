import kotlinx.coroutines.runBlocking
import moe.forpleuvoir.nebula.config.Config
import moe.forpleuvoir.nebula.config.fold
import moe.forpleuvoir.nebula.config.foldRight
import moe.forpleuvoir.nebula.config.item.impl.ConfigString
import moe.forpleuvoir.nebula.config.item.impl.ConfigStringList
import moe.forpleuvoir.nebula.config.item.impl.ConfigStringMap
import moe.forpleuvoir.nebula.serialization.extensions.serializeObject
import moe.forpleuvoir.nebula.serialization.gson.toJsonString
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.isAccessible
import kotlin.test.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


class ConfigMetaTest {


    @Test
    fun test() {
        println(5.5.seconds.toString())
        println(Duration.parse("5.4s"))
    }

    @Test
    fun test2() {
        val pattern = """#\{bind\[((\d+)|last)\]\}""".toRegex()
        val input = "Some string with #{bind[123]} and #{bind[last]}"

        if (pattern.containsMatchIn(input)) {
            println("Match found!")
            val matches = pattern.findAll(input)
            matches.forEach { matchResult ->
                println("Match: ${matchResult.value}")
            }
        } else {
            println("No match found.")
        }

    }

    @Test
    fun testConfig() {
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
                TestConfigs.markSavable()
            }
            TestConfigs.forceSave()
        }
        TestConfigs.init()
        TestConfigs.duration = 12.seconds
        TestConfigs.strings.stringList2.let {
            it.fold("") { acc, c ->
                val pre = if (c.parentContainer != null) "->" else ""
                acc + pre + c.key
            }.let(::println)
            it.foldRight("") { c, acc ->
                acc + c.key + if (c.parentContainer != null) "->" else ""
            }.let(::println)
            println(it.configManager()?.key)
        }
        Thread.sleep(50000)
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