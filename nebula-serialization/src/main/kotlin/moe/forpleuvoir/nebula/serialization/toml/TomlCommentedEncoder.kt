package moe.forpleuvoir.nebula.serialization.toml

import moe.forpleuvoir.nebula.serialization.base.SerializeArray
import moe.forpleuvoir.nebula.serialization.base.SerializeObject

abstract class TomlCommentedEncoder : TomlEncoder() {

    protected abstract fun getComment(path: String): String?

    override fun shouldExpand(obj: SerializeObject, parentPath: List<String>): Boolean {
        if (super.shouldExpand(obj, parentPath)) return true
        return hasCommentInSubPath(parentPath, obj)
    }

    private fun hasCommentInSubPath(prefix: List<String>, obj: SerializeObject): Boolean {
        for (key in obj.keys) {
            val subPath = prefix + key
            val subPathStr = subPath.joinToString(".")
            if (getComment(subPathStr) != null) return true

            val value = obj[key]
            if (value is SerializeObject && hasCommentInSubPath(subPath, value)) return true
            if (value is SerializeArray) {
                for (element in value) {
                    if (element is SerializeObject && hasCommentInSubPath(subPath, element)) return true
                }
            }
        }
        return false
    }

    override fun beforeEntry(path: List<String>, sb: StringBuilder) {
        emitComment(path.joinToString("."), sb)
    }

    override fun beforeTableHeader(path: List<String>, sb: StringBuilder) {
        emitComment(path.joinToString("."), sb)
    }

    override fun beforeArrayTableHeader(path: List<String>, sb: StringBuilder) {
        emitComment(path.joinToString("."), sb)
    }

    private fun emitComment(dottedPath: String, sb: StringBuilder) {
        getComment(dottedPath)?.let { comment ->
            if (sb.isNotEmpty() && !sb.endsWith("\n")) sb.appendLine()
            comment.lines().forEach { line ->
                sb.append("# ").append(line).appendLine()
            }
        }
    }
}
