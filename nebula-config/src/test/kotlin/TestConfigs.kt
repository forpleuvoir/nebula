import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.common.util.Time
import moe.forpleuvoir.nebula.common.util.format
import moe.forpleuvoir.nebula.config.Description
import moe.forpleuvoir.nebula.config.category.ConfigContainerImpl
import moe.forpleuvoir.nebula.config.item.impl.*
import moe.forpleuvoir.nebula.config.manager.AutoSaveConfigManager
import moe.forpleuvoir.nebula.config.manager.LocalConfigManager
import moe.forpleuvoir.nebula.config.persistence.ConfigManagerPersistence
import moe.forpleuvoir.nebula.config.persistence.JsonConfigManagerPersistence
import java.nio.file.Path
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.measureTime

object TestConfigs : LocalConfigManager("test"), AutoSaveConfigManager, ConfigManagerPersistence by JsonConfigManagerPersistence {
    override val configPath: Path = Path.of("./nebula-config/build/config")
    override val initialDelay: Duration = 5.seconds
    override val period: Duration = 5.seconds

    override val saveAction: (() -> Boolean) -> Unit
        get() = {
            println(measureTime {
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

    @Description("字符串配置测试")
    val bool = ConfigBoolean("bool", true)

    @Description("颜色配置测试")
    val color = ConfigColor("color", Color(255, 0, 0))

    @Description("字符串配置容器测试")
    object Strings : ConfigContainerImpl("config_strings", descriptionKeyMap = { "#$it.desc" }) {
        @Description("循环字符串配置测试")
        var cycleString by ConfigCycleString("cycleString", listOf("一", "二", "三"), defaultValue = "二")

        @Description("字符串列表配置测试")
        var stringList by ConfigStringList("stringList", listOf("element1", "element2", "element3"))

    }

    @Description("枚举配置测试")
    val enumTest = ConfigEnum("enumTest", TestEnum.E2)

    @Description("日期配置测试")
    val date = ConfigDate("date", Date())

    @Description("时间配置测试")
    val time = ConfigTime("time", Time(15.0, DurationUnit.MINUTES))

    @Description("整数配置测试")
    object Numbers : ConfigContainerImpl("config_numbers") {

        @Description("整数配置测试")
        var int by ConfigInt("int", 10)

        @Description("浮点数配置测试")
        var double by ConfigDouble("double", 10.0)
    }


    enum class TestEnum {
        E1, E2, E3
    }
}