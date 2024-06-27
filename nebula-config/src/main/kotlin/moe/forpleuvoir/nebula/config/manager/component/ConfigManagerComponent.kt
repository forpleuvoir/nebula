package moe.forpleuvoir.nebula.config.manager.component

interface ConfigManagerComponent {

    fun beforeInit() = Unit

    fun afterInit() = Unit

    suspend fun onSave() = Unit

    suspend fun onForcedSave() = Unit

    suspend fun onLoad() = Unit

}