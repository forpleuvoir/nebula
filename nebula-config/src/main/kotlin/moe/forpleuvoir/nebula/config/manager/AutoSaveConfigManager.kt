package moe.forpleuvoir.nebula.config.manager

import java.util.*

interface AutoSaveConfigManager : ConfigManager {

    val starTime: Date

    val period: Long

    val saveAction: (needSave:()-> Boolean) -> Unit
        get() = { if (it()) saveAsync() }

    override fun init() {
        Timer("AutoSaveConfigManager[${this.key}]").schedule(
            object : TimerTask() {
                override fun run() {
                    saveAction { needSave }
                }
            }, starTime, period
        )
    }
}