package moe.forpleuvoir.nebula.serialization.hjson

import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeObject

abstract class HJsonCommentedEncoder : HJsonEncoder() {

    protected abstract fun getComment(path: String): String?

    protected var currentPath: List<String> = emptyList()

    override fun encodeRootObject(obj: SerializeObject): String {
        currentPath = emptyList()
        return super.encodeRootObject(obj)
    }

    override fun encodeEntry(key: String, value: SerializeElement, indent: Int, spacing: String): String {
        val previousPath = currentPath
        currentPath = currentPath + key
        val pathStr = currentPath.joinToString(".")

        val sb = StringBuilder()
        getComment(pathStr)?.let { comment ->
            comment.lines().forEach { line ->
                sb.append(spacing).append("# ").append(line).append("\n")
            }
        }

        val entryLine = super.encodeEntry(key, value, indent, spacing)
        currentPath = previousPath

        return sb.append(entryLine).toString()
    }
}
