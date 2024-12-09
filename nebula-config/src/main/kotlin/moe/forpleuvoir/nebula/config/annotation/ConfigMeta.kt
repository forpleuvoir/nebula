package moe.forpleuvoir.nebula.config.annotation

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfigMeta(
    val comment: String = EMPTY_COMMENT,
    val order: Int = DEFAULT_ORDER
) {

    companion object {

        const val DEFAULT_ORDER: Int = 0x114514

        const val EMPTY_COMMENT = ""

//        fun ConfigMeta.createConfigDescription(configSerializable: ConfigSerializable, descriptionKeyMap: (String) -> String = { "_$it" }): ConfigDescription? {
//            return this.description.takeIf { it.isNotEmpty() }?.let {
//                ConfigDescription(configSerializable, it, descriptionKeyMap)
//            }
//        }

        /**
         * `merge` 函数用来合并两个 `ConfigMeta` 实例的属性。
         * 此函数检查新 `ConfigMeta` 实例的`description` 和 `order` 是否为默认值。
         * 如果新实例的 `description` 不是空描述 (`EMPTY_DESC`)，则结果为新实例的 `description`，否则保留原实例的 `description`。
         * 如果新实例的 `order` 不是默认序 (`DEFAULT_ORDER`)，则结果为新实例的 `order`，否则保留原实例的 `order`。
         * 合并后，此函数将返回一个新的 `ConfigMeta` 实例。
         *
         * @param new 新的 `ConfigMeta` 实例，与当前实例合并
         * @return 一个新的 `ConfigMeta` 实例，其中包含了合并后的属性
         */
        fun ConfigMeta.merge(new: ConfigMeta): ConfigMeta {
            return ConfigMeta(
                if (new.comment != EMPTY_COMMENT) new.comment else this.comment,
                if (new.order != DEFAULT_ORDER) new.order else this.order,
            )
        }

    }

}
