import com.forpleuvoir.nebula.config.item.impl.ConfigStringList
import com.forpleuvoir.nebula.config.item.impl.ConfigStringMap
import com.forpleuvoir.nebula.serialization.extensions.serializeObject
import com.forpleuvoir.nebula.serialization.json.toJsonString

fun main() {
	test1()

}


val list = ConfigStringList("list", listOf("t1", "t2"))
val map = ConfigStringMap("map", mapOf("k1" to "v1", "k2" to "v2"))


fun test1() {
	println(serializeObject {
		list.key - list.serialization()
		map.key - map.serialization()
	}.toJsonString())
}