package moe.forpleuvoir.nebula.config.manager

import kotlinx.coroutines.Deferred
import moe.forpleuvoir.nebula.config.container.ConfigContainer
import moe.forpleuvoir.nebula.config.manager.component.ConfigManagerComponent
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.time.Duration

interface ConfigManager : ConfigContainer {

    /**
     * 所有对配置内容的操作都应该在此函数调用之后执行
     */
    override fun init()

    override var configManager: () -> ConfigManager?

    /**
     * 将此配置管理器标记为可以保存的状态
     */
    fun markSavable()

    /**
     * 将此配置标记为以保存的状态
     */
    fun markSaved()

    fun savable(): Boolean

    /**
     * 添加组件 应该在初始化阶段就完成
     * @param component ConfigManagerComponent
     * @return ConfigManager
     */
    fun compose(component: ConfigManagerComponent): ConfigManager

    /**
     * 保存此配置管理器中的所有配置,只有当[savable]返回`true`时会触发真正的保存
     * @return Duration 耗时
     */
    suspend fun save(): Duration

    /**
     *  在配置保存完成后执行
     * @param callback (duration: Duration) -> Unit  duration为保存耗时
     */
    fun onSaved(callback: suspend (duration: Duration) -> Unit)

    fun asyncSave(): Deferred<Duration>

    /**
     *  保存此配置管理器中的所有配置,即使[savable]返回`false`时也会触发保存
     * @return Duration 耗时
     */
    suspend fun forceSave(): Duration

    fun asyncForceSave(): Deferred<Duration>

    suspend fun load(): Duration

    /**
     * 在配置加载完成后执行
     * @param callback (duration: Duration) -> Unit  duration为加载耗时
     */
    fun onLoaded(callback: suspend (duration: Duration) -> Unit)

    fun asyncLoad(): Deferred<Duration>

}

@OptIn(ExperimentalContracts::class)
inline fun ConfigManager.compose(pluginProvider: ConfigManager.() -> ConfigManagerComponent): ConfigManager {
    contract {
        callsInPlace(pluginProvider, InvocationKind.EXACTLY_ONCE)
    }
    return compose(pluginProvider.invoke(this))
}

@OptIn(ExperimentalContracts::class)
inline fun ConfigManager.components(scope: ConfigManagerComponentScope.() -> Unit): ConfigManager {
    contract {
        callsInPlace(scope, InvocationKind.EXACTLY_ONCE)
    }
    scope.invoke(ConfigManagerComponentScope(this))
    return this
}

class ConfigManagerComponentScope(val manager: ConfigManager) {
    fun compose(component: ConfigManagerComponent) = manager.compose(component)
}