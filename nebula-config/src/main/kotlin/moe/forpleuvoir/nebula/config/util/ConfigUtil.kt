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


    suspend fun writeStringToFile(string: String, file: File) =
        withContext(Dispatchers.IO) {
            var fileTmp = File(file.parentFile, file.name + ".tmp")
            if (fileTmp.exists()) {
                fileTmp = File(file.parentFile, UUID.randomUUID().toString() + ".tmp")
            }
            OutputStreamWriter(FileOutputStream(fileTmp), charset).use { it.write(string) }
            if (file.exists() && file.isFile && !file.delete()) {
                throw FileNotFoundException("Failed to delete file ${file.absolutePath}")
            }
            fileTmp.renameTo(file)
        }


    suspend fun readFileToString(file: File): String =
        withContext(Dispatchers.IO) {
            if (file.exists() && file.isFile && file.canRead()) {
                InputStreamReader(FileInputStream(file), charset).use { it.readText() }
            }
            throw IOException("Failed to read the file ${file.absolutePath}")
        }
}
