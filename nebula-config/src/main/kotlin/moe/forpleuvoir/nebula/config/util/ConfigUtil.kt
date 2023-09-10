package moe.forpleuvoir.nebula.config.util

import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.*

object ConfigUtil {

    fun configFile(configFileName: String, path: Path, create: Boolean = true): File {
        val file = File(path.toFile(), configFileName)
        if (!file.exists() && create) {
            if (!path.toFile().exists()) {
                path.toFile().mkdir()
            }
            runCatching {
                file.createNewFile()
            }.onFailure {
                throw IOException("${it.message},${file.absolutePath}")
            }
        }
        return file
    }

    fun writeStringToFile(string: String, file: File) {
        var fileTmp = File(file.parentFile, file.name + ".tmp")
        if (fileTmp.exists()) {
            fileTmp = File(file.parentFile, UUID.randomUUID().toString() + ".tmp")
        }
        OutputStreamWriter(FileOutputStream(fileTmp), StandardCharsets.UTF_8).use { it.write(string) }
        if (file.exists() && file.isFile && !file.delete()) {
            throw FileNotFoundException("Failed to delete file ${file.absolutePath}")
        }
        fileTmp.renameTo(file)
    }

    fun readFileToString(file: File): String {
        if (file.exists() && file.isFile && file.canRead()) {
            return InputStreamReader(FileInputStream(file), StandardCharsets.UTF_8).use { it.readText() }
        }
        throw IOException("Failed to read the file ${file.absolutePath}")
    }

}