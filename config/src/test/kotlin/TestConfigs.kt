import TestConfigs.Tag1.test4
import moe.forpleuvoir.nebula.common.color.Colors
import moe.forpleuvoir.nebula.common.times
import moe.forpleuvoir.nebula.common.util.format
import moe.forpleuvoir.nebula.common.util.plus
import moe.forpleuvoir.nebula.common.util.second
import moe.forpleuvoir.nebula.config.impl.*
import moe.forpleuvoir.nebula.config.item.impl.*
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.extensions.toSerializeObject
import moe.forpleuvoir.nebula.serialization.json.jsonStringToObject
import moe.forpleuvoir.nebula.serialization.json.parseToJsonObject
import moe.forpleuvoir.nebula.serialization.json.toJsonString
import java.nio.file.Path
import java.util.*

object TestConfigs : LocalConfigManager("test"), AutoSaveConfigManager {
    override val configPath: Path = Path.of("./config/build/config")
    override val starTime: Date = Date() + 30.second
    override val period: Long = 30.second

    override val saveAction: () -> Unit
        get() = {
            times {
                println("开始保存：${Date().format("HH:mm:ss")}")
                test4++
                saveAsync()
            }
        }


    override fun init() {
        super<LocalConfigManager>.init()
        super<AutoSaveConfigManager>.init()
    }

    override fun fileName(key: String): String {
        return "$key.json"
    }

    override fun serializeObjectToString(serializeObject: SerializeObject): String {
        return serializeObject.toJsonString()
    }

    override fun stringToSerializeObject(str: String): SerializeObject {
        return str.jsonStringToObject()
    }

    val a_test by ConfigString("test", "外部测试")

    object Tag1 : ConfigCategoryImpl("tag1") {

        val test = ConfigString("test", "defaultValue")

        val test2 = ConfigInt("test2", 10)

        val test3 = ConfigColor("test3", Colors.AQUA)

        var test4 by ConfigDouble("test4", 0.5)
        val test8 = ConfigStringMap("test8", mapOf("k1" to "v1", "k2" to "v2"))

        object Tag1_1 : ConfigCategoryImpl("tag1_1") {

            val test5 = ConfigStringList("test5", listOf("element1", "element2"))

            val test6 = ConfigStringList("test6", listOf("element3", "element4"))

            val test7 = ConfigStringMap("test7", mapOf("k1" to "v1", "k2" to "v2"))
        }

    }

}