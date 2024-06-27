package moe.forpleuvoir.nebula.config.manager

import moe.forpleuvoir.nebula.common.ioLaunch
import moe.forpleuvoir.nebula.config.container.ConfigContainerImpl
import moe.forpleuvoir.nebula.config.manager.component.ConfigManagerComponent
import kotlin.time.Duration
import kotlin.time.measureTime

open class ConfigManagerImpl(
    key: String,
    autoScan: Boolean = true,
    descriptionKeyMap: (String) -> String = { "_$it" }
) : ConfigManager, ConfigContainerImpl(key, autoScan, descriptionKeyMap) {

    protected val components: MutableSet<ConfigManagerComponent> = mutableSetOf()

    override fun compose(component: ConfigManagerComponent): ConfigManager {
        components.add(component)
        return this
    }

    override fun init() {
        super.init()
        components.forEach { it.onInit() }
    }

    override suspend fun save() {
        onSaved.invoke(
            measureTime {
                components.forEach { it.onSave() }
            }
        )
    }

    override fun asyncSave() {
        ioLaunch { save() }
    }

    override suspend fun forceSave() {
        onSaved.invoke(
            measureTime {
                components.forEach { it.onForcedSave() }
            }
        )
    }

    override fun asyncForceSave() {
        ioLaunch { forceSave() }
    }

    override suspend fun load() {
        onLoaded.invoke(
            measureTime {
                components.forEach { it.onLoad() }
            }
        )
    }

    override fun asyncLoad() {
        ioLaunch { load() }
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