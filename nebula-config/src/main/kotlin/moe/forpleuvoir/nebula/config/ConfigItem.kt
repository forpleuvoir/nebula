@file:Suppress("unused")

package moe.forpleuvoir.nebula.config

import kotlinx.serialization.KSerializer
import moe.forpleuvoir.nebula.common.api.Observable
import moe.forpleuvoir.nebula.common.api.Resettable
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.codec.Codec
import java.util.concurrent.CopyOnWriteArrayList

abstract class ConfigItem<C>(
    override val name: String,
    override val defaultValue: C,
) : ConfigNode, ConfigValued<C>, Resettable, Observable<ConfigItem<C>> {

    @Volatile
    protected var _value: C = defaultValue

    override var parent: ConfigGroup? = null

    private val metadata: MutableMap<String, Any> = mutableMapOf()

    override fun getMetadata(key: String): Any? = metadata[key]

    override fun setMetadata(key: String, value: Any) {
        metadata[key] = value
    }

    override fun getValue(): C = _value

    override fun setValue(value: C) {
        if (value valueNotEquals _value) {
            _value = value
            notifyChange()
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    protected inline infix fun C.valueEquals(b: C) = this == b

    @Suppress("NOTHING_TO_INLINE")
    protected inline infix fun C.valueNotEquals(b: C) = !this.valueEquals(b)

    override fun isDefault(): Boolean = _value valueEquals defaultValue

    override fun resetDefault() {
        if (isDefault()) return
        _value = defaultValue
        notifyChange()
    }

    override fun init() {}

    private val observers: MutableList<(ConfigItem<C>) -> Unit> = CopyOnWriteArrayList()

    override fun observe(callback: (ConfigItem<C>) -> Unit): Observable.Disposable {
        observers.add(callback)
        return Observable.Disposable { observers.remove(callback) }
    }

    override fun notifyChange(value: ConfigItem<C>) {
        root?.markSavable()
        observers.forEach { it(value) }
    }

    protected fun notifyChange() {
        notifyChange(this)
    }

    override fun matched(regex: Regex): Boolean {
        return regex.containsMatchIn(name) || regex.containsMatchIn(getValue().toString())
    }
}

open class Config<C>(
    name: String,
    defaultValue: C,
    private val serde: ConfigSerde<C>,
) : ConfigItem<C>(name, defaultValue) {

    override fun serialization(): SerializeElement = serde.encode(getValue())

    override fun deserialization(serializeElement: SerializeElement) {
        serde.decode(serializeElement).getOrThrow().let { setValue(it) }
    }
}

context(group: ConfigGroup)
fun <T> config(name: String, defaultValue: T, serde: ConfigSerde<T>) = group.addConfig(Config(name, defaultValue, serde))

context(group: ConfigGroup)
fun <T> config(name: String, defaultValue: T, codec: Codec<T>) = config(name, defaultValue, ConfigSerde.of(codec))

context(group: ConfigGroup)
fun <T> config(name: String, defaultValue: T, serializer: KSerializer<T>) = config(name, defaultValue, ConfigSerde.of(serializer))