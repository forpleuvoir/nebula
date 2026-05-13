@file:Suppress("unused")

package moe.forpleuvoir.nebula.config.persistence

import moe.forpleuvoir.nebula.config.ConfigManager
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.json.JsonDialect

data class JsonConfigPersistence(private val dialect: JsonDialect = JsonDialect()) : ConfigPersistence {
    companion object : ConfigPersistence by JsonConfigPersistence()

    override fun wrapFileName(fileName: String): String = "$fileName.json"

    override fun encode(data: SerializeElement): String = dialect.encode(data)

    override fun decode(input: String): Result<SerializeElement> = dialect.decode(input)
}

@Suppress("CAST_NEVER_SUCCEEDS")
context(manager: ConfigManager)
fun json(): ConfigPersistence = JsonConfigPersistence

