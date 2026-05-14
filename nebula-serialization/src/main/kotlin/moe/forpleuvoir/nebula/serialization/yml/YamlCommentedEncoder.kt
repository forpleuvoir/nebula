package moe.forpleuvoir.nebula.serialization.yml

abstract class YamlCommentedEncoder : YamlEncoder() {

    protected abstract fun getComment(path: String): String?

    override fun beforeEntry(path: List<String>, sb: StringBuilder) {}

    override fun beforeEntry(path: List<String>, indent: Int, sb: StringBuilder) {
        emitComment(path.joinToString("."), indent, sb)
    }

    private fun emitComment(dottedPath: String, indent: Int, sb: StringBuilder) {
        getComment(dottedPath)?.let { comment ->
            if (indent in 1..sb.length) sb.delete(sb.length - indent, sb.length)
            if (sb.isNotEmpty() && !sb.endsWith("\n")) sb.appendLine()
            comment.lines().forEach { line ->
                if (indent > 0) sb.append(" ".repeat(indent))
                sb.append("# ").append(line).appendLine()
            }
            if (indent > 0) sb.append(" ".repeat(indent))
        }
    }
}
