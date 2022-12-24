package com.forpleuvoir.nebula.common.util

import java.io.File
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.JarURLConnection
import java.net.URL
import java.net.URLConnection
import java.net.URLDecoder
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile

//以下代码截取自 https://stackoverflow.com/questions/520328/can-you-find-all-classes-in-a-package-using-reflection
object ClassScanner {
	/**
	 * Private helper method
	 *
	 * @param directory
	 * The directory to start with
	 * @param packageName
	 * The package name to search for. Will be needed for getting the
	 * Class object.
	 * @param classes
	 * if a file isn't loaded but still is in the directory
	 * @throws ClassNotFoundException
	 */
	@Throws(ClassNotFoundException::class)
	private fun checkDirectory(
		directory: File, packageName: String,
		classes: ArrayList<Class<*>>
	) {
		var tmpDirectory: File
		if (directory.exists() && directory.isDirectory) {
			val files = directory.list()
			for (file: String in files!!) {
				if (file.endsWith(".class")) {
					try {
						classes.add(Class.forName("$packageName.${file.substring(0, file.length - 6)}"))
					} catch (e: NoClassDefFoundError) {
						// do nothing. this class hasn't been found by the
						// loader, and we don't care.
					}
				} else if ((File(directory, file).also { tmpDirectory = it }).isDirectory) {
					checkDirectory(tmpDirectory, "$packageName.$file", classes)
				}
			}
		}
	}


	/**
	 * Private helper method.
	 *
	 * @param connection
	 * the connection to the jar
	 * @param packageName
	 * the package name to search for
	 * @param classes
	 * the current ArrayList of all classes. This method will simply
	 * add new classes.
	 * @throws ClassNotFoundException
	 * if a file isn't loaded but still is in the jar file
	 * @throws IOException
	 * if it can't correctly read from the jar file.
	 */
	@Throws(ClassNotFoundException::class, IOException::class)
	private fun checkJarFile(
		connection: JarURLConnection,
		packageName: String, classes: ArrayList<Class<*>>
	) {
		val jarFile: JarFile = connection.jarFile
		val entries: Enumeration<JarEntry> = jarFile.entries()
		var name: String
		var jarEntry: JarEntry? = null
		while ((entries.hasMoreElements() && ((entries.nextElement().also { jarEntry = it }) != null))) {
			name = jarEntry!!.name
			if (name.contains(".class")) {
				name = name.substring(0, name.length - 6).replace('/', '.')
				if (name.contains(packageName)) {
					classes.add(Class.forName(name))
				}
			}
		}
	}

	/**
	 * Attempts to list all the classes in the specified package as determined
	 * by the context class loader
	 *
	 * @param packageName
	 * the package name to search
	 * @return a list of classes that exist within that package
	 * @throws ClassNotFoundException
	 * if something went wrong
	 */
	@Throws(ClassNotFoundException::class)
	fun getClassesForPackage(packageName: String): ArrayList<Class<*>> {
		val classes = ArrayList<Class<*>>()
		try {
			val cld = Thread.currentThread().contextClassLoader ?: throw ClassNotFoundException("Can't get class loader.")
			val resources = cld.getResources(packageName.replace('.', '/'))
			var connection: URLConnection
			var url: URL? = null
			while ((resources.hasMoreElements() && ((resources.nextElement().also { url = it }) != null))) {
				try {
					connection = url!!.openConnection()
					if (connection is JarURLConnection) {
						checkJarFile(connection, packageName, classes)
					} else if (connection.javaClass.canonicalName == "sun.net.www.protocol.file.FileURLConnection") {
						try {
							checkDirectory(File(URLDecoder.decode(url!!.path, "UTF-8")), packageName, classes)
						} catch (ex: UnsupportedEncodingException) {
							throw ClassNotFoundException(("$packageName does not appear to be a valid package (Unsupported encoding)"), ex)
						}
					} else {
						throw ClassNotFoundException("$packageName (${url!!.path}) does not appear to be a valid package")
					}
				} catch (ioException: IOException) {
					throw ClassNotFoundException(("IOException was thrown when trying to get all resources for $packageName"), ioException)
				}
			}
		} catch (ex: NullPointerException) {
			throw ClassNotFoundException(("$packageName does not appear to be a valid package (Null pointer exception)"), ex)
		} catch (ioException: IOException) {
			throw ClassNotFoundException(("IOException was thrown when trying to get all resources for $packageName"), ioException)
		}
		return classes
	}

}