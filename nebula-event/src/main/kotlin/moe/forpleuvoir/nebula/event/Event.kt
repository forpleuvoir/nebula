package moe.forpleuvoir.nebula.event

import moe.forpleuvoir.nebula.event.Event.Companion.DEFAULT_PHASE


/**
 * 事件的抽象基类。
 *
 * 通过 [EventFactory] 创建实例。默认实现为 [moe.forpleuvoir.nebula.event.impl.ArrayBackedEvent]，
 * 所有 `listener` 以 [Array] 形式缓存，每次注册/注销后重建并原子发布。
 *
 * 使用方式：
 * ```
 * // 1. 定义事件
 * val event = EventFactory.create<(String) -> Unit> { handlers ->
 *     { value -> handlers.forEach { it(value) } }
 * }
 *
 * // 2. 注册监听器
 * val reg = event.register { println(it) }
 *
 * // 3. 触发事件
 * event.invoker()("hello")
 *
 * // 4. 注销
 * reg.unregister()
 * ```
 *
 * ## 阶段 (Phase)
 * 监听器可以注册到不同阶段以实现有序执行。阶段间通过 [addPhaseOrdering] 声明偏序关系，
 * 内部使用拓扑排序确定最终执行顺序。
 *
 * ```
 * event.addPhaseOrdering("before", Event.DEFAULT_PHASE)
 * event.addPhaseOrdering(Event.DEFAULT_PHASE, "after")
 * event.register("before") { println("first") }
 * event.register("after")  { println("last") }
 * event.register { println("middle") }
 * ```
 *
 * ## 实现说明
 * 默认实现 [moe.forpleuvoir.nebula.event.impl.ArrayBackedEvent] 使用 Kahn 拓扑排序，
 * 写操作由锁保护，[invoker] 以 [Volatile] 保证读端可见性。详细原理参考该类的文档。
 *
 * @param T invoker 类型，通常为函数类型或函数式接口。
 */
abstract class Event<T : Any> {

    companion object {
        /** 默认阶段标识，所有 Event 自动拥有该阶段。 */
        const val DEFAULT_PHASE: String = "nebula:default"
    }

    /**
     * 当前 invoker 实例。
     */
    @Volatile
    protected lateinit var invoker: T

    /** 返回当前 invoker 实例。 */
    fun invoker(): T = invoker

    /**
     * 注册监听器到 [DEFAULT_PHASE] 阶段。
     * @param listener 监听器实例。
     * @return [Registration] 用于注销该监听器。
     */
    abstract fun register(listener: T): Registration

    /**
     * 注册监听器到指定阶段。
     * @param phase 阶段名称，可通过 [addPhaseOrdering] 参与排序。
     * @param listener 监听器实例。
     * @return [Registration] 用于注销该监听器。
     */
    abstract fun register(phase: String, listener: T): Registration

    /**
     * 声明阶段偏序关系。
     * 调用后 [first] 阶段的监听器将在 [second] 阶段的监听器之前执行。
     * 可多次调用动态构建排序图。
     * @param first 在前执行的阶段。
     * @param second 在后执行的阶段。
     */
    abstract fun addPhaseOrdering(first: String, second: String)

}

/** 运算符重载，等价于 [invoker]。 */
operator fun <T : Any> Event<T>.invoke(): T = this.invoker()
