import moe.forpleuvoir.nebula.common.api.BetaApi
import moe.forpleuvoir.nebula.common.times
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.extensions.SObj
import moe.forpleuvoir.nebula.serialization.extensions.toSerializeObject
import moe.forpleuvoir.nebula.serialization.json.JsonParse
import java.math.BigDecimal
import java.math.BigInteger

fun main() {
    test1()
}

@OptIn(BetaApi::class)
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
              "value": "john.doe@example.com"
            },
            {
              "type": "phone",
              "value": "+1234567890"
            }
          ],
          "notes": " {\"nestedKey\":\"nested\\"Value\"}",
          "nestedJson": {
            "key1": "value1?§aa",
            "key2": "value2"
          },
          "url": "https://maven.forpleuvoir.moe"
        }
	""".trimIndent()
    val obj: SerializeObject
    times {
        obj = JsonParse.parse(json).asObject
    }
    println(obj)
    println(obj["url"]?.asString)
}

fun test3() {
    val o = object {
        var aa = 65
        var bb = "bb"

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
    }
    println(obj.toSerializeObject().toString())
}

fun test2() {
    val a = object : SObj {
        private val test = "aaa"
        val tes2 = 6
        var tes3 = true
        val test4 = 'a'
    }
    println(a.serialize())
    a.tes3 = false
    println(a.serialize())
}



