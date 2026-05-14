package moe.forpleuvoir.nebula.event.impl

import moe.forpleuvoir.nebula.event.Event

/**
 * [moe.forpleuvoir.nebula.event.EventFactory] 的发布级内部实现。
 *
 * 标记为 [PublishedApi] 以允许 [moe.forpleuvoir.nebula.event.EventFactory] 内联函数访问。
 * 对外不可见，仅由 [moe.forpleuvoir.nebula.event.EventFactory] 调用。
 */
@PublishedApi
internal object EventFactoryImpl {

    /**
     * 创建 [ArrayBackedEvent] 实例。
     * @param handlers 初始监听器数组。
     * @param invokerFactory 将数组合并为 invoker 的函数。
     * @param declaredPhases 声明的阶段集合，用于拓扑排序时优先处理。
     */
    @PublishedApi
    internal fun <T : Any> create(
        handlers: Array<T>,
        invokerFactory: (Array<T>) -> T,
        declaredPhases: Set<String> = setOf(Event.DEFAULT_PHASE),
    ): Event<T> = ArrayBackedEvent(handlers, invokerFactory, declaredPhases)

    /**
     * 验证阶段声明：必须包含 [Event.DEFAULT_PHASE] 且无重复。
     * @throws IllegalStateException 缺少 DEFAULT_PHASE。
     * @throws IllegalArgumentException 存在重复阶段名。
     */
    @PublishedApi
    internal fun verifyPhases(defaultPhases: Array<out String>) {
        if (!defaultPhases.contains(Event.DEFAULT_PHASE))
            throw IllegalStateException("The event phases must contain Event.DEFAULT_PHASE.")
        for (i in defaultPhases.indices) {
            for (j in i + 1..<defaultPhases.size) {
                require(defaultPhases[i] != defaultPhases[j]) { "Duplicate event phase: " + defaultPhases[i] }
            }
        }
    }

}
