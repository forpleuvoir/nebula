import com.forpleuvoir.nebula.common.times
import com.forpleuvoir.nebula.serialization.json.parseToJsonObject
import com.forpleuvoir.nebula.serialization.json.toJsonString
import com.forpleuvoir.nebula.serialization.json.toObject
import com.forpleuvoir.nebula.serialization.toml.toTomlString
import com.forpleuvoir.nebula.serialization.yaml.toYamlString

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

//	println(jsonObject)
//	println(jsonObject.toObject())
//	println(jsonObject.toObject().toYamlString())
//	println(yaml.yamlStringToObject())
}