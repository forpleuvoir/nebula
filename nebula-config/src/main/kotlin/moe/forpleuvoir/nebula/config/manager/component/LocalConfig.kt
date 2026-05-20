@file:Suppress("unused")

package moe.forpleuvoir.nebula.config.manager.component

import moe.forpleuvoir.nebula.config.ConfigManager
import moe.forpleuvoir.nebula.config.persistence.ConfigPersistence
import moe.forpleuvoir.nebula.config.util.ConfigUtil
import moe.forpleuvoir.nebula.serialization.DeserializationException
import java.nio.file.Path

class LocalConfig(
    val configPath: () -> Path,
    val persistence: () -> ConfigPersistence,
    override val manager: ConfigManager,
) : ConfigManagerComponent {

    override suspend fun onSave() {
        if (!manager.savable()) return
        onForcedSave()
    }

    override suspend fun onForcedSave() {
        val file = ConfigUtil.configFile(persistence().wrapFileName(manager.name), configPath())
        ConfigUtil.writeToFile(persistence().encode(manager.serialization()), file)
        manager.markSaved()
    }

    override suspend fun onLoad() {
        val file = ConfigUtil.configFile(persistence().wrapFileName(manager.name), configPath())
        persistence().decode(ConfigUtil.readFileToString(file))
            .onSuccess {
                manager.deserialization(it)
            }.onFailure {
                manager.exceptionHandler.onDeserializationException(manager, DeserializationException("Failed to load config file", it))
            }
    }
}

context(manager: ConfigManager)
fun localConfig(
    configPath: () -> Path,
    persistence: () -> ConfigPersistence,
) = LocalConfig(configPath, persistence, manager).also { manager.compose(it) }

context(manager: ConfigManager)
fun localConfig(
    configPath: Path,
    persistence: ConfigPersistence,
) = localConfig({ configPath }, { persistence })
