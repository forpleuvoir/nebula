import moe.forpleuvoir.nebula.common.times
import moe.forpleuvoir.nebula.serialization.extensions.SObj
import moe.forpleuvoir.nebula.serialization.json.Json
import moe.forpleuvoir.nebula.serialization.json.parseToJsonObject
import moe.forpleuvoir.nebula.serialization.json.toJsonString
import moe.forpleuvoir.nebula.serialization.json.toObject
import moe.forpleuvoir.nebula.serialization.toml.toTomlString
import moe.forpleuvoir.nebula.serialization.yaml.toYamlString

fun main() {
	test2()
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
	val yaml = """
		test: a
		b: true
		c: 123
		aa:
		  - saa
		  - dd
		  - dd
		asd:
		  asdd: dasdas
	""".trimIndent()
	val toml = """
		test = "a"
		b = true
		c = 123
		aa = [ "as", "dd", "dd" ]

		[asd]
		asdd = "dasdas"
	""".trimIndent()
	val jsonObject = json.parseToJsonObject
	val obj = jsonObject.toObject()
	times {
		println("************toml*************")
		println(obj.toTomlString())
		println("************yaml*************")
		println(obj.toYamlString())
		println("************json*************")
		println(obj.toJsonString())
	}

	val a = object : Json {
		val test = ""
	}

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



