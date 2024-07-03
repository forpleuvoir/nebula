package moe.forpleuvoir.nebula.config.container

import moe.forpleuvoir.nebula.common.api.Notifiable
import moe.forpleuvoir.nebula.config.ConfigSerializable
import moe.forpleuvoir.nebula.config.annotation.ConfigMeta
import moe.forpleuvoir.nebula.config.annotation.ConfigMeta.Companion.merge
import moe.forpleuvoir.nebula.serialization.DeserializationException
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.extensions.checkType
import moe.forpleuvoir.nebula.serialization.extensions.serializeObject
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
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
    private val autoScan: AutoScan = AutoScan.all,
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

    private val configs: MutableMap<String, ConfigSerializable> = LinkedHashMap()

    private val configMetas: MutableMap<String, ConfigMeta> = LinkedHashMap()

    override var needSave: Boolean = false
        get() {
            //如果有任意一个配置需要保存，则返回true
            return if (!configs().none { it::class.isSubclassOf(ConfigContainer::class) && (it as ConfigContainer).needSave }) true
            else field
        }
        set(value) {
            field = value
            if (!value) {
                configs().filter {
                    it::class.isSubclassOf(ConfigContainer::class)
                }.forEach {
                    it as ConfigContainer
                    it.needSave = false
                }
            }
        }

    override fun init() {
        loadConfigs()
        initConfigs()
    }

    override fun loadConfigs() {
        autoScan()
    }

    @Suppress("UNCHECKED_CAST")
    override fun initConfigs() {
        for (config in configs()) {
            config.init()
            if (config::class.isSubclassOf(Notifiable::class)) {
                (config as Notifiable<Any>).subscribe {
                    needSave = true
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun autoScan() {
        val configs = mutableListOf<Pair<ConfigSerializable, ConfigMeta>>()
        for (memberProperty in this::class.declaredMemberProperties) {
            memberProperty.isAccessible = true
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
                configs.add(c to (memberProperty.findAnnotation<ConfigMeta>() ?: ConfigMeta()))
            }
        }
        if (autoScan.nestedObject) {
            //获取嵌套类
            for (nestedClass in this::class.nestedClasses) {
                //判断是否为ConfigSerializable子类的实例对象
                if (nestedClass.objectInstance != null && nestedClass.isSubclassOf(ConfigSerializable::class)) {
                    (nestedClass.objectInstance as ConfigSerializable).let { configSerializable ->
                        configs.add(configSerializable to (nestedClass.findAnnotation<ConfigMeta>() ?: ConfigMeta()))
                    }
                }
            }
        }
        configs.sortedBy { it.second.order }.forEach { (configSerializable, meta) ->
            addConfig(configSerializable, meta)
        }
    }

    override fun configs(): Collection<ConfigSerializable> {
        return configs.values
    }

    override fun <C : ConfigSerializable> addConfig(config: C): C {
        configs[config.key] = config
        return config
    }

    fun <C : ConfigSerializable> addConfig(config: C, meta: ConfigMeta): C {
        if (configMetas[config.key] != null) {
            configMetas[config.key] = configMetas[config.key]!!.merge(meta)
        }
        return addConfig(config)
    }

    fun <C : ConfigSerializable> addConfig(config: C, order: Int = ConfigMeta.DEFAULT_ORDER, description: String = ConfigMeta.EMPTY_DESC): C {
        return addConfig(config, ConfigMeta(description, order))
    }

    override fun deserializationExceptionHandler(
        config: ConfigSerializable,
        serializeElement: SerializeElement,
        e: DeserializationException
    ) {
        needSave = true
        e.printStackTrace()
    }

    override fun serialization(): SerializeElement {
        return serializeObject {
            for (configSerialize in configs()) {
                configSerialize.key - configSerialize.serialization()
            }
        }
    }

    override fun deserialization(serializeElement: SerializeElement) {
        serializeElement.checkType<SerializeObject, Unit> {
            for (configSerialize in configs()) {
                runCatching {
                    configSerialize.deserialization(it[configSerialize.key]!!)
                }.onFailure {
                    var message = "${configSerialize.key}:\"${serializeElement}\" deserialization failed"
                    when (it) {
                        is NullPointerException -> message =
                            "not found key[${configSerialize.key}] from \"${serializeElement}\",the default value will be used"
                    }
                    deserializationExceptionHandler(configSerialize, serializeElement, DeserializationException(message, it))
                }
            }
        }
    }

}

