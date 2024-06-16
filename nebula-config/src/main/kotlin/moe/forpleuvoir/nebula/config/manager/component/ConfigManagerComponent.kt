package moe.forpleuvoir.nebula.config.manager.component

interface ConfigManagerComponent {

    fun onInit() = Unit

    fun onSave() = Unit

    fun onForcedSave() = Unit

    fun onLoad() = Unit

}