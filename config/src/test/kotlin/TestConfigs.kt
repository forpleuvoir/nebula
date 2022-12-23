import TestConfigs.Tag1.test4
import com.forpleuvoir.nebula.common.color.Colors
import com.forpleuvoir.nebula.common.times
import com.forpleuvoir.nebula.common.util.format
import com.forpleuvoir.nebula.common.util.plus
import com.forpleuvoir.nebula.common.util.second
import com.forpleuvoir.nebula.config.impl.AutoSaveConfigManager
import com.forpleuvoir.nebula.config.impl.ConfigCategoryImpl
import com.forpleuvoir.nebula.config.impl.LocalConfigManager
import com.forpleuvoir.nebula.config.item.impl.*
import com.forpleuvoir.nebula.serialization.yaml.toYamlString
import com.forpleuvoir.nebula.serialization.yaml.yamlStringToObject
import java.nio.file.Path
import java.util.*

object TestConfigs : LocalConfigManager("test.yaml", { it.toYamlString() }, { it.yamlStringToObject() }), AutoSaveConfigManager {
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


	object Tag1 : ConfigCategoryImpl("tag1") {

		val test = ConfigString("test", "defaultValue")

		val test2 = ConfigInt("test2", 10)

		val test3 = ConfigColor("test3", Colors.AQUA)

		var test4 by ConfigDouble("date", 0.5)


		object Tag1_1 : ConfigCategoryImpl("tag1-1") {

			val test4 = ConfigStringList("test4", listOf("element1", "element2"))
		}

	}

}