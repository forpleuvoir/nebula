@file:Suppress("unused")

package moe.forpleuvoir.nebula.config

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import moe.forpleuvoir.nebula.common.util.ioAsync
import moe.forpleuvoir.nebula.config.manager.component.ConfigManagerComponent
import kotlin.time.Duration
import kotlin.time.TimeSource

open class ConfigManager(
    name: String,
    val exceptionHandler: ExceptionHandler = ExceptionHandler.Terminal,
) : ConfigGroup(name) {

    override val root: ConfigManager? get() = this

    override var parent: ConfigGroup?
        get() = null
        set(_) = Unit

    private val components: MutableList<ConfigManagerComponent> = mutableListOf()

    fun compose(component: ConfigManagerComponent): ConfigManager {
        components.add(component)
        return this
    }

    override fun init() {
        components.forEach { it.beginInit() }
        super.init()
        components.forEach { it.finishInit() }
    }

    private var shouldSave: Boolean = false

    fun markSavable() {
        shouldSave = true
    }

    fun markSaved() {
        shouldSave = false
    }

    fun savable(): Boolean = shouldSave

    suspend fun save(): Duration {
        val start = TimeSource.Monotonic.markNow()
        components.forEach { it.onSave() }
        val time = start.elapsedNow()
        onSaveCallback(time)
        return time
    }

    fun asyncSave(): Deferred<Duration> = ioAsync { save() }

    suspend fun forceSave(): Duration {
        val start = TimeSource.Monotonic.markNow()
        components.forEach { it.onForcedSave() }
        val time = start.elapsedNow()
        onSaveCallback(time)
        return time
    }

    fun asyncForceSave(): Deferred<Duration> = ioAsync { forceSave() }

    suspend fun load(): Duration {
        val start = TimeSource.Monotonic.markNow()
        components.forEach { it.onLoad() }
        val time = start.elapsedNow()
        onLoadedCallback(time)
        return time
    }

    fun asyncLoad(): Deferred<Duration> = ioAsync { load() }

    fun onSaved(callback: suspend (Duration) -> Unit) {
        onSaveCallback = callback
    }

    fun onLoaded(callback: suspend (Duration) -> Unit) {
        onLoadedCallback = callback
    }

    private var onSaveCallback: suspend (Duration) -> Unit = {}
    private var onLoadedCallback: suspend (Duration) -> Unit = {}
}

fun ConfigManager.startup() {
    init()
    runBlocking {
        runCatching { load() }.onFailure { forceSave() }
    }
}
