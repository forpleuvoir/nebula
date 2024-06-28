import moe.forpleuvoir.nebula.common.api.ExperimentalApi
import moe.forpleuvoir.nebula.common.color.Colors
import moe.forpleuvoir.nebula.common.util.replace
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import moe.forpleuvoir.nebula.serialization.extensions.checkType
import moe.forpleuvoir.nebula.serialization.extensions.serializeObject
import moe.forpleuvoir.nebula.serialization.extensions.toSerializeObject
import moe.forpleuvoir.nebula.serialization.gson.parseToJsonObject
import moe.forpleuvoir.nebula.serialization.json.JsonParser
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.jvm.ExperimentalReflectionOnLambdas
import kotlin.time.measureTime

fun main() {
    test3()
}

@OptIn(ExperimentalApi::class)
fun test1() {
    val json = """
		{
          "name": "John Doe",
          "age": 30,
          "address": {
            "street": "123 Main St",
            "city": "Cityville"
          },
          "contacts": [
            {
              "type": "email",
              "value": "john.do\"e@example.com"
            },
            {
              "type": "phone",
              "value": "+1234567890"
            }
          ],
          "notes": " {\"nestedKey\":\"nested\\\"Value\"}",
          "nestedJson": {
            "key1": "value1?§aa",
            "key2": "value2"
          },
          "url": "https://maven.forpleuvoir.moe"
        }
	""".trimIndent()
    val obj: SerializeObject
    measureTime {
        obj = JsonParser.parse(json).asObject
    }.let { println(it) }
//    println(obj)
//    println(obj["notes"]?.asString?.replace(JsonParser.ESCAPE))
    println(JsonParser.parse(obj["notes"]!!.asString.replace(JsonParser.ESCAPE)).asObject["nestedKey"]!!.asString)
    println(json.parseToJsonObject.get("notes").asString)
//    println(obj.dumpAsJson(true))
//    println(obj["contacts"]?.dumpAsJson(true))
}

@OptIn(ExperimentalApi::class, ExperimentalReflectionOnLambdas::class)
fun test3() {
    val o = object {
        var aa = 65
        var bb = "bb"

        fun serialization(): SerializeObject {
            return serializeObject {
                "aa" to aa
                "bb" to bb
            }
        }

        override fun toString(): String {
            return "(aa=$aa, bb='$bb')"
        }

    }

    val obj = object {
        private val a = 65
        val b = 'b'
        val c = "ccc"
        val d = true
        val e = BigInteger.valueOf(4544)
        val f = BigDecimal.valueOf(45.11145)
        val g = null
        val h = arrayOf(6, "c", "asdas", o)
        val j = o
        val color = Colors.BLACK
        val t = T.V1
    }

    SerializePrimitive(10f).checkType<Int>()
        .check<Float> {
            it.toInt()
        }.getOrThrow().let {
            println(it)
        }

//    (serializeArray(1, 2, 3, 4) as SerializeElement)
    obj.toSerializeObject()
        .checkType<SerializeObject, String> {
            it["t"].toString()
        }.getOrThrow().let {
            println(it)
        }
//    o.toSerializeObject().let {
//        println(it.toString())
//    }
//    println(obj.toSerializeObject().dumpAsJson(true))
}

fun test2() {

}


enum class T {
    V1, V2;
}


