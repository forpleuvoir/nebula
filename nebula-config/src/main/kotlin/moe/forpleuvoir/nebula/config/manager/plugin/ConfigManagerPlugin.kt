package moe.forpleuvoir.nebula.config.manager.plugin

import moe.forpleuvoir.nebula.config.manager.ConfigManager

interface ConfigManagerPlugin {

    val manager: ConfigManager

    fun onInit()

    fun onSave()

    fun onForcedSave()

    fun onLoad()

}