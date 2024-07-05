import moe.forpleuvoir.nebula.common.api.ExperimentalApi
import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.common.color.Colors
import moe.forpleuvoir.nebula.common.util.replace
import moe.forpleuvoir.nebula.serialization.Deserializer
import moe.forpleuvoir.nebula.serialization.annotation.SerializerName
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import moe.forpleuvoir.nebula.serialization.extensions.*
import moe.forpleuvoir.nebula.serialization.gson.parseToJsonObject
import moe.forpleuvoir.nebula.serialization.json.JsonParser
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.ExperimentalReflectionOnLambdas
import kotlin.time.measureTime


class SerializationTest {


    @OptIn(ExperimentalApi::class)
    @Test
    fun test2() {
        println(Deserializer.deserialization<Color>(SerializePrimitive("#66CCFF")))
    }

    @Test
    fun test1() {
        SerializationTest::class.companionObject?.let { clazz ->
            SerializationTest::class.companionObjectInstance!!
            clazz.declaredFunctions.first().let {
                println(it.parameters.size)
                println(it.parameters.last().name)
                println(it.name)
                println(it.returnType.classifier == T::class)
            }
        }
        println()
    }

    data class DT(
        val name: String,
        val list: List<String>,
        val map: Map<String, Any>,
        @SerializerName("ddd")
        val dt2: DT2
    )

    data class DT2(
        @SerializerName("a_name")
        val name: String
    )

    @OptIn(ExperimentalApi::class)
    @Test
    fun test3() {
        Deserializer.deserialization<DT>(serializeObject {
            "name" to "forpleuvoir"
            "list" to serializeArray("aa", "b")
            "map" {
                "test_key" to "test_value"
            }
            "ddd" {
                "a_name" to "Guts"
            }
        }).let {
            println(it)

        }
    }

    @OptIn(ExperimentalApi::class)
    @Test
    fun test4() {
        serializeArray("12", 666, serializeArray("aa", "bb")).deserialization(List::class).apply {
            println(this)
        }
    }

    fun <E : Enum<E>> a(type: KClass<E>) {
    }

}

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
                "test" {
                    "test" to "aa"
                    "aa" {

                    }
                }
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

class DT {
    val a: Int = 10
    val b: Int = 5
}


enum class T {

    V1, V2;

//    companion object {
//        fun deserialization(serializeElement: SerializeElement): T {
//            return T.valueOf(serializeElement.asString)
//        }
//
//    }
}


