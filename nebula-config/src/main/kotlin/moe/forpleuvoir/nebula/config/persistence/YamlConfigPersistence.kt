package moe.forpleuvoir.nebula.config.persistence

import moe.forpleuvoir.nebula.config.ConfigManager
import moe.forpleuvoir.nebula.config.comment
import moe.forpleuvoir.nebula.config.flat
import moe.forpleuvoir.nebula.config.path
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.yml.YamlCommentedEncoder
import moe.forpleuvoir.nebula.serialization.yml.YamlDialect

class YamlConfigPersistence(manager: ConfigManager) : ConfigPersistence {

    private val encoder = object : YamlCommentedEncoder() {
        private val comments: Map<String, String> by lazy {
            manager.flat
                .filter { it.comment != null }
                .associate { it.path to it.comment!! }
        }

        override fun getComment(path: String): String? = comments[path]
    }

    override fun wrapFileName(fileName: String): String = "$fileName.yml"

    override fun encode(data: SerializeElement): String = encoder.encode(data)

    override fun decode(input: String): Result<SerializeElement> = YamlDialect.decode(input)
}

context(manager: ConfigManager)
fun yaml(): YamlConfigPersistence = YamlConfigPersistence(manager)
