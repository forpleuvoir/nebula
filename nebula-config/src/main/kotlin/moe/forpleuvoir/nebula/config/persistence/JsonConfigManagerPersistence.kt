package moe.forpleuvoir.nebula.config.persistence

import moe.forpleuvoir.nebula.common.api.ExperimentalApi
import moe.forpleuvoir.nebula.config.manager.ConfigManager
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.json.JsonParser
import moe.forpleuvoir.nebula.serialization.json.JsonSerializer.Companion.dumpAsJson

object JsonConfigManagerPersistence : ConfigManagerPersistence {
    override fun fileName(manager: ConfigManager): String {
        return "${manager.key}.json"
    }

    @OptIn(ExperimentalApi::class)
    override fun serializeObjectToString(serializeObject: SerializeObject): String {
        return serializeObject.dumpAsJson(true)
    }

    @OptIn(ExperimentalApi::class)
    override fun stringToSerializeObject(str: String): SerializeObject {
        return JsonParser.parse(str).asObject
    }

}

fun jsonPersistence(): JsonConfigManagerPersistence = JsonConfigManagerPersistence