import com.forpleuvoir.nebula.config.ConfigBase

/**
 *

 * 项目名 nebula

 * 包名

 * 文件名 ConfigString

 * 创建时间 2022/12/6 0:44

 * @author forpleuvoir

 */
class ConfigString<S : Any>(
	override val key: String,
	override val defaultValue: String
) : ConfigBase<String, S, ConfigString<S>>() {

	override var value: String = defaultValue


}