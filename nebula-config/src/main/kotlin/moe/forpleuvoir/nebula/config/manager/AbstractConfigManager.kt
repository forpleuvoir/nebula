package moe.forpleuvoir.nebula.config.manager

import moe.forpleuvoir.nebula.config.category.ConfigCategory
import moe.forpleuvoir.nebula.config.category.ConfigCategoryImpl
import moe.forpleuvoir.nebula.config.persistence.ConfigManagerPersistence
import moe.forpleuvoir.nebula.config.util.configLaunch
import kotlin.reflect.full.isSubclassOf
import kotlin.time.Duration

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
        configLaunch { save() }
    }

    override fun forceSaveAsync() {
        configLaunch { forceSave() }
    }

    override fun loadAsync() {
        configLaunch { load() }
    }

    override fun onSaved(callback: (duration: Duration) -> Unit) {
        onSaved = callback
    }

    override fun onLoaded(callback: (duration: Duration) -> Unit) {
        onLoaded = callback
    }

    protected var onSaved: (duration: Duration) -> Unit = {}

    protected var onLoaded: (duration: Duration) -> Unit = {}

}