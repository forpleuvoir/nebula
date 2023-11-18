package moe.forpleuvoir.nebula.common.util

import moe.forpleuvoir.nebula.common.api.Notifiable
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.function.UnaryOperator
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

fun <T> notifiableList(callback: Consumer<NotifiableArrayList<T>>): NotifiableArrayList<T> {
    return NotifiableArrayList(callback)
}

fun <T> notifiableList(list: Collection<T>): NotifiableArrayList<T> {
    return NotifiableArrayList(list)
}

fun <T> notifiableList(vararg elements: T): NotifiableArrayList<T> {
    return NotifiableArrayList(*elements)
}

fun <T> List<T>.notification(): NotifiableArrayList<T> {
    return NotifiableArrayList(this)
}


class NotifiableArrayList<T>() : ArrayList<T>(), Notifiable<NotifiableArrayList<T>> {

    constructor(callback: Consumer<NotifiableArrayList<T>>) : this() {
        this.subscribers.add(callback)
    }

    constructor(list: Collection<T>) : this() {
        disableNotify {
            super.addAll(list)
        }
    }

    constructor(vararg elements: T) : this() {
        disableNotify {
            addAll(elements)
        }
    }

    var enableNotify = true

    @OptIn(ExperimentalContracts::class)
    inline fun disableNotify(action: () -> Unit) {
        contract {
            callsInPlace(action, InvocationKind.EXACTLY_ONCE)
        }
        val temp = enableNotify
        enableNotify = false
        action()
        enableNotify = temp
    }

    private val subscribers: MutableList<Consumer<NotifiableArrayList<T>>> = ArrayList()

    override fun onChange(value: NotifiableArrayList<T>) {
        if (!enableNotify) return
        for (subscriber in subscribers) {
            subscriber.accept(value)
        }
    }

    override fun subscribe(callback: Consumer<NotifiableArrayList<T>>) {
        subscribers.add(callback)
    }

    override fun add(element: T): Boolean {
        if (super.add(element)) {
            onChange(this)
            return true
        }
        return false
    }

    override fun add(index: Int, element: T) {
        super.add(index, element)
        onChange(this)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        if (super.addAll(elements)) {
            onChange(this)
            return true
        }
        return false
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        if (super.addAll(index, elements)) {
            onChange(this)
            return true
        }
        return false
    }

    override fun clear() {
        super.clear()
        onChange(this)
    }


    override fun remove(element: T): Boolean {
        if (super.remove(element)) {
            onChange(this)
            return true
        }
        return false
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        if (super.removeAll(elements.toSet())) {
            onChange(this)
            return true
        }
        return false
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        if (super.retainAll(elements.toSet())) {
            onChange(this)
            return true
        }
        return false
    }

    override fun removeIf(filter: Predicate<in T>): Boolean {
        if (super.removeIf(filter)) {
            onChange(this)
            return true
        }
        return false
    }

    override fun removeAt(index: Int): T {
        val element = super.removeAt(index)
        if (element != null) {
            onChange(this)
        }
        return element
    }

    override fun replaceAll(operator: UnaryOperator<T>) {
        super.replaceAll(operator)
        onChange(this)
    }

    override fun sort(c: Comparator<in T>?) {
        super.sort(c)
        onChange(this)
    }
}