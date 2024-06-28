package moe.forpleuvoir.nebula.config.annotation

import moe.forpleuvoir.nebula.config.ConfigDescription
import moe.forpleuvoir.nebula.config.ConfigSerializable

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfigMeta(
    val description: String = "",
    val order: Int = 0
) {

    companion object {

        fun ConfigMeta.createConfigDescription(configSerializable: ConfigSerializable, descriptionKeyMap: (String) -> String = { "_$it" }): ConfigDescription? {
            return this.description.takeIf { it.isNotEmpty() }?.let {
                ConfigDescription(configSerializable, it, descriptionKeyMap)
            }
        }

    }

}
