package moe.forpleuvoir.nebula.config.manager

import moe.forpleuvoir.nebula.config.util.ConfigUtil
import java.nio.file.Path
import kotlin.time.measureTime

abstract class LocalConfigManager(
    key: String,
    autoScan: Boolean = true,
    descriptionKeyMap: (String) -> String = { "_$it" }
) : AbstractConfigManager(key, autoScan, descriptionKeyMap) {

    abstract val configPath: Path
    override suspend fun save() {
        onSaved.invoke(
            measureTime {
                if (!needSave) return
                ConfigUtil.run {
                    val file = configFile(fileName(key), configPath)
                    writeStringToFile(serializeObjectToString(serialization().asObject), file)
                    needSave = false
                }
            }
        )
    }

    override suspend fun forceSave() {
        onSaved.invoke(
            measureTime {
                ConfigUtil.run {
                    val file = configFile(fileName(key), configPath)
                    writeStringToFile(serializeObjectToString(serialization().asObject), file)
                    needSave = false
                }
            }
        )
    }

    override suspend fun load() {
        onLoaded.invoke(
            measureTime {
                ConfigUtil.run {
                    val file = configFile(fileName(key), configPath)
                    stringToSerializeObject(readFileToString(file)).apply {
                        deserialization(this)
                    }
                }
            }
        )
    }

}