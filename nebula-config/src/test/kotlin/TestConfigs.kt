import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.config.ConfigGroup
import moe.forpleuvoir.nebula.config.ConfigManager
import moe.forpleuvoir.nebula.config.comment
import moe.forpleuvoir.nebula.config.item.*
import moe.forpleuvoir.nebula.config.manager.component.localConfig
import moe.forpleuvoir.nebula.config.persistence.yaml
import moe.forpleuvoir.nebula.serialization.codec.Codec
import java.nio.file.Path
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.minutes

object TestConfigs : ConfigManager("test") {

    init {
        localConfig(Path.of("./build/config"), yaml())
    }


    private val numbers2 = Numbers2

    object Numbers2 : ConfigGroup("config_numbers2", this) {
        init {
            comment("数字配置容器测试")
        }

        val int = configInt("int", 10).comment("整数配置测试")
        val double = configDouble("double", 10.0).comment("浮点数配置测试")
    }

    val bool = configBoolean("bool", false)

    val color = configColor("color", Color.fromARGB(0xFFFF0000))

    val strings = Strings()

    class Strings : ConfigGroup("config_strings", this) {
        val stringList = configList("stringList", listOf("element1", "element2", "element3"), Codec.string)
    }

    val enumTest = configEnum("enumTest", TestEnum.E2)

    val enumTest2 = configEnum("enumTest2", TimeUnit.MICROSECONDS)

    var duration by configDuration("time", 15.minutes)

    private val numbers = Numbers

    object Numbers : ConfigGroup("config_numbers", this) {
        val int = configInt("int", 10).apply {
            subscribe {
                println("$it, 数值有变!(${it.getValue()})")
            }
        }.comment("这是int的注释")

        val double = configDouble("double", 10.0)

        private val numbers = Numbers

        object Numbers : ConfigGroup("config_numbers", this) {
            val int = configInt("int", 10).apply {
                subscribe {
                    println("$it, 数值有变!(${it.getValue()})")
                }
            }.comment("这是int的注释")

            val double = configDouble("double", 10.0)
        }
    }

    val map by configMap(
        "map", mapOf(
            "key1" to "value1",
            "key2" to "value2",
            "key3" to "value3",
            "user" to "forpleuvoir"
        ), Codec.string
    )

    enum class TestEnum {
        E1, E2, E3
    }

}
