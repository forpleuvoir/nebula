package moe.forpleuvoir.nebula.config.manager.component

import moe.forpleuvoir.nebula.config.ConfigManager

interface ConfigManagerComponent {

    val manager: ConfigManager

    fun beginInit() = Unit

    fun finishInit() = Unit

    suspend fun onSave() = Unit

    suspend fun onForcedSave() = Unit

    suspend fun onLoad() = Unit

}