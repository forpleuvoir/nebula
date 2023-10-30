package moe.forpleuvoir.nebula.config.manager

import moe.forpleuvoir.nebula.config.category.ConfigCategory
import kotlin.time.Duration

interface ConfigManager : ConfigCategory {

    /**
     * 所有对配置内容的操作都应该在此函数调用之后执行
     */
    override fun init()

    suspend fun save()

    /**
     *  在配置保存完成后执行
     * @param callback (duration: Duration) -> Unit  duration为保存耗时
     */
    fun onSaved(callback: (duration: Duration) -> Unit)

    fun saveAsync()

    suspend fun forceSave()

    fun forceSaveAsync()

    suspend fun load()

    /**
     * 在配置加载完成后执行
     * @param callback (duration: Duration) -> Unit  duration为加载耗时
     */
    fun onLoaded(callback: (duration: Duration) -> Unit)

    fun loadAsync()

}