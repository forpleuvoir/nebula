package moe.forpleuvoir.nebula.config.persistence

import moe.forpleuvoir.nebula.serialization.base.SerializeElement

interface ConfigPersistence {

    fun wrapFileName(fileName: String): String

    fun encode(data: SerializeElement): String

    fun decode(input: String): Result<SerializeElement>
}
