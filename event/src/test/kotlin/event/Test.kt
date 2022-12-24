package event

import com.forpleuvoir.nebula.event.*
import kotlin.reflect.KClass

fun main() {
	TestEvents.init()
	EventBus.broadcast(TestEvent("asdas"))
}

class TestEvent(val a: String) : CancellableEvent {
	override var canceled: Boolean = false
}


object TestEvents : EventManager() {
	override fun scanPackage(predicate: (KClass<*>) -> Boolean): Set<KClass<*>> {
		return setOf(Test1::class, JavaTestEvent::class)
	}
}

@EventSubscriber
object Test1 {

	@Subscriber
	fun test(event: TestEvent) {
		println(event.a)
	}

}