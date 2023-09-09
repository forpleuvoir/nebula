package moe.forpleuvoir.nebula.config

import moe.forpleuvoir.nebula.common.api.Initializable
import moe.forpleuvoir.nebula.common.api.Matchable
import moe.forpleuvoir.nebula.common.api.Notifiable
import moe.forpleuvoir.nebula.common.api.Resettable

/**
 * 最基础的配置

 * 项目名 nebula

 * 包名 moe.forpleuvoir.nebula.config

 * 文件名 Config

 * 创建时间 2022/11/30 13:53

 * @author forpleuvoir

 */
interface Config<V, C : Config<V, C>> : ConfigSerializable, Initializable, ConfigValue<V>, Resettable, Notifiable<C>, Matchable