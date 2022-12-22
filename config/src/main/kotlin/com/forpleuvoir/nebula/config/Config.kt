package com.forpleuvoir.nebula.config

import com.forpleuvoir.nebula.common.api.Initializable
import com.forpleuvoir.nebula.common.api.Matchable
import com.forpleuvoir.nebula.common.api.Notifiable
import com.forpleuvoir.nebula.common.api.Resettable

/**
 * 最基础的配置

 * 项目名 nebula

 * 包名 com.forpleuvoir.nebula.config

 * 文件名 Config

 * 创建时间 2022/11/30 13:53

 * @author forpleuvoir

 */
interface Config<V, C : Config<V, C>> : ConfigSerializable, Initializable, ConfigValue<V>, Resettable, Notifiable<C>, Matchable