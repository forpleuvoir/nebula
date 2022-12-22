package com.forpleuvoir.nebula.config.impl

import com.forpleuvoir.nebula.config.Config
import com.forpleuvoir.nebula.config.ConfigCategory
import com.forpleuvoir.nebula.config.ConfigSerializable
import com.forpleuvoir.nebula.serialization.base.SerializeElement
import com.forpleuvoir.nebula.serialization.extensions.serializeObject
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

open class ConfigCategoryImpl(override val key: String) : ConfigCategory {

	private val configSerializes: MutableList<ConfigSerializable> = ArrayList()

	override fun init() {
		configSerializes.clear()

		for (nestedClass in this::class.nestedClasses) {
			if (nestedClass.objectInstance != null && nestedClass.isSubclassOf(ConfigSerializable::class))
				addConfigSerializable(nestedClass.objectInstance as ConfigSerializable)
		}

		for (memberProperty in this::class.declaredMemberProperties) {
			memberProperty.isAccessible = true
			memberProperty.javaField?.let {
				if (it.type.kotlin.isSubclassOf(Config::class)) {
					addConfigSerializable(it.get(this) as Config<*, *>)
				}
			}
		}

		for (config in configSerializes()) {
			config.init()
		}
	}

	override fun configSerializes(): Iterable<ConfigSerializable> {
		return configSerializes
	}

	override fun addConfigSerializable(configSerializable: ConfigSerializable): ConfigSerializable {
		configSerializes.add(configSerializable)
		return configSerializable
	}

	override fun serialization(): SerializeElement {
		return serializeObject {
			for (configSerialize in configSerializes()) {
				configSerialize.key - configSerialize.serialization()
			}
		}
	}

	override fun deserialization(serializeElement: SerializeElement) {
		serializeElement.asObject.apply {
			for (configSerialize in configSerializes()) {
				configSerialize.deserialization(this[configSerialize.key]!!)
			}
		}
	}

}