import com.forpleuvoir.nebula.config.ConfigBase
import com.forpleuvoir.nebula.config.item.ConfigStringValue

/**
 *

 * 项目名 nebula

 * 包名

 * 文件名 ConfigString

 * 创建时间 2022/12/6 0:44

 * @author forpleuvoir

 */
class ConfigString<S>(
	override val key: String,
	override val defaultValue: String
) : ConfigBase<String, S, ConfigString<S>>(),ConfigStringValue {

	override var configValue: String = defaultValue

	override fun matched(regex: Regex): Boolean {
		return super.matched(regex)
	}

}