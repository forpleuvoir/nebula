import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.common.color.HSVColor
import moe.forpleuvoir.nebula.common.util.format
import moe.forpleuvoir.nebula.config.container.ConfigContainerImpl
import moe.forpleuvoir.nebula.config.item.impl.*
import moe.forpleuvoir.nebula.config.manager.ConfigManagerImpl
import moe.forpleuvoir.nebula.config.manager.component.autoSave
import moe.forpleuvoir.nebula.config.manager.component.localConfig
import moe.forpleuvoir.nebula.config.manager.components
import moe.forpleuvoir.nebula.config.persistence.jsonPersistence
import java.nio.file.Path
import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource
import kotlin.time.measureTime

object TestConfigs : ConfigManagerImpl("test", autoScan = AutoScan.close) {

    init {
        components {
            localConfig({ Path.of("./build/config") }, ::jsonPersistence)
            autoSave(initialDelay = 5.seconds, period = 5.seconds) { needSave ->
                println(measureTime {
                    println("当前是否需要保存 ${if (this.manager.savable()) "是" else "否"}")
                    Numbers.int.setValue(Numbers.int.getValue() + 1)
                    println("是否需要保存 ${if (needSave()) "是" else "否"}")
                    if (needSave()) println("${Thread.currentThread().name} 开始保存：${Date().format("HH:mm:ss")}")
                    if (needSave()) save()
                })
            }
        }
    }

    private var mark = TimeSource.Monotonic.markNow()

    val number = addConfig(Numbers2, comment = "数字配置容器测试")

    object Numbers2 : ConfigContainerImpl("config_numbers2") {

        var int by addConfig(ConfigInt("int", 10), comment = "整数配置测试")

        var double by addConfig(ConfigDouble("double", 10.0), comment = "浮点数配置测试")

    }

    val bool by boolean("bool", false)

    val color = color("color", Color(255, 0, 0))

    val hsvColor by hsvColor("hsv_color", HSVColor(180f, 1f, 1f))

    val strings = addConfig(Strings())

    class Strings : ConfigContainerImpl("config_strings") {

        var cycleString by cycleString("cycleString", listOf("一", "二", "三"), defaultValue = "二")

        var stringList by stringList("stringList", listOf("element1", "element2", "element3"))

        var stringList2 = stringList("stringList2", listOf("element1", "element2", "element3"))

    }

    val enumTest = enum("enumTest", TestEnum.E2)

    val date = date("date", Date())

    var duration by duration("time", 15.minutes)

    val numbers = addConfig(Numbers)

    object Numbers : ConfigContainerImpl("config_numbers") {

        var int = int("int", 10).apply {
            subscribe {
                println("$it,数值有变!(${it.getValue()})")
            }
        }

        var double by double("double", 10.0)
    }

    enum class TestEnum {
        E1, E2, E3;
    }

}