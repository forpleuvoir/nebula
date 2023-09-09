package moe.forpleuvoir.nebula.config.item

import moe.forpleuvoir.nebula.config.ConfigValue

interface ConfigMutableListValue<T> : ConfigValue<MutableList<T>>, MutableList<T> {

	override val size: Int
		get() = getValue().size

	override fun contains(element: T): Boolean {
		return getValue().contains(element)
	}

	override fun containsAll(elements: Collection<T>): Boolean {
		return getValue().containsAll(elements)
	}

	override fun get(index: Int): T {
		return getValue()[index]
	}

	override fun indexOf(element: T): Int {
		return getValue().indexOf(element)
	}

	override fun isEmpty(): Boolean {
		return getValue().isEmpty()
	}

	override fun iterator(): MutableIterator<T> {
		return getValue().iterator()
	}

	override fun lastIndexOf(element: T): Int {
		return getValue().lastIndexOf(element)
	}

	override fun add(element: T): Boolean {
		return getValue().add(element)
	}

	override fun add(index: Int, element: T) {
		getValue().add(index, element)
	}

	override fun addAll(index: Int, elements: Collection<T>): Boolean {
		return getValue().addAll(index, elements)
	}

	override fun addAll(elements: Collection<T>): Boolean {
		return getValue().addAll(elements)
	}

	override fun clear() {
		getValue().clear()
	}

	override fun listIterator(): MutableListIterator<T> {
		return getValue().listIterator()
	}

	override fun listIterator(index: Int): MutableListIterator<T> {
		return getValue().listIterator(index)
	}

	override fun remove(element: T): Boolean {
		return getValue().remove(element)
	}

	override fun removeAll(elements: Collection<T>): Boolean {
		return getValue().removeAll(elements)
	}

	override fun removeAt(index: Int): T {
		return getValue().removeAt(index)
	}

	override fun retainAll(elements: Collection<T>): Boolean {
		return getValue().retainAll(elements)
	}

	override fun set(index: Int, element: T): T {
		return getValue().set(index, element)
	}

	override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> {
		return subList(fromIndex, toIndex)
	}
}