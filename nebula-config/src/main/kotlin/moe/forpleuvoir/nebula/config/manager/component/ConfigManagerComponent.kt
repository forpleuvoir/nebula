package moe.forpleuvoir.nebula.config.manager.component

interface ConfigManagerComponent {

    fun onInit() = Unit

    suspend fun onSave() = Unit

    suspend fun onForcedSave() = Unit

    suspend fun onLoad() = Unit

}