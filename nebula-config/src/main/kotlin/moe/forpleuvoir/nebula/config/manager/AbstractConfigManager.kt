package moe.forpleuvoir.nebula.config.manager

import moe.forpleuvoir.nebula.config.category.ConfigCategory
import moe.forpleuvoir.nebula.config.category.ConfigCategoryImpl
import moe.forpleuvoir.nebula.config.persistence.ConfigManagerPersistence
import moe.forpleuvoir.nebula.config.util.ConfigThreadPool
import kotlin.reflect.full.isSubclassOf

abstract class AbstractConfigManager(key: String) : ConfigManager, ConfigCategoryImpl(key), ConfigManagerPersistence {

    override var needSave: Boolean = false
        get() {
            return if (!allConfigSerializable().none { it::class.isSubclassOf(ConfigCategory::class) && (it as ConfigCategory).needSave }) true
            else field
        }
        set(value) {
            field = value
            if (!value) {
                allConfigSerializable().filter {
                    it::class.isSubclassOf(ConfigCategory::class)
                }.forEach {
                    it as ConfigCategory
                    it.needSave = false
                }
            }
        }

    override fun saveAsync() {
        ConfigThreadPool.execute(::save)
    }

    override fun forceSaveAsync() {
        ConfigThreadPool.execute(::forceSave)
    }

    override fun loadAsync() {
        ConfigThreadPool.execute(::load)
    }

    override fun onSaved(callback: (time: Long) -> Unit) {
        onSaved = callback
    }

    override fun onLoaded(callback: (time: Long) -> Unit) {
        onLoaded = callback
    }

    protected var onSaved: (time: Long) -> Unit = {}

    protected var onLoaded: (time: Long) -> Unit = {}

}