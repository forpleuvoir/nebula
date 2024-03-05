package moe.forpleuvoir.nebula.config.manager.component

import moe.forpleuvoir.nebula.common.util.schedule
import moe.forpleuvoir.nebula.config.manager.ConfigManager
import moe.forpleuvoir.nebula.config.manager.ConfigManagerComponentContext
import java.util.*
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
    val saveAction: (needSave: () -> Boolean) -> Unit = { if (it()) manager().asyncSave() }
) : ConfigManagerComponent {

    override fun onInit() {
        Timer("AutoSaveConfigManager[${manager().key}]").schedule(initialDelay, period) {
            saveAction { manager().needSave }
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
) = AutoSave({ this }, initialDelay, period, saveAction)

fun ConfigManagerComponentContext.autoSave(
    initialDelay: Duration,
    period: Duration,
    saveAction: (needSave: () -> Boolean) -> Unit = { if (it()) this.manager.asyncSave() }
) = AutoSave({ this.manager }, initialDelay, period, saveAction).also { component(it) }