package moe.forpleuvoir.nebula.config.container

import moe.forpleuvoir.nebula.common.api.Notifiable
import moe.forpleuvoir.nebula.config.ConfigDescription
import moe.forpleuvoir.nebula.config.ConfigSerializable
import moe.forpleuvoir.nebula.config.annotation.ConfigMeta
import moe.forpleuvoir.nebula.config.annotation.ConfigMeta.Companion.createConfigDescription
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
 * @param  descriptionKeyMap (String) -> String 配置描述的键映射，如果配置描述没有设置键，则使用默认的映射规则
 * @constructor
 */
open class ConfigContainerImpl(
    override val key: String,
    private val autoScan: Boolean = true,
    private val descriptionKeyMap: (String) -> String = { "_$it" }
) : ConfigContainer {

    private val configSerializes: MutableMap<String, ConfigSerializable> = LinkedHashMap()

    override var needSave: Boolean = false
        get() {
            //如果有任意一个配置需要保存，则返回true
            return if (!allConfigSerializable().none { it::class.isSubclassOf(ConfigContainer::class) && (it as ConfigContainer).needSave }) true
            else field
        }
        set(value) {
            field = value
            if (!value) {
                allConfigSerializable().filter {
                    it::class.isSubclassOf(ConfigContainer::class)
                }.forEach {
                    it as ConfigContainer
                    it.needSave = false
                }
            }
        }

    override fun init() {
        configureSerializable()
        initSerializable()
    }

    override fun configureSerializable() {
        if (autoScan) autoScan()
    }

    @Suppress("UNCHECKED_CAST")
    override fun initSerializable() {
        for (config in allConfigSerializable()) {
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
        val configs = mutableListOf<Pair<ConfigSerializable, ConfigMeta?>>()
        for (memberProperty in this::class.declaredMemberProperties) {
            memberProperty.isAccessible = true
            memberProperty as KProperty1<ConfigContainerImpl, *>

            var config: ConfigSerializable? = null

            //获取委托属性
            memberProperty.getDelegate(this)?.let { property ->
                if (property::class.isSubclassOf(ConfigSerializable::class)) {
                    config = property as ConfigSerializable
                }
            }

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

            config?.let { c ->
                configs.add(c to memberProperty.findAnnotation<ConfigMeta>())
            }
        }
        //获取嵌套类
        for (nestedClass in this::class.nestedClasses) {
            //判断是否为ConfigSerializable子类的实例对象
            if (nestedClass.objectInstance != null && nestedClass.isSubclassOf(ConfigSerializable::class)) {
                (nestedClass.objectInstance as ConfigSerializable).let { configSerializable ->
                    configs.add(configSerializable to nestedClass.findAnnotation<ConfigMeta>())
                }
            }
        }
        configs.sortedBy { it.second?.order ?: 0x114514 }.forEach { (configSerializable, meta) ->
            addConfigSerializableWithDescription(configSerializable, meta?.createConfigDescription(configSerializable))
        }
    }

    override fun allConfigSerializable(): Iterable<ConfigSerializable> {
        return configSerializes.values
    }

    override fun <C : ConfigSerializable> addConfigSerializable(configSerializable: C): C {
        configSerializes[configSerializable.key] = configSerializable
        return configSerializable
    }

    fun addConfigSerializableWithDescription(configSerializable: ConfigSerializable, description: ConfigDescription?) {
        description?.let { configSerializes[it.key] = it }
        configSerializes[configSerializable.key] = configSerializable
    }

    fun addConfigSerializableWithDescription(configSerializable: ConfigSerializable, description: String, descriptionKeyMap: ((String) -> String)? = null) {
        ConfigDescription(configSerializable, description, descriptionKeyMap ?: this.descriptionKeyMap).let { configSerializes[it.key] = it }
        configSerializes[configSerializable.key] = configSerializable
    }

    override fun deserializationExceptionHandler(
        configSerializable: ConfigSerializable,
        serializeElement: SerializeElement,
        e: DeserializationException
    ) {
        needSave = true
        e.printStackTrace()
    }

    override fun serialization(): SerializeElement {
        return serializeObject {
            for (configSerialize in allConfigSerializable()) {
                configSerialize.key - configSerialize.serialization()
            }
        }
    }

    override fun deserialization(serializeElement: SerializeElement) {
        serializeElement.checkType<SerializeObject, Unit> {
            for (configSerialize in allConfigSerializable()) {
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

