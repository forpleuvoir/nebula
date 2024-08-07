package moe.forpleuvoir.nebula.config.manager.component

import moe.forpleuvoir.nebula.config.manager.ConfigManager
import moe.forpleuvoir.nebula.config.manager.ConfigManagerComponentScope
import moe.forpleuvoir.nebula.config.persistence.ConfigManagerPersistence
import moe.forpleuvoir.nebula.config.util.ConfigUtil
import java.nio.file.Path

class LocalConfig(
    val manager: () -> ConfigManager,
    val configPath: () -> Path,
    val persistence: () -> ConfigManagerPersistence
) : ConfigManagerComponent {

    override suspend fun onSave() {
        if (!manager().savable()) return
        ConfigUtil.run {
            val file = configFile(persistence().wrapFileName(manager().key), configPath())
            writeToFile(persistence().serializeToString(manager().serialization().asObject), file)
            manager().markSaved()
        }
    }

    override suspend fun onForcedSave() {
        ConfigUtil.run {
            val file = configFile(persistence().wrapFileName(manager().key), configPath())
            writeToFile(persistence().serializeToString(manager().serialization().asObject), file)
            manager().markSaved()
        }
    }

    override suspend fun onLoad() {
        ConfigUtil.run {
            val file = configFile(persistence().wrapFileName(manager().key), configPath())
            persistence().stringToSerialization(readFileToString(file)).apply {
                manager().deserialization(this)
            }
        }
    }

}

fun ConfigManager.localConfig(
    configPath: () -> Path,
    persistence: () -> ConfigManagerPersistence
) = LocalConfig({ this }, configPath, persistence)

fun ConfigManagerComponentScope.localConfig(
    configPath: () -> Path,
    persistence: () -> ConfigManagerPersistence
) = LocalConfig({ this.manager }, configPath, persistence).also { compose(it) }

fun ConfigManager.localConfig(
    configPath: Path,
    persistence: ConfigManagerPersistence
) = LocalConfig({ this }, { configPath }, { persistence })

fun ConfigManagerComponentScope.localConfig(
    configPath: Path,
    persistence: ConfigManagerPersistence
) = LocalConfig({ this.manager }, { configPath }, { persistence }).also { compose(it) }