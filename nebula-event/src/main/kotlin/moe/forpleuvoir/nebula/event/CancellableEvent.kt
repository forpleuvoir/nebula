package moe.forpleuvoir.nebula.event

interface CancellableEvent : Event {

	override val cancellable: Boolean
		get() = true

	override var canceled: Boolean

	override fun cancel() {
		if (!canceled) canceled = true
		else throw EventException("this event has been canceled")
	}

	fun isCanceled(block: Runnable) = if (canceled) block.run() else Unit

}