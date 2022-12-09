package com.forpleuvoir.nebula.serialization.toml

import com.forpleuvoir.nebula.serialization.base.SerializeObject
import com.forpleuvoir.nebula.serialization.extensions.serializeObject
import org.tomlj.Toml
import org.tomlj.TomlParseResult

/**
 *

 * 项目名 nebula

 * 包名 com.forpleuvoir.nebula.serialization.toml

 * 文件名 TomlExtensions

 * 创建时间 2022/12/9 16:53

 * @author forpleuvoir

 */

fun String.parseToToml(): TomlParseResult {
	return Toml.parse(this)
}

fun TomlParseResult.toObject(): SerializeObject {
 	return serializeObject(this.entrySet())
}