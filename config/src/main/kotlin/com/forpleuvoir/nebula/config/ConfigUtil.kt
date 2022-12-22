package com.forpleuvoir.nebula.config

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
			file.createNewFile()
		}
		return file
	}

	fun writeStringToFile(string: String, file: File) {
		var fileTmp = File(file.parentFile, file.name + ".tmp")
		if (fileTmp.exists()) {
			fileTmp = File(file.parentFile, UUID.randomUUID().toString() + ".tmp")
		}
		try {
			OutputStreamWriter(FileOutputStream(fileTmp), StandardCharsets.UTF_8).use { writer ->
				writer.write(string)
				writer.close()
				if (file.exists() && file.isFile && !file.delete()) {
					throw FileNotFoundException("Failed to delete file ${file.absolutePath}")
				}
				fileTmp.renameTo(file)
			}
		} catch (e: Exception) {
			throw IOException("Failed to write string to file ${fileTmp.absolutePath}")
		}
	}

	fun readFileToString(file: File): String {
		if (file.exists() && file.isFile && file.canRead()) {
			val fileName = file.absolutePath
			try {
				InputStreamReader(FileInputStream(file), StandardCharsets.UTF_8).use { reader ->
					return reader.readText()
				}
			} catch (e: Exception) {
				throw IOException("Failed to read the file $fileName")
			}
		}
		throw IOException("Failed to read the file ${file.absolutePath}")
	}

}