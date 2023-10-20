package moe.forpleuvoir.nebula.config.manager

import moe.forpleuvoir.nebula.config.category.ConfigCategory
import kotlin.time.Duration

interface ConfigManager : ConfigCategory {

    fun save()

    /**
     *  在配置保存完成后执行
     * @param callback (duration: Duration) -> Unit  duration为保存耗时
     */
    fun onSaved(callback: (duration: Duration) -> Unit)

    fun saveAsync()

    fun forceSave()

    fun forceSaveAsync()

    fun load()

    /**
     * 在配置加载完成后执行
     * @param callback (duration: Duration) -> Unit  duration为加载耗时
     */
    fun onLoaded(callback: (duration: Duration) -> Unit)

    fun loadAsync()

}