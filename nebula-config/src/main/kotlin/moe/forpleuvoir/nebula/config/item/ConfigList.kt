@file:Suppress("unused")

package moe.forpleuvoir.nebula.config.item

import kotlinx.serialization.KSerializer
import moe.forpleuvoir.nebula.common.util.checkType
import moe.forpleuvoir.nebula.config.Config
import moe.forpleuvoir.nebula.config.ConfigGroup
import moe.forpleuvoir.nebula.config.ConfigSerde
import moe.forpleuvoir.nebula.serialization.base.SerializeArray
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.codec.Codec
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.reflect.KClass

class ConfigList<T : Any>(
    name: String,
    defaultValue: List<T>,
    private val serde: ConfigSerde<T>,
) : Config<List<T>>(name, defaultValue), MutableList<T> {

    private val buffer: MutableList<T> = CopyOnWriteArrayList(defaultValue)

    override var configValue: List<T>
        get() = buffer
        set(value) {
            setValue(value)
        }

    override val valueType: KClass<*> = MutableList::class

    override fun getValue(): List<T> = buffer.toList()

    override fun setValue(value: List<T>) {
        if (buffer valueNotEquals value) {
            buffer.clear()
            buffer.addAll(value)
            notifyChange()
        }
    }

    override fun isDefault(): Boolean = buffer valueEquals defaultValue

    override fun resetDefault() {
        if (isDefault()) return
        buffer.clear()
        buffer.addAll(defaultValue)
        notifyChange()
    }

    override fun serialization(): SerializeElement = SerializeArray().apply {
        buffer.forEach { add(serde.encode(it)) }
    }

    override fun deserialization(data: SerializeElement) {
        data.checkType<SerializeArray, Unit> { arr ->
            setValue(arr.mapNotNull { serde.decode(it).getOrNull() })
        }
    }

    override val size: Int get() = buffer.size
    override fun isEmpty(): Boolean = buffer.isEmpty()
    override fun contains(element: T): Boolean = buffer.contains(element)
    override fun containsAll(elements: Collection<T>): Boolean = buffer.containsAll(elements)
    override fun get(index: Int): T = buffer[index]
    override fun indexOf(element: T): Int = buffer.indexOf(element)
    override fun lastIndexOf(element: T): Int = buffer.lastIndexOf(element)
    override fun iterator(): MutableIterator<T> = object : MutableIterator<T> {
        val it = buffer.iterator()
        override fun hasNext() = it.hasNext()
        override fun next() = it.next()
        override fun remove() {
            it.remove(); notifyChange()
        }
    }

    override fun listIterator(): MutableListIterator<T> = object : MutableListIterator<T> {
        val it = buffer.listIterator()
        override fun hasNext() = it.hasNext()
        override fun hasPrevious() = it.hasPrevious()
        override fun next() = it.next()
        override fun nextIndex() = it.nextIndex()
        override fun previous() = it.previous()
        override fun previousIndex() = it.previousIndex()
        override fun add(element: T) {
            it.add(element); notifyChange()
        }

        override fun remove() {
            it.remove(); notifyChange()
        }

        override fun set(element: T) {
            it.set(element); notifyChange()
        }
    }

    override fun listIterator(index: Int): MutableListIterator<T> = object : MutableListIterator<T> {
        val it = buffer.listIterator(index)
        override fun hasNext() = it.hasNext()
        override fun hasPrevious() = it.hasPrevious()
        override fun next() = it.next()
        override fun nextIndex() = it.nextIndex()
        override fun previous() = it.previous()
        override fun previousIndex() = it.previousIndex()
        override fun add(element: T) {
            it.add(element); notifyChange()
        }

        override fun remove() {
            it.remove(); notifyChange()
        }

        override fun set(element: T) {
            it.set(element); notifyChange()
        }
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
        val sub = buffer.subList(fromIndex, toIndex)
        return object : MutableList<T> by sub {
            override fun add(element: T): Boolean = sub.add(element).also { if (it) notifyChange() }
            override fun add(index: Int, element: T) {
                sub.add(index, element); notifyChange()
            }

            override fun addAll(elements: Collection<T>): Boolean = sub.addAll(elements).also { if (it) notifyChange() }
            override fun addAll(index: Int, elements: Collection<T>): Boolean = sub.addAll(index, elements).also { if (it) notifyChange() }
            override fun clear() {
                sub.clear(); notifyChange()
            }

            override fun remove(element: T): Boolean = sub.remove(element).also { if (it) notifyChange() }
            override fun removeAll(elements: Collection<T>): Boolean = sub.removeAll(elements).also { if (it) notifyChange() }
            override fun removeAt(index: Int): T = sub.removeAt(index).also { notifyChange() }
            override fun retainAll(elements: Collection<T>): Boolean = sub.retainAll(elements).also { if (it) notifyChange() }
            override fun set(index: Int, element: T): T = sub.set(index, element).also { notifyChange() }
        }
    }

    override fun add(element: T): Boolean = buffer.add(element).also { if (it) notifyChange() }
    override fun add(index: Int, element: T) {
        buffer.add(index, element); notifyChange()
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean = buffer.addAll(index, elements).also { if (it) notifyChange() }
    override fun addAll(elements: Collection<T>): Boolean = buffer.addAll(elements).also { if (it) notifyChange() }
    override fun clear() {
        buffer.clear(); notifyChange()
    }

    override fun remove(element: T): Boolean = buffer.remove(element).also { if (it) notifyChange() }
    override fun removeAll(elements: Collection<T>): Boolean = buffer.removeAll(elements).also { if (it) notifyChange() }
    override fun removeAt(index: Int): T = buffer.removeAt(index).also { notifyChange() }
    override fun retainAll(elements: Collection<T>): Boolean = buffer.retainAll(elements).also { if (it) notifyChange() }
    override fun set(index: Int, element: T): T = buffer.set(index, element).also { notifyChange() }
}

context(group: ConfigGroup)
fun <T : Any> configList(name: String, defaultValue: List<T>, serde: ConfigSerde<T>): ConfigList<T> =
    group.addConfig(ConfigList(name, defaultValue, serde))

context(group: ConfigGroup)
fun <T : Any> configList(name: String, defaultValue: List<T>, codec: Codec<T>): ConfigList<T> =
    configList(name, defaultValue, ConfigSerde.of(codec))

context(group: ConfigGroup)
fun <T : Any> configList(name: String, defaultValue: List<T>, serializer: KSerializer<T>): ConfigList<T> =
    configList(name, defaultValue, ConfigSerde.of(serializer))