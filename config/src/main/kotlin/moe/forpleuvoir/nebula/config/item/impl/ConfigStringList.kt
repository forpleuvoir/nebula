package moe.forpleuvoir.nebula.config.item.impl

import moe.forpleuvoir.nebula.common.util.NotifiableArrayList
import moe.forpleuvoir.nebula.config.ConfigBase
import moe.forpleuvoir.nebula.config.item.ConfigMutableListValue
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.extensions.serializeArray

class ConfigStringList(
	override val key: String,
	defaultValue: List<String>
) : ConfigBase<MutableList<String>, ConfigStringList>(), ConfigMutableListValue<String> {

	override val defaultValue: MutableList<String> = ArrayList(defaultValue)

	override var configValue: MutableList<String> = list(this.defaultValue)

	override fun restDefault() {
		if (isDefault()) return
		configValue = list(defaultValue)
		onChange(this)
	}

	private fun list(list: List<String>): NotifiableArrayList<String> {
		return NotifiableArrayList(list).apply {
			subscribe {
				this@ConfigStringList.onChange(this@ConfigStringList)
			}
		}
	}

	override fun serialization(): SerializeElement =
		serializeArray(configValue)

	override fun deserialization(serializeElement: SerializeElement) {
		val list = serializeElement.asArray.map { it.asString }
		configValue = list(list)
	}
}