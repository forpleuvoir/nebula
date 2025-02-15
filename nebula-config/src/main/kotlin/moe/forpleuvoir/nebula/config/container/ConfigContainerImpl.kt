package moe.forpleuvoir.nebula.config.container

import moe.forpleuvoir.nebula.config.ConfigSerializable
import moe.forpleuvoir.nebula.config.manager.ConfigManager
import moe.forpleuvoir.nebula.config.userdata.order
import moe.forpleuvoir.nebula.serialization.DeserializationException
import moe.forpleuvoir.nebula.serialization.SerializationException
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.extensions.checkType
import moe.forpleuvoir.nebula.serialization.extensions.serializeObject
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.isAccessible

/**
 * 配置容器的默认实现
 * @param key String 配置的键
 * @param autoScan Boolean 是否启用自动扫描，如果启用，会自动将内部定义[普通属性, 委托属性，嵌套的对象]的配置添加到列表中
 * @constructor
 */
open class ConfigContainerImpl(
    override val key: String,
    private val autoScan: AutoScan = AutoScan.close,
) : ConfigContainer {

    data class AutoScan(
        val property: Boolean,
        val delegateProperty: Boolean,
        val nestedObject: Boolean,
    ) {
        companion object {

            val all = AutoScan(property = true, delegateProperty = true, nestedObject = true)

            val close = AutoScan(property = false, delegateProperty = false, nestedObject = false)

            val property = AutoScan(property = true, delegateProperty = false, nestedObject = false)

            val delegateProperty = AutoScan(property = true, delegateProperty = false, nestedObject = false)

            val nestedObject = AutoScan(property = false, delegateProperty = false, nestedObject = false)

        }
    }

    override val configManager: () -> ConfigManager? = {
        if (parentContainer is ConfigManager) parentContainer as ConfigManager
        else parentContainer?.configManager?.invoke()
    }

    override var parentContainer: ConfigContainer? = null

    private val configs: MutableMap<String, ConfigSerializable> = LinkedHashMap()

    private val userData: MutableMap<String, Any> = mutableMapOf()

    override fun getUserData(key: String): Any? = userData[key]

    override fun setUserData(key: String, value: Any) {
        userData.put(key, value)
    }

    override fun init() {
        loadConfigs()
        initConfigs()
    }

    override fun loadConfigs() {
        if (autoScan != AutoScan.close) autoScan()
    }

    override fun initConfigs() {
        for (config in configs()) {
            config.init()
        }
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun autoScan() {
        val configs = mutableListOf<ConfigSerializable>()
        for (memberProperty in this::class.declaredMemberProperties) {
            runCatching {
                memberProperty.isAccessible = true
            }.onFailure {
                continue
            }

            memberProperty as KProperty1<ConfigContainerImpl, *>

            var config: ConfigSerializable? = null

            if (autoScan.delegateProperty) {
                //获取委托属性
                memberProperty.getDelegate(this)?.let { property ->
                    if (property::class.isSubclassOf(ConfigSerializable::class)) {
                        config = property as ConfigSerializable
                    }
                }
            }
            if (autoScan.property) {
                //获取普通属性
                memberProperty.get(this)?.let { property ->
                    runCatching {
                        if (property::class.isSubclassOf(ConfigSerializable::class)) {
                            config = property as ConfigSerializable
                        }
                    }.onFailure {
                        //防止获取到高阶函数无法转换
                        if (it !is UnsupportedOperationException) throw it
                    }
                }
            }

            config?.let { c ->
                configs.add(c)
            }
        }
        if (autoScan.nestedObject) {
            //获取嵌套类
            for (nestedClass in this::class.nestedClasses) {
                //判断是否为ConfigSerializable子类的实例对象
                if (nestedClass.objectInstance != null && nestedClass.isSubclassOf(ConfigSerializable::class)) {
                    (nestedClass.objectInstance as ConfigSerializable).let { configSerializable ->
                        configs.add(configSerializable)
                    }
                }
            }
        }
    }

    override fun configs(): Collection<ConfigSerializable> {
        return configs.values.sortedBy { it.order }
    }

    override fun <C : ConfigSerializable> addConfig(config: C): C {
        configs[config.key] = config
        config.parentContainer = this
        return config
    }

    override fun serializationExceptionHandler(config: ConfigSerializable, e: SerializationException) {
        e.printStackTrace()
    }

    override fun serialization(): SerializeElement {
        return serializeObject {
            for (config in configs()) {
                runCatching {
                    config.key - config.serialization()
                }.onFailure {
                    val message = "${config.key}:serialization failed"
                    serializationExceptionHandler(config, SerializationException(message, it))
                }
            }
        }
    }

    override fun deserializationExceptionHandler(
        config: ConfigSerializable,
        serializeElement: SerializeElement,
        e: DeserializationException
    ) {
        configManager()?.markSavable()
        e.printStackTrace()
    }

    override fun deserialization(serializeElement: SerializeElement) {
        serializeElement.checkType<SerializeObject, Unit> {
            for (config in configs()) {
                runCatching {
                    config.deserialization(it[config.key]!!)
                }.onFailure {
                    var message = "${config.key}:\"${serializeElement}\" deserialization failed"
                    when (it) {
                        is NullPointerException -> message =
                            "not found key[${config.key}] from \"${serializeElement}\",the default value will be used"
                    }
                    deserializationExceptionHandler(config, serializeElement, DeserializationException(message, it))
                }
            }
        }
    }

}

