@file:Suppress("unused")

package moe.forpleuvoir.nebula.config

import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.builder.build
import moe.forpleuvoir.nebula.serialization.extensions.checkType

open class ConfigGroup(
    override val name: String
) : ConfigNode {

    constructor(name: String, parent: ConfigGroup) : this(name) {
        parent.addConfig(this)
    }

    override var parent: ConfigGroup? = null

    private val metadata: MutableMap<String, Any> = mutableMapOf()

    override fun getMetadata(key: String): Any? = metadata[key]

    override fun setMetadata(key: String, value: Any) {
        metadata[key] = value
    }

    private val _children: MutableList<ConfigNode> = mutableListOf()

    override fun init() {
        _children.forEach { it.init() }
    }

    val children: Collection<ConfigNode> get() = _children

    fun <T : ConfigNode> addConfig(child: T): T {
        require(!_children.any { it.name == child.name }) {
            "ConfigGroup[$name] already contains a child named \"${child.name}\""
        }
        child.parent = this
        _children += child
        return child
    }

    fun isDefault(): Boolean = children.all {
        when (it) {
            is ConfigItem<*> -> it.isDefault()
            is ConfigGroup   -> it.isDefault()
            else             -> true
        }
    }

    fun restDefault() {
        children.forEach {
            when (it) {
                is ConfigItem<*> -> it.resetDefault()
                is ConfigGroup   -> it.restDefault()
            }
        }
    }

    override fun matched(regex: Regex): Boolean =
        regex.containsMatchIn(name) || children.any { it.matched(regex) }

    override fun serialization(): SerializeElement = SerializeObject.build {
        _children.forEach { child ->
            runCatching {
                obj[child.name] = child.serialization()
            }.onFailure { e ->
                val se = SerializationException("Config[${child.name}] serialization failed", e)
                root?.exceptionHandler?.onSerializationException(child, se) ?: throw se
            }
        }
    }

    override fun deserialization(data: SerializeElement) {
        data.checkType<SerializeObject, Unit> { obj ->
            _children.forEach { child ->
                obj[child.name]?.let { element ->
                    runCatching {
                        child.deserialization(element)
                    }.onFailure { e ->
                        root?.markSavable()
                        root?.exceptionHandler?.onDeserializationException(
                            child,
                            DeserializationException("Config[${child.name} failed to deserialize value: $element", e)
                        )
                    }
                } ?: {
                    root?.markSavable()
                    root?.exceptionHandler?.onDeserializationException(
                        child,
                        DeserializationException("Config[${child.name}] is missing from the deserialization data")
                    )
                }
            }
        }.onFailure {
            root?.markSavable()
            root?.exceptionHandler?.onDeserializationException(
                this,
                DeserializationException("Config[$name] deserialization failed: require SerializeObject,but find ${data.javaClass.name}", it)
            )
        }
    }
}

val ConfigGroup.items: List<ConfigItem<*>> get() = children.filterIsInstance<ConfigItem<*>>()

val ConfigGroup.groups: List<ConfigGroup> get() = children.filterIsInstance<ConfigGroup>()

val ConfigGroup.flat: List<ConfigNode>
    get() = listOf(this) + children.flatMap {
        when (it) {
            is ConfigGroup -> it.flat
            else           -> listOf(it)
        }
    }
