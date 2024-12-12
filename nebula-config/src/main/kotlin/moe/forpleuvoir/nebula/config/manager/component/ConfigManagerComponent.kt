package moe.forpleuvoir.nebula.config.manager.component

interface ConfigManagerComponent {

    fun beginInit() = Unit

    fun finishInit() = Unit

    suspend fun onSave() = Unit

    suspend fun onForcedSave() = Unit

    suspend fun onLoad() = Unit

}