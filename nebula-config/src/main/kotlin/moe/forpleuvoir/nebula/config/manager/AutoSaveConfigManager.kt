package moe.forpleuvoir.nebula.config.manager

import moe.forpleuvoir.nebula.common.util.schedule
import java.util.*
import kotlin.time.Duration

interface AutoSaveConfigManager : ConfigManager {

    val initialDelay: Duration

    val period: Duration

    val saveAction: (needSave: () -> Boolean) -> Unit
        get() = { if (it()) saveAsync() }

    override fun init() {
        Timer("AutoSaveConfigManager[${this.key}]").schedule(initialDelay, period) {
            saveAction { needSave }
        }
    }
}