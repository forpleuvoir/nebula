package moe.forpleuvoir.nebula.config.manager.component

import kotlinx.coroutines.delay
import moe.forpleuvoir.nebula.common.util.ioLaunch
import moe.forpleuvoir.nebula.config.manager.ConfigManager
import moe.forpleuvoir.nebula.config.manager.ConfigManagerComponentScope
import kotlin.time.Duration

open class AutoSave(
    /**
     * 配置管理器
     */
    val manager: () -> ConfigManager,
    /**
     * 初始延迟
     */
    private val initialDelay: Duration,
    /**
     * 间隔
     */
    private val period: Duration,
    /**
     * 保存操作
     */
    val saveAction: suspend (needSave: () -> Boolean) -> Unit = { if (it()) manager().save() }
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
                saveAction { manager().savable() }
            }
        }
    }

}

fun ConfigManager.autoSave(
    initialDelay: Duration,
    period: Duration,
    saveAction: suspend (needSave: () -> Boolean) -> Unit = { if (it()) save() }
) = AutoSave({ this }, initialDelay, period, saveAction)

fun ConfigManagerComponentScope.autoSave(
    initialDelay: Duration,
    period: Duration,
    saveAction: suspend (needSave: () -> Boolean) -> Unit = { if (it()) this.manager.save() }
) = AutoSave({ this.manager }, initialDelay, period, saveAction).also { compose(it) }