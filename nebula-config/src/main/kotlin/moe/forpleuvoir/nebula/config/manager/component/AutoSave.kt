@file:Suppress("unused")

package moe.forpleuvoir.nebula.config.manager.component

import kotlinx.coroutines.delay
import moe.forpleuvoir.nebula.common.util.ioLaunch
import moe.forpleuvoir.nebula.config.ConfigManager
import kotlin.time.Duration

open class AutoSave(
    override val manager: ConfigManager,
    private val initialDelay: Duration,
    private val period: Duration,
    val saveAction: suspend (needSave: () -> Boolean) -> Unit = { if (it()) manager.save() },
) : ConfigManagerComponent {

    var isActive: Boolean = true
        set(value) {
            field = value
            if (field) finishInit()
        }

    override fun finishInit() {
        ioLaunch {
            delay(initialDelay)
            while (isActive) {
                delay(period)
                saveAction { manager.savable() }
            }
        }
    }
}

context(manager: ConfigManager)
fun autoSave(
    initialDelay: Duration,
    period: Duration,
    saveAction: suspend (needSave: () -> Boolean) -> Unit = { if (it()) manager.save() },
) = AutoSave(manager, initialDelay, period, saveAction).also { manager.compose(it) }
