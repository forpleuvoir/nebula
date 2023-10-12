package moe.forpleuvoir.nebula.config.manager

import java.util.*

interface AutoSaveConfigManager : ConfigManager {

	val starTime: Date

	val period: Long

	val saveAction: () -> Unit
		get() = this::saveAsync

	override fun init() {
		Timer("AutoSaveConfigManager[${this.key}]").schedule(
			object : TimerTask() {
				override fun run() {
					if (needSave)
						saveAction()
				}
			}, starTime, period
		)
	}
}