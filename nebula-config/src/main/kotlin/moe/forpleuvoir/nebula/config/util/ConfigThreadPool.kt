package moe.forpleuvoir.nebula.config.util

import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

object ConfigThreadPool {

	private val threadNumber = AtomicInteger(0)

	private val executor by lazy {
		Executors.newCachedThreadPool {
			val thread = Executors.defaultThreadFactory().newThread(it)
			thread.name = "config-${threadNumber.getAndAdd(1)}"
			thread
		}
	}

	@JvmStatic
	fun execute(runnable: Runnable) {
		executor.execute(runnable)
	}

}