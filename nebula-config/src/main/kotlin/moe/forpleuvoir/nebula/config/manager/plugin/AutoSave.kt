package moe.forpleuvoir.nebula.config.manager.plugin

import moe.forpleuvoir.nebula.common.util.schedule
import moe.forpleuvoir.nebula.config.manager.ConfigManager
import moe.forpleuvoir.nebula.config.manager.ConfigManagerPluginContext
import java.util.*
import kotlin.time.Duration

open class AutoSave(
    override val manager: ConfigManager,
    private val initialDelay: Duration,
    private val period: Duration,
    val saveAction: (needSave: () -> Boolean) -> Unit = { if (it()) manager.asyncSave() }
) : ConfigManagerPlugin {

    override fun onInit() {
        Timer("AutoSaveConfigManager[${manager.key}]").schedule(initialDelay, period) {
            saveAction { manager.needSave }
        }
    }

    override fun onSave() = Unit

    override fun onForcedSave() = Unit

    override fun onLoad() = Unit
}

fun ConfigManager.autoSave(
    initialDelay: Duration,
    period: Duration,
    saveAction: (needSave: () -> Boolean) -> Unit = { if (it()) asyncSave() }
) = AutoSave(this, initialDelay, period, saveAction)

fun ConfigManagerPluginContext.autoSave(
    initialDelay: Duration,
    period: Duration,
    saveAction: (needSave: () -> Boolean) -> Unit = { if (it()) this.manager.asyncSave() }
) = AutoSave(this.manager, initialDelay, period, saveAction).also { plugin(it) }