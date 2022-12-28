package com.forpleuvoir.nebula.config.impl

import com.forpleuvoir.nebula.config.ConfigManager
import java.util.*

interface AutoSaveConfigManager : ConfigManager {

	val starTime: Date

	val period: Long

	val saveAction: () -> Unit
		get() = this::saveAsync

	override fun init() {
		Timer().schedule(
			object : TimerTask() {
				override fun run() {
					if (needSave)
						saveAction()
				}
			}, starTime, period
		)
	}
}