package moe.forpleuvoir.nebula.serialization.extensions

import moe.forpleuvoir.nebula.serialization.base.*


infix fun SerializeElement.completeEquals(target: SerializeElement): Boolean {
    if (this.hashCode() != target.hashCode() || this.javaClass != target.javaClass) return false
    if (this is SerializePrimitive) {
        return this == target
    } else if (this is SerializeNull) {
        return true
    } else if (this is SerializeArray && this.size == (target as SerializeArray).size) {
        return indices.all { index -> this[index] completeEquals target[index] }
    } else if (this is SerializeObject && this.size == (target as SerializeObject).size && this.keys == target.keys) {
        return entries.all { (k, v) -> v completeEquals target[k]!! }
    } else return false
}

infix operator fun SerializeArray.contains(target: SerializeElement): Boolean {
    if (contains(target)) return true
    return if (target is SerializeArray) {
        this.containsAll(target)
    } else {
        false
    }
}

infix operator fun SerializeObject.contains(target: SerializeElement): Boolean {
    if (target !is SerializeObject) return false
    return if (keys.containsAll(target.keys)) {
        var result = false
        target.forEach { k, v ->
            result = if (this[k] is SerializeArray) {
                (this[k] as SerializeArray) contains v
            } else if (this[k] is SerializeObject) {
                (this[k] as SerializeObject) contains v
            } else this[k] == v
        }
        result
    } else {
        false
    }
}
