package moe.forpleuvoir.nebula.event.impl

import moe.forpleuvoir.nebula.event.Event
import moe.forpleuvoir.nebula.event.Registration

/**
 * [Event] 的 Array-backed 实现。
 *
 * ## 工作原理
 * 1. 使用 [linkedMapOf] 维护阶段名到监听器列表的映射，注册/注销仅修改该映射。
 * 2. 每次变更后调用 [rebuild]：先拓扑排序确定阶段顺序，再展平所有监听器为 [Array]，
 *    最后通过 [invokerFactory] 合成新的 [invoker] 并原子发布。
 * 3. [invoker] 以 [Volatile] 修饰，保证多线程下新 invoker 立即可见。
 *
 * ## 线程安全
 * - [lock] 对象保护所有写操作（[register]、[unregister]、[addPhaseOrdering]）。
 * - [rebuild] 在锁内完成数组重建和 volatile write，读端无锁直接读取 [invoker]。
 *
 * ## 阶段排序
 * 使用 Kahn 拓扑排序算法。零入度节点优先处理 declared phases，
 * 同层级中 dynamic phases 排在 declared phases 之后。
 * 当出现循环依赖时，未解析的阶段会被追加到结果末尾（静默降级，不抛异常）。
 *
 * @param T invoker 类型。
 * @param handlers 初始监听器数组，用作类型标记和初始值。
 * @param invokerFactory 将监听器数组合并为 invoker 的函数。
 * @param declaredPhases 声明的阶段集合，在拓扑排序中获得更高优先级。
 */
internal class ArrayBackedEvent<T : Any>(
    private var handlers: Array<T>,
    private val invokerFactory: (Array<T>) -> T,
    private val declaredPhases: Set<String> = emptySet(),
) : Event<T>() {
    private val arrayType: Class<Array<T>> = handlers.javaClass

    private val lock: Any = Any()
    private val registrations = linkedMapOf<String, MutableList<T>>()
    private val orderings = mutableMapOf<String, MutableSet<String>>()

    init {
        registrations[DEFAULT_PHASE] = mutableListOf()
        orderings[DEFAULT_PHASE] = mutableSetOf()
        rebuild()
    }

    override fun register(listener: T): Registration = register(DEFAULT_PHASE, listener)

    override fun register(phase: String, listener: T): Registration {
        synchronized(lock) {
            registrations.getOrPut(phase) { mutableListOf() } += listener
            orderings.putIfAbsent(phase, mutableSetOf())
            rebuild()
        }
        return Registration { unregister(phase, listener) }
    }

    private fun unregister(phase: String, listener: T) {
        synchronized(lock) {
            registrations[phase]?.remove(listener)
            rebuild()
        }
    }

    override fun addPhaseOrdering(first: String, second: String) {
        require(first != second) { "Tried to add a phase that depends on itself." }
        synchronized(lock) {
            orderings.getOrPut(first) { mutableSetOf() } += second
            orderings.putIfAbsent(second, mutableSetOf())
            rebuild()
        }
    }

    /**
     * 重建监听器数组和 invoker。
     * 步骤：拓扑排序 → 按序收集所有监听器 → 复制为新数组 → 调用 [invokerFactory] → 赋值 [invoker]。
     */
    private fun rebuild() {
        val sorted = topologicalSort()
        val list = mutableListOf<Any>()
        for (p in sorted) {
            registrations[p]?.let { list.addAll(it) }
        }
        handlers = java.util.Arrays.copyOf(list.toTypedArray(), list.size, arrayType) as Array<T>
        invoker = invokerFactory(handlers)
    }

    /**
     * Kahn 拓扑排序。
     *
     * - 所有零入度节点优先按 declared/dynamic 分类入队，declared 始终优先出队。
     * - 同层级内，首次见到某个阶段的顺序即为最终顺序（[linkedMapOf] 保持插入序）。
     * - 若产生循环依赖导致部分节点未解析，直接追加到结果末尾。
     *
     * @return 排序后的阶段列表。
     */
    private fun topologicalSort(): List<String> {
        val allPhases = orderings.keys + registrations.keys
        val inDegree = linkedMapOf<String, Int>()
        val graph = mutableMapOf<String, MutableList<String>>()
        for (p in allPhases) inDegree[p] = 0
        for ((before, afters) in orderings)
            for (after in afters) {
                graph.getOrPut(before) { mutableListOf() } += after
                inDegree[after] = (inDegree[after] ?: 0) + 1
            }

        val declared = ArrayDeque<String>()
        val dynamic = ArrayDeque<String>()
        for (p in inDegree.keys)
            if (inDegree[p] == 0) {
                if (p in declaredPhases) declared.addLast(p) else dynamic.addLast(p)
            }

        val result = mutableListOf<String>()
        while (declared.isNotEmpty() || dynamic.isNotEmpty()) {
            val p = if (declared.isNotEmpty()) declared.removeFirst() else dynamic.removeFirst()
            result.add(p)
            for (n in graph[p].orEmpty()) {
                inDegree[n] = inDegree[n]!! - 1
                if (inDegree[n]!! == 0) {
                    if (n in declaredPhases) declared.addLast(n) else dynamic.addLast(n)
                }
            }
        }
        if (result.size < inDegree.size)
            result.addAll(inDegree.keys - result.toSet())
        return result
    }

}
