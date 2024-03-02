package moe.forpleuvoir.nebula.config.manager.plugin

import moe.forpleuvoir.nebula.config.manager.ConfigManager
import moe.forpleuvoir.nebula.config.manager.ConfigManagerPluginContext
import moe.forpleuvoir.nebula.config.persistence.ConfigManagerPersistence
import moe.forpleuvoir.nebula.config.util.ConfigUtil
import java.nio.file.Path
import kotlin.time.measureTime

class LocalConfig(
    override val manager: ConfigManager,
    val configPath: Path,
    private val persistence: ConfigManagerPersistence
) : ConfigManagerPlugin {

    override fun onInit() = Unit
    override fun onSave() {
        if (!manager.needSave) return
        ConfigUtil.run {
            val file = configFile(persistence.fileName(manager), configPath)
            writeStringToFile(persistence.serializeObjectToString(manager.serialization().asObject), file)
            manager.needSave = false
        }
    }

    override fun onForcedSave() {
        ConfigUtil.run {
            val file = configFile(persistence.fileName(manager), configPath)
            writeStringToFile(persistence.serializeObjectToString(manager.serialization().asObject), file)
            manager.needSave = false
        }
    }

    override fun onLoad() {
        ConfigUtil.run {
            val file = configFile(persistence.fileName(manager), configPath)
            persistence.stringToSerializeObject(readFileToString(file)).apply {
                manager.deserialization(this)
            }
        }
    }

}

fun ConfigManager.localConfig(
    configPath: Path,
    persistence: ConfigManagerPersistence
) = LocalConfig(this, configPath, persistence)

fun ConfigManagerPluginContext.localConfig(
    configPath: Path,
    persistence: ConfigManagerPersistence
) = LocalConfig(this.manager, configPath, persistence).also { plugin(it) }