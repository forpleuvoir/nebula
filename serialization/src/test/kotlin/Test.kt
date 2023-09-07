import moe.forpleuvoir.nebula.serialization.extensions.SObj
import moe.forpleuvoir.nebula.serialization.extensions.toSerializeObject
import java.math.BigDecimal
import java.math.BigInteger

fun main() {
    test1()
}

fun test1() {
    val json = """
		{
		  "test": "a",
		  "b": true,
		  "asdas": 2,
		  "c": "1234165.0f",
		  "aa": [
		    {
		      "type":"aa"
		    },
		    {
		      "type":"bb",
		      "cc":"dd"
		    },
		    {
		      "type":"cc"
		    }
		  ],
		  "asd": {
		    "asdd": "dasdas"
		  }
		}
	""".trimIndent()

    val o = object {
        var aa = 65
        var bb = "bb"

        override fun toString(): String {
            return "(aa=$aa, bb='$bb')"
        }

    }

    val obj = object {
        val a = 65
        val b = 'b'
        val c = "ccc"
        val d = true
        val e = BigInteger.valueOf(4544)
        val f = BigDecimal.valueOf(45.11145)
        val g = null
        val h = arrayOf(6,"c","asdas",o)
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



