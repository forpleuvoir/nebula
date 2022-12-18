package com.forpleuvoir.nebula.config.item

import com.forpleuvoir.nebula.config.ConfigValue

interface ConfigMutableListValue<T> : ConfigValue<MutableList<T>>, MutableList<T> {


}