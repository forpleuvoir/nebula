import kotlinx.coroutines.runBlocking
import moe.forpleuvoir.nebula.config.path
import moe.forpleuvoir.nebula.config.pathWithRoot
import moe.forpleuvoir.nebula.config.persistence.JsonConfigPersistence
import moe.forpleuvoir.nebula.serialization.base.SerializeArray
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import moe.forpleuvoir.nebula.serialization.base.builder.build
import kotlin.test.Test

class ConfigMetaTest {

    @Test
    fun testConfig() {
        TestConfigs.onSaved { println("保存耗时 $it") }
        TestConfigs.onLoaded { println("加载耗时 $it") }
        TestConfigs.init()

        runBlocking {
            runCatching {
                TestConfigs.load()
            }.onFailure {
                TestConfigs.markSavable()
            }
            TestConfigs.forceSave()
        }

        println(TestConfigs.mtest.matched("\\bdefault\\b".toRegex()))
        println(TestConfigs.pathWithRoot)
        println(TestConfigs.Numbers.double.path)
        println(TestConfigs.Numbers.double.pathWithRoot)
        println(JsonConfigPersistence.encode(TestConfigs.serialization()))
    }

    @Test
    fun testSerializeObjectBuilder() {
        val obj = SerializeObject.build {
            "key"("value")
            "num"(42)
            "bool" to true
        }
        println(JsonConfigPersistence.encode(obj))
    }

    @Test
    fun testSerializeArrayCreate() {
        val arr = SerializeArray(SerializePrimitive("a"), SerializePrimitive("b"), SerializePrimitive("c"))
        println(JsonConfigPersistence.encode(arr))
    }

}
