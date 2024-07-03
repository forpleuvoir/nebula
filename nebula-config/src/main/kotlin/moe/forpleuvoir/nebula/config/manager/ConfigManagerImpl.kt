package moe.forpleuvoir.nebula.config.manager

import kotlinx.coroutines.Deferred
import moe.forpleuvoir.nebula.common.ioAsync
import moe.forpleuvoir.nebula.config.container.ConfigContainerImpl
import moe.forpleuvoir.nebula.config.manager.component.ConfigManagerComponent
import kotlin.time.Duration
import kotlin.time.measureTime

open class ConfigManagerImpl(
    key: String,
    autoScan: AutoScan = AutoScan.all,
) : ConfigManager, ConfigContainerImpl(key, autoScan) {

    private val components: MutableSet<ConfigManagerComponent> = mutableSetOf()

    override fun compose(component: ConfigManagerComponent): ConfigManager {
        components.add(component)
        return this
    }

    override fun init() {
        components.forEach { it.beforeInit() }
        super.init()
        components.forEach { it.afterInit() }
    }

    override suspend fun save(): Duration {
        measureTime {
            components.forEach { it.onSave() }
        }.let {
            onSaved.invoke(it)
            return it
        }
    }

    override fun asyncSave() = ioAsync { save() }

    override suspend fun forceSave(): Duration {
        measureTime {
            components.forEach { it.onForcedSave() }
        }.let {
            onSaved.invoke(it)
            return it
        }
    }

    override fun asyncForceSave() = ioAsync { forceSave() }

    override suspend fun load(): Duration {
        measureTime {
            components.forEach { it.onLoad() }
        }.let {
            onLoaded.invoke(it)
            return it
        }
    }

    override fun asyncLoad(): Deferred<Duration> = ioAsync { load() }


    override fun onSaved(callback: suspend (duration: Duration) -> Unit) {
        onSaved = callback
    }

    override fun onLoaded(callback: suspend (duration: Duration) -> Unit) {
        onLoaded = callback
    }

    private var onSaved: suspend (duration: Duration) -> Unit = {}

    private var onLoaded: suspend (duration: Duration) -> Unit = {}

}