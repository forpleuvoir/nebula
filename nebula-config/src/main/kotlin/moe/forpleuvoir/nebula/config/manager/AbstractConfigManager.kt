package moe.forpleuvoir.nebula.config.manager

import moe.forpleuvoir.nebula.config.container.ConfigContainerImpl
import moe.forpleuvoir.nebula.config.persistence.ConfigManagerPersistence
import moe.forpleuvoir.nebula.config.util.configLaunch
import kotlin.time.Duration

abstract class AbstractConfigManager(
    key: String,
    autoScan: Boolean = true,
    descriptionKeyMap: (String) -> String = { "@$it" }
) : ConfigManager, ConfigContainerImpl(key, autoScan, descriptionKeyMap), ConfigManagerPersistence {

//    override var needSave: Boolean = false
//        get() {
//            //如果有任意一个配置需要保存，则返回true
//            return if (!allConfigSerializable().none { it::class.isSubclassOf(ConfigContainer::class) && (it as ConfigContainer).needSave }) true
//            else field
//        }
//        set(value) {
//            field = value
//            if (!value) {
//                allConfigSerializable().filter {
//                    it::class.isSubclassOf(ConfigContainer::class)
//                }.forEach {
//                    it as ConfigContainer
//                    it.needSave = false
//                }
//            }
//        }

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