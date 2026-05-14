package moe.forpleuvoir.nebula.event

/**
 * 监听器注销句柄。
 *
 * 由 [Event.register] 返回，持有者可通过 [unregister] 精确移除对应的监听器。
 *
 * ## 示例
 * ```
 * val reg = event.register { println("hello") }
 * // 稍后注销
 * reg.unregister()
 *
 * // 或使用运算符重载
 * reg()
 * ```
 *
 * ## 注意
 * - 每个 [Registration] 对应一次 [Event.register] 调用，仅移除该次注册的监听器。
 * - 同一个 listener 注册到不同阶段会得到不同的 [Registration] 实例，需分别注销。
 * - 重复注销不会抛出异常（已注销的 listener 会被 rebuild 跳过）。
 */
fun interface Registration {

    /** 从事件中移除关联的监听器。 */
    fun unregister()

}

/** 运算符重载，等价于 [unregister]。 */
operator fun Registration.invoke() = unregister()
