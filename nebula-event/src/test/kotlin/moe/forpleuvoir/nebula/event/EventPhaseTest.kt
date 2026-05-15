package moe.forpleuvoir.nebula.event

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class EventPhaseTest {

    @Test
    fun `register and invoke default phase`() {
        val results = mutableListOf<String>()
        val event = EventFactory.create<(String) -> Unit> { handlers ->
            { value -> handlers.forEach { it(value) } }
        }
        event.register { results.add("x:$it") }
        event()("a")
        assertEquals(listOf("x:a"), results)
    }

    @Test
    fun `register specific phases preserves insertion order within same phase`() {
        val results = mutableListOf<String>()
        val event = EventFactory.create<(String) -> Unit> { handlers ->
            { value -> handlers.forEach { it(value) } }
        }
        event.register("before") { results.add("b1:$it") }
        event.register("before") { results.add("b2:$it") }
        event.invoker()("x")
        assertEquals(listOf("b1:x", "b2:x"), results)
    }

    @Test
    fun `phase ordering`() {
        val results = mutableListOf<String>()
        val event = EventFactory.create<(String) -> Unit> { handlers ->
            { value -> handlers.forEach { it(value) } }
        }
        event.addPhaseOrdering("before", Event.DEFAULT_PHASE)
        event.addPhaseOrdering(Event.DEFAULT_PHASE, "after")
        event.register("after") { results.add("a:$it") }
        event.register(Event.DEFAULT_PHASE) { results.add("d:$it") }
        event.register("before") { results.add("b:$it") }
        event.invoker()("x")
        assertEquals(listOf("b:x", "d:x", "a:x"), results)
    }

    @Test
    fun `dynamic addPhaseOrdering after registration`() {
        val results = mutableListOf<String>()
        val event = EventFactory.create<(String) -> Unit> { handlers ->
            { value -> handlers.forEach { it(value) } }
        }
        event.register("y") { results.add("y:$it") }
        event.register("x") { results.add("x:$it") }
        event.addPhaseOrdering("x", "y")
        event.invoker()("v")
        assertEquals(listOf("x:v", "y:v"), results)
    }

    @Test
    fun `addPhaseOrdering invalidates cache`() {
        val results = mutableListOf<String>()
        val event = EventFactory.create<(String) -> Unit> { handlers ->
            { value -> handlers.forEach { it(value) } }
        }
        event.register("a") { results.add("a:$it") }
        event.register("b") { results.add("b:$it") }
        event.invoker()("1")
        event.addPhaseOrdering("a", "b")
        event.invoker()("2")
        assertEquals(listOf("a:1", "b:1", "a:2", "b:2"), results)
    }

    @Test
    fun `unregister removes listener`() {
        val results = mutableListOf<String>()
        val event = EventFactory.create<(String) -> Unit> { handlers ->
            { value -> handlers.forEach { it(value) } }
        }
        val reg = event.register { results.add("x:$it") }
        event.register { results.add("y:$it") }
        reg.unregister()
        event.invoker()("z")
        assertEquals(listOf("y:z"), results)
    }

    @Test
    fun `unregister from specific phase`() {
        val results = mutableListOf<String>()
        val event = EventFactory.create<(String) -> Unit> { handlers ->
            { value -> handlers.forEach { it(value) } }
        }
        val reg = event.register("a") { results.add("a:$it") }
        event.register("b") { results.add("b:$it") }
        reg.unregister()
        event.invoker()("x")
        assertEquals(listOf("b:x"), results)
    }

    @Test
    fun `cycle in phase ordering does not crash`() {
        val results = mutableListOf<String>()
        val event = EventFactory.createWithPhases<(String) -> Unit>("b", Event.DEFAULT_PHASE, "a") { handlers ->
            { value -> handlers.forEach { it(value) } }
        }
        event.register("a") { results.add("a:$it") }
        event.register("b") { results.add("b:$it") }
        println(results)
        event.register("cdasd") { results.add("cdasd:$it") }
        event.register("ggg") { results.add("ggg:$it") }
        event.register { results.add("${Event.DEFAULT_PHASE}:$it") }
        event.invoker()("x")
        println(results)
        results.clear()
        event.invoker()("盛大的")
        println(results)
//        assertTrue(results.contains("a:x"))
//        assertTrue(results.contains("b:x"))

    }

    @Test
    fun `custom phase not declared at init`() {
        val results = mutableListOf<String>()
        val event = EventFactory.create<(String) -> Unit> { handlers ->
            { value -> handlers.forEach { it(value) } }
        }
        event.register(Event.DEFAULT_PHASE) { results.add("d:$it") }
        event.register("custom") { results.add("c:$it") }
        event.addPhaseOrdering(Event.DEFAULT_PHASE, "custom")
        event.invoker()("x")
        assertEquals(listOf("d:x", "c:x"), results)
    }

    @Test
    fun `typed callback interface`() {
        val event = EventFactory.create<GreetCallback> { handlers ->
            GreetCallback { name, count ->
                var result = ""
                for (l in handlers) result = l.greet(name, count)
                result
            }
        }
        event.register(GreetCallback { name, count -> "hello $name x$count" })
        val result = event.invoker().greet("world", 3)
        assertEquals("hello world x3", result)
    }

    @Test
    fun `invoker cached across multiple calls`() {
        var buildCount = 0
        val event = EventFactory.create<(String) -> Unit> { handlers ->
            buildCount++
            { value -> handlers.forEach { it(value) } }
        }
        event.register { }
        event.invoker()("a")
        event.invoker()("b")
        // init rebuild(1) + register rebuild(1), invoker() doesn't trigger rebuild
        assertEquals(2, buildCount)
    }

    @Test
    fun `cache invalidated after register`() {
        var buildCount = 0
        val event = EventFactory.create<(String) -> Unit> { handlers ->
            buildCount++
            { value -> handlers.forEach { it(value) } }
        }
        event.invoker()("a")
        event.register { }
        event.invoker()("b")
        assertEquals(2, buildCount)
    }

    @Test
    fun `empty event no crash`() {
        val event = EventFactory.create<() -> Unit> { handlers ->
            { handlers.forEach { it() } }
        }
        event.invoker()()
    }

    @Test
    fun `self phase dependency throws`() {
        val event = EventFactory.create<() -> Unit> { handlers ->
            { handlers.forEach { it() } }
        }
        assertThrows(IllegalArgumentException::class.java) {
            event.addPhaseOrdering("a", "a")
        }
    }

    @Test
    fun `concurrent register does not deadlock`() {
        val results = mutableListOf<String>()
        val event = EventFactory.create<(String) -> Unit> { handlers ->
            { value -> handlers.forEach { it(value) } }
        }
        val threads = (1..10).map { i ->
            Thread {
                event.register("t$i") { results.add("$i:${it}") }
            }
        }
        threads.forEach { it.start() }
        threads.forEach { it.join() }
        event.invoker()("x")
        assertEquals(10, results.size)
    }

    @Test
    fun `concurrent register and invoke`() {
        val results = Collections.synchronizedList(mutableListOf<String>())
        val event = EventFactory.create<(String) -> Unit> { handlers ->
            { value -> handlers.forEach { it(value) } }
        }
        event.register { results.add("init:$it") }
        val threads = (1..20).map { i ->
            Thread {
                repeat(100) { j ->
                    val phase = "t${i}_$j"
                    event.register(phase) { results.add("$i:$it") }
                    event.invoker()("x")
                }
            }
        }
        threads.forEach { it.start() }
        threads.forEach { it.join() }
        assertTrue(results.size >= 2001) // 1 init + 20*100 per-phase invocations
    }

    @Test
    fun `dynamic phases after declared phases`() {
        val results = mutableListOf<String>()
        val event = EventFactory.createWithPhases("b", Event.DEFAULT_PHASE, "a") { handlers: Array<(String) -> Unit> ->
            { value -> handlers.forEach { it(value) } }
        }
        event.register("a") { results.add("a:$it") }
        event.register("b") { results.add("b:$it") }
        event.register("cdasd") { results.add("cdasd:$it") }
        event.invoker()("x")
        println(results)
        assertEquals(0, results.indexOf("b:x"))
        assertTrue(results.indexOf("cdasd:x") > results.indexOf("a:x"))
    }

    fun interface GreetCallback {
        fun greet(name: String, count: Int): String
    }

}
