import com.forpleuvoir.nebula.config.Config
import com.forpleuvoir.nebula.config.item.impl.ConfigString
import com.forpleuvoir.nebula.config.item.impl.ConfigStringList
import com.forpleuvoir.nebula.config.item.impl.ConfigStringMap
import com.forpleuvoir.nebula.serialization.extensions.serializeObject
import com.forpleuvoir.nebula.serialization.json.toJsonString
import jdk.incubator.vector.VectorOperators.Test
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.isAccessible

fun main() {
	TestConfigs.init()
	TestConfigs.load()
	TestConfigs.save()
	println(TestConfigs.needSave)
	TestConfigs.Tag1.test.setValue("sdasdasdsadsa")
	println(TestConfigs.needSave)
	TestConfigs.save()
	println(TestConfigs.needSave)
}

fun t() {
	for (memberProperty in T::class.declaredMemberProperties) {
		memberProperty.isAccessible = true
		val delegate = memberProperty.getDelegate(T)
		if (delegate != null) {
			println(delegate::class.isSubclassOf(Config::class))
		}
		println(delegate)
	}
}

object T {
	val str by ConfigString("test", "test")
	val str2 = ConfigString("test2", "test2")
}

val list = ConfigStringList("list", listOf("t1", "t2"))
val map = ConfigStringMap("map", mapOf("k1" to "v1", "k2" to "v2"))


fun test1() {
	println(serializeObject {
		list.key - list.serialization()
		map.key - map.serialization()
	}.toJsonString())
}