package moe.forpleuvoir.nebula.config.manager.component

interface ConfigManagerComponent {

    fun onInit()

    fun onSave()

    fun onForcedSave()

    fun onLoad()

}