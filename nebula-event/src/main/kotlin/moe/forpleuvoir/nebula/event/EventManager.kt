package moe.forpleuvoir.nebula.event

import moe.forpleuvoir.nebula.common.api.Initializable
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.jvmErasure

abstract class EventManager : Initializable {

    private val events = LinkedHashSet<KClass<out Event>>()

    /**
     * 应该在应用初始化时就调用
     */
    override fun init() {
        register()
        subscribe()
    }

    /**
     * 返回所有需要扫描的类
     * 包括事件类，需要订阅事件的类
     * @param predicate Function1<KClass<*>, Boolean> 过滤器
     * @return Set<KClass<*>> 所有类
     */
    abstract fun scanPackage(predicate: (KClass<*>) -> Boolean = { true }): Set<KClass<*>>


    /**
     * 注册所有事件
     */
    @Suppress("UNCHECKED_CAST")
    protected open fun register() {
        scanPackage {
            it.isSubclassOf(Event::class)
        }.forEach { clazz ->
            events.add(clazz as KClass<out Event>)
        }
    }

    /**
     * 处理所有注解订阅的事件
     */
    @Suppress("UNCHECKED_CAST")
    protected open fun subscribe() {
        scanPackage { it.hasAnnotation<EventSubscriber>() }
            .forEach { clazz ->
                val eventSubscriber = clazz.findAnnotation<EventSubscriber>()!!
                EventBus[eventSubscriber.eventBus]?.let { eventBus ->
                    var instance = clazz.objectInstance
                    if (instance == null)
                        clazz.constructors.find {
                            it.parameters.size == 1
                        }?.let {
                            instance = it.call()
                        }
                    clazz.declaredFunctions
                        .filter { kFunction ->
                            kFunction.isAccessible = true
                            var returnValue = false
                            if (kFunction.hasAnnotation<Subscriber>()) {
                                if (kFunction.parameters.size == 2 && kFunction.parameters[0].type.jvmErasure == clazz) {
                                    returnValue = kFunction.parameters[1].type.jvmErasure.isSubclassOf(Event::class)
                                            || kFunction.parameters[1].type.jvmErasure == Event::class
                                } else if (kFunction.parameters.size == 1) {
                                    returnValue = kFunction.parameters[0].type.jvmErasure.isSubclassOf(Event::class)
                                            || kFunction.parameters[0].type.jvmErasure == Event::class
                                }
                            }
                            returnValue
                        }
                        .forEach { kFunction ->
                            kFunction.isAccessible = true
                            val parameter = kFunction.parameters.last()
                            val type = parameter.type.jvmErasure
                            val subscriber = kFunction.findAnnotation<Subscriber>()!!
                            eventBus.subscribe(type as KClass<out Event>, subscriber.greedy, subscriber.priority) { event ->
                                kFunction.javaMethod?.invoke(instance, event)
                            }
                        }

                }
            }
    }

    fun eventSet(): Set<KClass<out Event>> {
        return events
    }

    fun nameSet(): Set<String> {
        return LinkedHashSet<String>().apply {
            events.forEach { add(it.eventName) }
        }
    }

    fun byName(name: String): KClass<out Event>? {
        return events.find { it.eventName == name }
    }

}