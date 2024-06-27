package moe.forpleuvoir.nebula.config.persistence

import moe.forpleuvoir.nebula.common.api.ExperimentalApi
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.json.JsonParser
import moe.forpleuvoir.nebula.serialization.json.JsonSerializer.Companion.dumpAsJson

object JsonConfigManagerPersistence : ConfigManagerPersistence {

    override fun wrapFileName(fileName: String): String {
        return "$fileName.json"
    }

    @OptIn(ExperimentalApi::class)
    override fun serializeToString(serializeObject: SerializeElement): String {
        return serializeObject.dumpAsJson(true)
    }

    @OptIn(ExperimentalApi::class)
    override fun stringToSerialization(str: String): SerializeElement {
        return JsonParser.parse(str)
    }

}

@Suppress("NOTHING_TO_INLINE")
inline fun jsonPersistence(): JsonConfigManagerPersistence = JsonConfigManagerPersistence