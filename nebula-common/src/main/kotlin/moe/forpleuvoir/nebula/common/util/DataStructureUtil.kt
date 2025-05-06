package moe.forpleuvoir.nebula.common.util

/**
 * 计算满足条件的父级对象数量。
 *
 * @param T 泛型类型，表示当前对象及其父级对象的类型。
 * @param parentSupplier 提供当前对象的父级对象。如果返回 null，则表示没有更多的父级对象。
 * @return 返回满足条件的父级对象数量，包括起始对象本身。
 */
inline fun <T> T.countParents(parentSupplier: (T) -> T?): Int {
    var count = 0
    var current = this
    while (current != null) {
        val parent = parentSupplier(current)
        if (parent == null) {
            break
        } else {
            ++count
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
inline fun <T> T.pathToRoot(limit: Int = Int.MAX_VALUE, parentSupplier: (T) -> T?): List<T> {
    val path = mutableListOf<T>()
    var currentNode: T? = this
    while (currentNode != null && path.size <= limit) {
        path.addFirst(currentNode)
        currentNode = parentSupplier(currentNode)
    }
    return path
}