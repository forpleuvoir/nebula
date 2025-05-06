package moe.forpleuvoir.nebula.config

import moe.forpleuvoir.nebula.common.api.Initializable
import moe.forpleuvoir.nebula.common.api.Matchable
import moe.forpleuvoir.nebula.common.api.Resettable
import moe.forpleuvoir.nebula.common.util.pathToRoot
import moe.forpleuvoir.nebula.config.container.ConfigContainer
import moe.forpleuvoir.nebula.config.manager.ConfigManager
import moe.forpleuvoir.nebula.serialization.Deserializable
import moe.forpleuvoir.nebula.serialization.Serializable

/**
 * 可序列化的配置
 */
interface ConfigSerializable : Initializable, Matchable, Resettable, Serializable, Deserializable {

    /**
     * 配置的键
     */
    val key: String

    var parentContainer: ConfigContainer?

    val configManager: () -> ConfigManager?

    operator fun component1(): String = key

    fun getUserData(key: String): Any?

    fun setUserData(key: String, value: Any)

}

//fun ConfigSerializable.pathToRoot(): List<ConfigSerializable> {
//    val path = mutableListOf<ConfigSerializable>()
//    var currentNode: ConfigSerializable? = this
//    while (currentNode != null) {
//        path.add(currentNode)
//        currentNode = currentNode.parentContainer
//    }
//    return path.reversed()
//}

fun ConfigSerializable.pathToRoot() = this.pathToRoot { it.parentContainer }

inline fun <R> ConfigSerializable.fold(initial: R, operation: (acc: R, ConfigSerializable) -> R) =
    pathToRoot().fold(initial, operation)

inline fun <R> ConfigSerializable.foldIndexed(initial: R, operation: (index: Int, acc: R, ConfigSerializable) -> R) =
    pathToRoot().foldIndexed(initial, operation)

inline fun <R> ConfigSerializable.foldRight(initial: R, operation: (ConfigSerializable, acc: R) -> R) =
    pathToRoot().foldRight(initial, operation)
