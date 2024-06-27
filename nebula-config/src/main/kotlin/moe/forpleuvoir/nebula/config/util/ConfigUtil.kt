package moe.forpleuvoir.nebula.config.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.*

object ConfigUtil {

    @JvmStatic
    var charset: Charset = StandardCharsets.UTF_8

    suspend fun configFile(configFileName: String, path: Path, create: Boolean = true): File =
        withContext(Dispatchers.IO) {
            File(path.toFile(), configFileName).apply {
                if (!this.exists() && create) {
                    if (!path.toFile().exists()) {
                        path.toFile().mkdir()
                    }
                    runCatching {
                        this.createNewFile()
                    }.onFailure {
                        throw IOException("${it.message},${this.absolutePath}")
                    }
                }
            }
        }


    suspend fun writeToFile(content: ByteArray, file: File) = withContext(Dispatchers.IO) {
        var fileTmp = File(file.parentFile, file.name + ".tmp")
        if (fileTmp.exists()) {
            fileTmp = File(file.parentFile, UUID.randomUUID().toString() + ".tmp")
        }
        fileTmp.outputStream().use {
            it.write(content)
        }
        if (file.exists() && file.isFile && !file.delete()) {
            throw IOException("Failed to delete file ${file.absolutePath}")
        }
        if (!fileTmp.renameTo(file)) {
            throw IOException("Failed to rename temp file to ${file.absolutePath}")
        }
    }

    suspend fun writeToFile(content: String, file: File) = withContext(Dispatchers.IO) {
        var fileTmp = File(file.parentFile, file.name + ".tmp")
        if (fileTmp.exists()) {
            fileTmp = File(file.parentFile, UUID.randomUUID().toString() + ".tmp")
        }
        OutputStreamWriter(fileTmp.outputStream(), charset).use { it.write(content) }
        if (file.exists() && file.isFile && !file.delete()) {
            throw IOException("Failed to delete file ${file.absolutePath}")
        }
        if (!fileTmp.renameTo(file)) {
            throw IOException("Failed to rename temp file to ${file.absolutePath}")
        }
    }


    suspend fun readFileToString(file: File): String = withContext(Dispatchers.IO) {
        if (file.exists() && file.isFile && file.canRead()) {
            InputStreamReader(file.inputStream(), charset).use { it.readText() }
        } else throw IOException("Failed to read the file ${file.absolutePath}")
    }

    suspend fun readFile(file: File): ByteArray = withContext(Dispatchers.IO) {
        if (file.exists() && file.isFile && file.canRead()) {
            file.inputStream().use(InputStream::readBytes)
        } else throw IOException("Failed to read the file ${file.absolutePath}")
    }
}
