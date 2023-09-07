package moe.forpleuvoir.nebula.config.impl

import moe.forpleuvoir.nebula.common.api.Notifiable
import moe.forpleuvoir.nebula.config.Config
import moe.forpleuvoir.nebula.config.ConfigCategory
import moe.forpleuvoir.nebula.config.ConfigSerializable
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.extensions.serializeObject
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.isAccessible

open class ConfigCategoryImpl(override val key: String) : ConfigCategory {

    private val configSerializes: MutableList<ConfigSerializable> = ArrayList()

    override var needSave: Boolean = false

    @Suppress("UNCHECKED_CAST")
    override fun init() {
        configSerializes.clear()

        for (nestedClass in this::class.nestedClasses) {
            if (nestedClass.objectInstance != null && nestedClass.isSubclassOf(ConfigSerializable::class)) {
                addConfigSerializable(nestedClass.objectInstance as ConfigSerializable)

            }
        }

        for (memberProperty in this::class.declaredMemberProperties) {
            memberProperty.isAccessible = true
            memberProperty as KProperty1<ConfigCategoryImpl, *>

            //获取委托属性
            memberProperty.getDelegate(this)?.let {
                if (it::class.isSubclassOf(Config::class)) {
                    addConfigSerializable(it as Config<*, *>)
                }
            }

            //获取普通属性
            memberProperty.get(this)?.let {
                try {
                    if (it::class.isSubclassOf(Config::class)) {
                        addConfigSerializable(it as Config<*, *>)
                    }
                } catch (_: UnsupportedOperationException) {
                    //防止获取到高阶函数无法转换
                }
            }

        }

        for (config in configSerializes()) {
            config.init()
            if (config::class.isSubclassOf(Notifiable::class)) {
                (config as Notifiable<Any>).subscribe {
                    needSave = true
                }
            }
        }
    }

    override fun configSerializes(): Iterable<ConfigSerializable> {
        return configSerializes
    }

    override fun addConfigSerializable(configSerializable: ConfigSerializable): ConfigSerializable {
        configSerializes.add(configSerializable)
        return configSerializable
    }

    override fun deserializationExceptionHandler(
        configSerializable: ConfigSerializable,
        serializeElement: SerializeElement,
        e: Exception
    ) {
        needSave = true
        println("${configSerializable.key}:{${serializeElement}} deserialization failed")
        e.printStackTrace()
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
                try {
                    configSerialize.deserialization(this[configSerialize.key]!!)
                } catch (e: Exception) {
                    deserializationExceptionHandler(configSerialize, serializeElement, e)
                }
            }
        }
    }

}
