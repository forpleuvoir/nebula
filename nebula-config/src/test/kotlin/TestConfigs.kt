import TestConfigs.Tag1.test4
import moe.forpleuvoir.nebula.common.color.Colors
import moe.forpleuvoir.nebula.common.util.format
import moe.forpleuvoir.nebula.common.util.plus
import moe.forpleuvoir.nebula.config.category.ConfigCategoryImpl
import moe.forpleuvoir.nebula.config.item.impl.*
import moe.forpleuvoir.nebula.config.manager.AutoSaveConfigManager
import moe.forpleuvoir.nebula.config.manager.LocalConfigManager
import moe.forpleuvoir.nebula.config.persistence.JsonConfigManagerPersistence
import java.nio.file.Path
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

object TestConfigs : LocalConfigManager("test"), AutoSaveConfigManager, JsonConfigManagerPersistence {
    override val configPath: Path = Path.of("./nebula-config/build/config")
    override val initialDelay: Duration = 5.seconds
    override val period: Duration = 5.seconds

    override val saveAction: (() -> Boolean) -> Unit
        get() = {
            println(measureTime {
                test4++
                println(test4)
                println("当前是否需要保存 ${this.needSave}")

                println("是否需要保存 ${if (it()) "是" else "否"}")
                if (it()) println("${Thread.currentThread().name} 开始保存：${Date().format("HH:mm:ss")}")
                if (it()) saveAsync()
            })
        }


    override fun init() {
        super<LocalConfigManager>.init()
        super<AutoSaveConfigManager>.init()
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