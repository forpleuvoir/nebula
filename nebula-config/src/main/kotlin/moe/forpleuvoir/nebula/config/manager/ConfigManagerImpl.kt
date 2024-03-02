package moe.forpleuvoir.nebula.config.manager

import moe.forpleuvoir.nebula.config.container.ConfigContainerImpl
import moe.forpleuvoir.nebula.config.manager.plugin.ConfigManagerPlugin
import moe.forpleuvoir.nebula.config.util.configLaunch
import kotlin.time.Duration
import kotlin.time.measureTime

open class ConfigManagerImpl(
    key: String,
    autoScan: Boolean = true,
    descriptionKeyMap: (String) -> String = { "_$it" }
) : ConfigManager, ConfigContainerImpl(key, autoScan, descriptionKeyMap) {

    protected val plugins: MutableSet<ConfigManagerPlugin> = mutableSetOf()

    override fun plugin(plugin: ConfigManagerPlugin): ConfigManager {
        plugins.add(plugin)
        return this
    }

    override fun init() {
        super.init()
        plugins.forEach { it.onInit() }
    }

    override suspend fun save() {
        onSaved.invoke(
            measureTime {
                plugins.forEach { it.onSave() }
            }
        )
    }

    override fun asyncSave() {
        configLaunch { save() }
    }

    override suspend fun forceSave() {
        onSaved.invoke(
            measureTime {
                plugins.forEach { it.onForcedSave() }
            }
        )
    }

    override fun asyncForceSave() {
        configLaunch { forceSave() }
    }

    override suspend fun load() {
        onLoaded.invoke(
            measureTime {
                plugins.forEach { it.onLoad() }
            }
        )
    }

    override fun asyncLoad() {
        configLaunch { load() }
    }

    override fun onSaved(callback: (duration: Duration) -> Unit) {
        onSaved = callback
    }

    override fun onLoaded(callback: (duration: Duration) -> Unit) {
        onLoaded = callback
    }

    protected var onSaved: (duration: Duration) -> Unit = {}

    protected var onLoaded: (duration: Duration) -> Unit = {}

}