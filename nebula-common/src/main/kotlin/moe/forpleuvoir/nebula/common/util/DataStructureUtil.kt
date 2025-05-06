package moe.forpleuvoir.nebula.common.util

/**
 * 计算满足条件的父级对象数量。
 *
 * @param T 泛型类型，表示当前对象及其父级对象的类型。
 * @param predicate 用于判断当前对象是否满足继续查找父级的条件。如果返回 true，则继续查找；否则停止。
 * @param parentSupplier 提供当前对象的父级对象。如果返回 null，则表示没有更多的父级对象。
 * @param limit 查找父级的最大数量限制，默认为 Int.MAX_VALUE。当达到此限制时，停止查找。
 * @return 返回满足条件的父级对象数量，包括起始对象本身。
 */
inline fun <T> T.countParents(predicate: (T) -> Boolean, parentSupplier: (T) -> T?, limit: Int = Int.MAX_VALUE): Int {
    var count = 0
    var current = this
    while (predicate(current)) {
        val parent = parentSupplier(current)
        if (parent == null || ++count >= limit) {
            break
        } else {
            current = parent
        }
    }
    return count
}


/**
 * 构建从当前对象到根对象的路径列表。
 *
 * @param T 泛型类型，表示当前对象及其父级对象的类型。
 * @param parentSupplier 提供当前对象的父级对象。如果返回 null，则表示没有更多的父级对象。
 * @return 返回一个列表，包含从当前对象到根对象的路径，按从根到当前对象的顺序排列。
 */
inline fun <T> T.pathToRoot(parentSupplier: (T) -> T?): List<T> {
    val path = mutableListOf<T>()
    var currentNode: T? = this
    while (currentNode != null) {
        path.addFirst(currentNode)
        currentNode = parentSupplier(currentNode)
    }
    return path
}