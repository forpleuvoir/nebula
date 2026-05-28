@file:Suppress("unused")

package moe.forpleuvoir.nebula.config

import kotlinx.serialization.KSerializer
import moe.forpleuvoir.nebula.common.api.Matchable
import moe.forpleuvoir.nebula.common.api.Observable
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.codec.Codec
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.reflect.KClass

abstract class Config<C>(
    override val name: String,
    override val defaultValue: C,
) : ConfigNode, ConfigValued<C>, Observable<Config<C>> {

    @Volatile
    protected open var configValue: C = defaultValue

    override val valueType: KClass<*>? by lazy { configValue?.let { it::class } }

    override var parent: ConfigGroup? = null

    private val metadata: MutableMap<String, Any> = mutableMapOf()

    override fun getMetadata(key: String): Any? = metadata[key]

    override fun setMetadata(key: String, value: Any) {
        metadata[key] = value
    }

    override fun getValue(): C = configValue

    override fun setValue(value: C) {
        if (value valueNotEquals configValue) {
            configValue = value
            notifyChange()
        }
    }

    protected open infix fun C.valueEquals(b: C) = this == b

    protected open infix fun C.valueNotEquals(b: C) = !this.valueEquals(b)

    override fun isDefault(): Boolean = configValue valueEquals defaultValue

    override fun resetDefault() {
        if (isDefault()) return
        configValue = defaultValue
        notifyChange()
    }

    override fun init() {}

    private val observers: MutableList<(Config<C>) -> Unit> = CopyOnWriteArrayList()

    override fun observe(callback: (Config<C>) -> Unit): Observable.Disposable {
        observers.add(callback)
        return Observable.Disposable { observers.remove(callback) }
    }

    override fun notifyChange(value: Config<C>) {
        root?.markSavable()
        observers.forEach { it(value) }
    }

    protected fun notifyChange() {
        notifyChange(this)
    }

    override fun matched(target: Regex): Boolean {
        @Suppress("UNCHECKED_CAST")
        return target.containsMatchIn(name) || (getValue() as? Matchable<Regex>)?.matched(target) == true || target.containsMatchIn(getValue().toString())
    }
}

open class ConfigItem<C : Any>(
    name: String,
    defaultValue: C,
    private val serde: ConfigSerde<C>,
) : Config<C>(name, defaultValue) {

    override fun serialization(): SerializeElement = serde.encode(getValue())

    override fun deserialization(data: SerializeElement) {
        serde.decode(data).getOrThrow().let { setValue(it) }
    }
}

context(group: ConfigGroup)
fun <T : Any> config(name: String, defaultValue: T, serde: ConfigSerde<T>) = group.addConfig(ConfigItem(name, defaultValue, serde))

@Suppress("NOTHING_TO_INLINE")
context(group: ConfigGroup)
inline fun <T : Any> config(name: String, defaultValue: T, codec: Codec<T>) = config(name, defaultValue, ConfigSerde.of(codec))

@Suppress("NOTHING_TO_INLINE")
context(group: ConfigGroup)
inline fun <T : Any> config(name: String, defaultValue: T, serializer: KSerializer<T>) = config(name, defaultValue, ConfigSerde.of(serializer))