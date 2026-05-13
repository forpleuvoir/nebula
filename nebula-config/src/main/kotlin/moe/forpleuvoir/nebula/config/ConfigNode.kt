@file:Suppress("unused")

package moe.forpleuvoir.nebula.config

import moe.forpleuvoir.nebula.common.api.Initializable
import moe.forpleuvoir.nebula.common.api.Matchable
import moe.forpleuvoir.nebula.serialization.Serde

interface ConfigNode : Initializable, Matchable, Serde {

    val name: String

    var parent: ConfigGroup?

    val root: ConfigManager?
        get() = parent?.root

    fun getMetadata(key: String): Any?

    fun setMetadata(key: String, value: Any)

}

val ConfigNode.isRoot: Boolean
    get() = parent == null && this is ConfigManager

fun ConfigNode.pathToRoot(): List<ConfigNode> {
    val path = mutableListOf<ConfigNode>()
    var current: ConfigNode? = this
    while (current != null) {
        path.add(current)
        current = current.parent
    }
    return path.reversed()
}

val ConfigNode.path: String
    get() {
        if (isRoot) return ""
        val p = parent ?: return name
        return if (p.isRoot) name else "${p.path}.$name"
    }

val ConfigNode.pathWithRoot: String
    get() = if (isRoot) root!!.name else root?.let { "${it.name}:$path" } ?: path

val ConfigNode.comment: String?
    get() = getMetadata("comment") as? String

fun ConfigNode.setComment(comment: String) {
    setMetadata("comment", comment)
}

@Suppress("NOTHING_TO_INLINE")
inline fun <T : ConfigNode> T.comment(comment: String): T = apply { setComment(comment) }
