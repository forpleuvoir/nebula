package moe.forpleuvoir.nebula.event

import moe.forpleuvoir.nebula.event.EventFactory.create
import moe.forpleuvoir.nebula.event.EventFactory.createWithPhases
import moe.forpleuvoir.nebula.event.impl.EventFactoryImpl
import moe.forpleuvoir.nebula.event.impl.EventFactoryImpl.verifyPhases

/**
 * [Event] 实例的工厂对象。
 *
 * 使用 [create] 创建一个仅含默认阶段的 Event，或使用 [createWithPhases]
 * 声明一组有序阶段。
 *
 * ## 基本使用
 * ```
 * val event = EventFactory.create<(String) -> Unit> { handlers ->
 *     { value -> handlers.forEach { it(value) } }
 * }
 * ```
 *
 * 也可提供无监听器时的默认值：
 * ```
 * val event = EventFactory.create(emptyInvoker = { /* no-op */ }) { handlers ->
 *     { value -> handlers.forEach { it(value) } }
 * }
 * ```
 *
 * ## 类型参数 T
 * T 通常是函数类型（如 `(String) -> Unit`）或函数式接口（fun interface）。
 * `initializer` 接收当前所有 listener 的数组，返回一个组合后的 invoker。
 * `handlers.forEach { it(...) }` 是最常见的遍历调用方式。
 *
 * 对于返回值累积的场景:
 * ```
 * val event = EventFactory.create<GreetCallback> { handlers ->
 *     GreetCallback { name, count ->
 *         var result = ""
 *         for (l in handlers) result = l.greet(name, count)
 *         result
 *     }
 * }
 * ```
 *
 * ## createWithPhases
 * 通过声明有序阶段列表，按参数传入顺序隐式添加阶段排序关系。
 * 注意 `Event.DEFAULT_PHASE` 必须显式包含在列表中：
 * ```
 * val event = EventFactory.createWithPhases("before", Event.DEFAULT_PHASE, "after") { handlers ->
 *     { -> handlers.forEach { it() } }
 * }
 * // 等价于：
 * // event.addPhaseOrdering("before", Event.DEFAULT_PHASE)
 * // event.addPhaseOrdering(Event.DEFAULT_PHASE, "after")
 * ```
 */
object EventFactory {

    /**
     * 创建一个仅含默认阶段的 Event。
     * @param invokerFactory 将 listener 数组合并为 invoker 的函数。
     * @return [Event] 实例。
     */
    inline fun <reified T : Any> create(noinline invokerFactory: (Array<T>) -> T): Event<T> =
        EventFactoryImpl.create(emptyArray<T>(), invokerFactory)


    /**
     * 创建一个 Event，在无监听器时返回 [emptyInvoker]，单监听器时直接返回该监听器。
     *
     * 与 [create] 的区别在于不需要总是遍历数组合成 invoker，适用于需要兜底行为的场景。
     *
     * @param emptyInvoker 无任何监听器注册时的默认 invoker。
     * @param invokerFactory 当有多个监听器时，将数组合并为 invoker 的函数。
     * @return [Event] 实例。
     */
    inline fun <reified T : Any> create(emptyInvoker: T, noinline invokerFactory: (Array<T>) -> T): Event<T> =
        create { listeners ->
            if (listeners.isEmpty()) emptyInvoker
            else if (listeners.size == 1) listeners[0]
            else invokerFactory(listeners)
        }

    /**
     * 创建带有声明阶段和默认阶段的 Event。
     * 参数顺序即为阶段间的排序顺序。[Event.DEFAULT_PHASE] 必须显式包含在 [defaultPhases] 中。
     *
     * 例如 `createWithPhases("a", Event.DEFAULT_PHASE, "b")` 等价于：
     * - 声明阶段：`"a"`，`Event.DEFAULT_PHASE`，`"b"`
     * - 排序：`"a" -> DEFAULT_PHASE -> "b"`
     *
     * @param defaultPhases 按执行顺序排列的阶段名列表（需包含 [Event.DEFAULT_PHASE]）。
     * @param initializer 将 listener 数组合并为 invoker 的函数。
     * @return [Event] 实例。
     * @throws IllegalStateException 如果未包含 [Event.DEFAULT_PHASE]。
     * @throws IllegalArgumentException 如果存在重复的阶段名。
     */
    inline fun <reified T : Any> createWithPhases(
        vararg defaultPhases: String,
        noinline initializer: (Array<T>) -> T
    ): Event<T> {
        verifyPhases(defaultPhases)
        val declared = defaultPhases.toSet()
        return EventFactoryImpl.create(emptyArray<T>(), initializer, declared).apply {
            for (i in 1..<defaultPhases.size) {
                addPhaseOrdering(defaultPhases[i - 1], defaultPhases[i])
            }
        }
    }

}
