package moe.forpleuvoir.nebula.config.manager

import moe.forpleuvoir.nebula.config.container.ConfigContainer
import moe.forpleuvoir.nebula.config.manager.component.ConfigManagerComponent
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.time.Duration

interface ConfigManager : ConfigContainer {

    companion object {

        operator fun invoke(
            key: String,
            autoScan: Boolean = true,
            descriptionKeyMap: (String) -> String = { "_$it" }
        ): ConfigManager {
            return ConfigManagerImpl(key, autoScan, descriptionKeyMap)
        }

    }

    /**
     * 所有对配置内容的操作都应该在此函数调用之后执行
     */
    override fun init()

    /**
     * 添加组件 应该在初始化阶段就完成
     * @param component ConfigManagerComponent
     * @return ConfigManager
     */
    fun compose(component: ConfigManagerComponent): ConfigManager

    suspend fun save()

    /**
     *  在配置保存完成后执行
     * @param callback (duration: Duration) -> Unit  duration为保存耗时
     */
    fun onSaved(callback: (duration: Duration) -> Unit)

    fun asyncSave()

    suspend fun forceSave()

    fun asyncForceSave()

    suspend fun load()

    /**
     * 在配置加载完成后执行
     * @param callback (duration: Duration) -> Unit  duration为加载耗时
     */
    fun onLoaded(callback: (duration: Duration) -> Unit)

    fun asyncLoad()

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