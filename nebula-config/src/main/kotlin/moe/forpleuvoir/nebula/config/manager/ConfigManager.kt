package moe.forpleuvoir.nebula.config.manager

import moe.forpleuvoir.nebula.config.category.ConfigCategory

interface ConfigManager : ConfigCategory {

    fun save()

    /**
     * 在配置保存完成后执行
     * time为保存时间,单位ns
     */
    fun onSaved(callback: (time: Long) -> Unit)

    fun saveAsync()

    fun forceSave()

    fun forceSaveAsync()

    fun load()

    /**
     * 在配置加载完成后执行
     * time为保存时间,单位ns
     */
    fun onLoaded(callback: (time: Long) -> Unit)

    fun loadAsync()

}