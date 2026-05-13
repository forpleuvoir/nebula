package moe.forpleuvoir.nebula.config.persistence

import moe.forpleuvoir.nebula.config.ConfigManager
import moe.forpleuvoir.nebula.config.comment
import moe.forpleuvoir.nebula.config.flat
import moe.forpleuvoir.nebula.config.path
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.toml.TomlCommentedEncoder
import moe.forpleuvoir.nebula.serialization.toml.TomlDialect

class TomlConfigPersistence(manager: ConfigManager) : ConfigPersistence {

    private val encoder = object : TomlCommentedEncoder() {
        private val comments: Map<String, String> by lazy {
            manager.flat
                .filter { it.comment != null }
                .associate { it.path to it.comment!! }
        }

        override fun getComment(path: String): String? = comments[path]

    }

    override fun wrapFileName(fileName: String): String = "$fileName.toml"
    override fun encode(data: SerializeElement): String = encoder.encode(data)

    override fun decode(input: String): Result<SerializeElement> = TomlDialect.decode(input)

}

context(manager: ConfigManager)
fun toml(): TomlConfigPersistence = TomlConfigPersistence(manager)