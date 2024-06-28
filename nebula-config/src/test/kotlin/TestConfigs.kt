import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.common.util.format
import moe.forpleuvoir.nebula.config.annotation.ConfigMeta
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
import kotlin.time.measureTime

object TestConfigs : ConfigManagerImpl("test") {

    init {
        components {
            localConfig({ Path.of("./nebula-config/build/config") }, ::jsonPersistence)
            autoSave(initialDelay = 5.seconds, period = 5.seconds) { needSave ->
                println(measureTime {
                    println("当前是否需要保存 ${this.manager.needSave}")
                    Numbers.int++
                    println("是否需要保存 ${if (needSave()) "是" else "否"}")
                    if (needSave()) println("${Thread.currentThread().name} 开始保存耗时：${Date().format("HH:mm:ss")}")
                    if (needSave()) save()
                })
            }
        }
    }

    @ConfigMeta(order = -999)
    object Numbers2 : ConfigContainerImpl("config_numbers2") {

        @ConfigMeta("整数配置测试")
        var int by ConfigInt("int", 10)

        @ConfigMeta("浮点数配置测试")
        var double by ConfigDouble("double", 10.0)
    }


    @ConfigMeta(description = "字符串配置测试")
    val bool by ConfigBoolean("bool", true)

    @ConfigMeta(description = "颜色配置测试")
    val color = ConfigColor("color", Color(255, 0, 0))

    @ConfigMeta(description = "字符串配置容器测试", -1)
    object Strings : ConfigContainerImpl("config_strings", descriptionKeyMap = { "#$it.desc" }) {
        @ConfigMeta(description = "循环字符串配置测试")
        var cycleString by ConfigCycleString("cycleString", listOf("一", "二", "三"), defaultValue = "二")

        @ConfigMeta("字符串列表配置测试")
        var stringList by ConfigStringList("stringList", listOf("element1", "element2", "element3"))

    }

    @ConfigMeta("枚举配置测试")
    val enumTest = ConfigEnum("enumTest", TestEnum.E2)

    @ConfigMeta("日期配置测试")
    val date = ConfigDate("date", Date())

    @ConfigMeta("时间配置测试")
    var duration by ConfigDuration("time", 15.minutes)

    @ConfigMeta("整数配置测试")
    object Numbers : ConfigContainerImpl("config_numbers") {

        @ConfigMeta("整数配置测试")
        var int by ConfigInt("int", 10)

        @ConfigMeta("浮点数配置测试")
        var double by ConfigDouble("double", 10.0)
    }

    enum class TestEnum {
        E1, E2, E3;
    }

}