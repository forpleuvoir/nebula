package moe.forpleuvoir.nebula.config

import moe.forpleuvoir.nebula.config.container.ConfigContainer
import moe.forpleuvoir.nebula.config.manager.ConfigManager
import java.util.function.Consumer
import kotlin.reflect.KProperty

/**
 *

 * 项目名 nebula

 * 包名 moe.forpleuvoir.nebula.config

 * 文件名 ConfigBase

 * 创建时间 2022/12/6 1:04

 * @author forpleuvoir

 */
@Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
abstract class ConfigBase<V, C : Config<V, C>> : Config<V, C> {
    override fun init() {
        restDefault()
    }

    override val configManager: () -> ConfigManager? = {
        if (parentContainer is ConfigManager) parentContainer as ConfigManager
        else parentContainer?.configManager?.invoke()
    }

    override var parentContainer: ConfigContainer? = null

    private val userData: MutableMap<String, Any> = mutableMapOf()

    override fun getUserData(key: String): Any? = userData[key]

    override fun setUserData(key: String, value: Any) {
        userData.put(key, value)
    }

    protected abstract var configValue: V

    operator fun component2(): V {
        return getValue()
    }

    protected open infix fun V.isEquals(other: V): Boolean = this == other

    protected open infix fun V.notEquals(other: V): Boolean = !(this isEquals other)

    override fun getValue(): V {
        return this.configValue
    }

    override fun setValue(value: V) {
        if (value notEquals this.configValue) {
            this.configValue = value
            this.onChange(this as C)
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>?): V {
        return getValue()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>?, value: V) {
        this.setValue(value)
    }

    override fun isDefault(): Boolean {
        return this.configValue isEquals defaultValue
    }

    override fun restDefault() {
        setValue(this, this::configValue, defaultValue)
    }

    protected open val observers: MutableList<Consumer<C>> = ArrayList()

    override fun subscribe(callback: Consumer<C>) {
        observers.add(callback)
    }

    override fun onChange(value: C) {
        configManager()?.markSavable()
        observers.forEach { it.accept(this as C) }
    }

    override fun matched(regex: Regex): Boolean {
        return regex.run {
            containsMatchIn(key) || containsMatchIn(getValue().toString())
        }
    }

    override fun toString(): String {
        return "${this.javaClass.simpleName}(${key} : ${configValue.toString()})"
    }


}