import moe.forpleuvoir.nebula.serialization.base.SerializeArray
import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import moe.forpleuvoir.nebula.serialization.yml.YamlCommentedEncoder
import moe.forpleuvoir.nebula.serialization.yml.YamlDialect
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class YamlTest {

    val basic = """
        title: TOML Example
        enabled: true
        count: 42
        score: 3.14
    """.trimIndent()

    val nested = """
        server:
          host: localhost
          port: 8080
          admin:
            user: admin
            pass: secret
    """.trimIndent()

    val listYaml = """
        products:
          - name: Hammer
            sku: 738594937
          - name: Nail
            sku: 284758393
    """.trimIndent()

    val mixed = """
        item:
          name: Laptop
          price: 999.99
        tags:
          - sale
          - new
    """.trimIndent()

    @Test
    fun testBasic() {
        val decoded = YamlDialect.decode(basic).getOrThrow()
        println("=== basic decoded ===")
        println(decoded)
        val encoded = YamlDialect.encode(decoded)
        println("=== basic encoded ===")
        println(encoded)
        val reDecoded = YamlDialect.decode(encoded).getOrThrow()
        assertEquals(decoded, reDecoded)
    }

    @Test
    fun testNested() {
        val decoded = YamlDialect.decode(nested).getOrThrow()
        println("=== nested decoded ===")
        println(decoded)
        val encoded = YamlDialect.encode(decoded)
        println("=== nested encoded ===")
        println(encoded)
        val reDecoded = YamlDialect.decode(encoded).getOrThrow()
        assertEquals(decoded, reDecoded)
        val server = decoded.asObject?.getAsObject("server")
        assertEquals("localhost", server?.getAsPrimitive("host")?.asString)
        assertEquals(8080, server?.getAsPrimitive("port")?.asInt)
    }

    @Test
    fun testList() {
        val decoded = YamlDialect.decode(listYaml).getOrThrow()
        println("=== list decoded ===")
        println(decoded)
        val encoded = YamlDialect.encode(decoded)
        println("=== list encoded ===")
        println(encoded)
        val reDecoded = YamlDialect.decode(encoded).getOrThrow()
        assertEquals(decoded, reDecoded)
        val products = decoded.asObject?.getAsArray("products")
        assertTrue(products != null && products.size == 2)
        val first = products[0].asObject
        assertEquals("Hammer", first?.getAsPrimitive("name")?.asString)
    }

    @Test
    fun testMixed() {
        val decoded = YamlDialect.decode(mixed).getOrThrow()
        println("=== mixed decoded ===")
        println(decoded)
        val encoded = YamlDialect.encode(decoded)
        println("=== mixed encoded ===")
        println(encoded)
        val reDecoded = YamlDialect.decode(encoded).getOrThrow()
        assertEquals(decoded, reDecoded)
    }

    @Test
    fun testScalarTypes() {
        val input = """
            str: hello world
            int: 42
            float: 3.14
            bool: true
            null_val: null
            quoted: "quoted string"
            single_quoted: 'single quoted'
        """.trimIndent()
        val decoded = YamlDialect.decode(input).getOrThrow()
        println("=== scalars ===")
        println(decoded)
        val encoded = YamlDialect.encode(decoded)
        println("=== encoded ===")
        println(encoded)
        val reDecoded = YamlDialect.decode(encoded).getOrThrow()
        assertEquals(decoded, reDecoded)
    }

    @Test
    fun testCommentedEncoder() {
        val input = """
            title: YAML Example
            enabled: true
            server:
              host: localhost
              port: 8080
              logging:
                level: debug
            products:
              - name: Hammer
                sku: 738594937
        """.trimIndent()

        val decode = YamlDialect.decode(input).getOrThrow()

        val comments = mapOf(
            "title" to "Main title",
            "enabled" to "Enable flag",
            "server" to "Server configuration",
            "server.host" to "Host address",
            "server.port" to "Listen port",
            "server.logging" to "Logging settings",
            "products" to "Product list",
        )

        val encoder = object : YamlCommentedEncoder() {
            override fun getComment(path: String): String? = comments[path]
        }

        val encoded = encoder.encode(decode)
        println("=== commented ===")
        println(encoded)

        assertTrue(encoded.contains("# Main title"), "Missing title comment")
        assertTrue(encoded.contains("# Server configuration"), "Missing server comment")
        assertTrue(encoded.contains("# Host address"), "Missing host comment")
        assertTrue(encoded.contains("# Listen port"), "Missing port comment")
        assertTrue(encoded.contains("# Logging settings"), "Missing logging comment")
        assertTrue(encoded.contains("# Product list"), "Missing products comment")

        assertTrue(encoded.contains("  # Host address"), "Host comment not aligned (expected 2-space indent)")
        assertTrue(encoded.contains("  # Listen port"), "Port comment not aligned (expected 2-space indent)")
        assertTrue(encoded.contains("  # Logging settings"), "Logging comment not aligned (expected 2-space indent)")
    }

    @Test
    fun testEmptyObject() {
        val input = "empty: {}"
        val decoded = YamlDialect.decode(input).getOrThrow()
        println("=== empty object ===")
        println(decoded)
        val encoded = YamlDialect.encode(decoded)
        println("=== encoded ===")
        println(encoded)
        val reDecoded = YamlDialect.decode(encoded).getOrThrow()
        assertEquals(decoded, reDecoded)
    }

    @Test
    fun testEmptyArray() {
        val input = "items: []"
        val decoded = YamlDialect.decode(input).getOrThrow()
        println("=== empty array ===")
        println(decoded)
        val encoded = YamlDialect.encode(decoded)
        println("=== encoded ===")
        println(encoded)
        val reDecoded = YamlDialect.decode(encoded).getOrThrow()
        assertEquals(decoded, reDecoded)
    }
}
