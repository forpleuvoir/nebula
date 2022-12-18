package com.forpleuvoir.nebula.serialization.toml

import cc.ekblad.toml.decode
import cc.ekblad.toml.encodeToString
import cc.ekblad.toml.tomlMapper
import com.forpleuvoir.nebula.serialization.base.SerializeObject
import com.forpleuvoir.nebula.serialization.extensions.serializeObject
import com.forpleuvoir.nebula.serialization.extensions.toMap

/**
 *

 * 项目名 nebula

 * 包名 com.forpleuvoir.nebula.serialization.toml

 * 文件名 TomlExtensions

 * 创建时间 2022/12/9 16:53

 * @author forpleuvoir

 */

fun String.tomlStringToObject(): SerializeObject {
	val mapper = tomlMapper { }
	return serializeObject(mapper.decode<Map<String, Any>>(this))
}

fun SerializeObject.toTomlString(): String {
	return tomlMapper {}.encodeToString(this.toMap())
}