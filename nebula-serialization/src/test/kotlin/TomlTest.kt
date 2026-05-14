import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.toml.TomlCommentedEncoder
import moe.forpleuvoir.nebula.serialization.toml.TomlDialect
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TomlTest {

    val basic = """
        title = "TOML Example"
        enabled = true
        count = 42
        score = 3.14
    """.trimIndent()

    val withTable = """
        [server]
        host = "localhost"
        port = 8080

        [server.admin]
        user = "admin"
        pass = "secret"
    """.trimIndent()

    val arrayOfTables = """
        [[products]]
        name = "Hammer"
        sku = 738594937

        [[products]]
        name = "Nail"
        sku = 284758393
    """.trimIndent()

    val inline = """
        item = { name = "Laptop", price = 999.99 }
        tags = [ "sale", "new" ]
    """.trimIndent()

    @Test
    fun testBasic() {
        val decoded = TomlDialect.decode(basic).getOrThrow()
        println("=== basic decoded ===")
        println(decoded)
        val encoded = TomlDialect.encode(decoded)
        println("=== basic encoded ===")
        println(encoded)
        val reDecoded = TomlDialect.decode(encoded).getOrThrow()
        assertEquals(decoded, reDecoded)
    }

    @Test
    fun testTable() {
        val decoded = TomlDialect.decode(withTable).getOrThrow()
        println("=== table decoded ===")
        println(decoded)
        val encoded = TomlDialect.encode(decoded)
        println("=== table encoded ===")
        println(encoded)
        assertEquals(decoded.asObject?.containsKey("server"), true)
        assertEquals(decoded.asObject?.getAsObject("server")?.containsKey("admin"), true)
    }

    @Test
    fun testArrayOfTables() {
        val decoded = TomlDialect.decode(arrayOfTables).getOrThrow()
        println("=== array of tables decoded ===")
        println(decoded)
        val encoded = TomlDialect.encode(decoded)
        println("=== array of tables encoded ===")
        println(encoded)
        val arr = decoded.asObject?.getAsArray("products")
        assertTrue(arr != null && arr.size == 2)
    }

    @Test
    fun testDottedKeys() {
        val input = """
            a.b.c = 1
            x.y.z = "deep"
        """.trimIndent()
        val decoded = TomlDialect.decode(input).getOrThrow()
        println("=== dotted keys ===")
        println(decoded)
        val a = decoded.asObject?.getAsObject("a")
        val b = a?.getAsObject("b")
        assertEquals(1, b?.getAsPrimitive("c")?.asInt)
    }

    @Test
    fun testInline() {
        val decoded = TomlDialect.decode(inline).getOrThrow()
        println("=== inline decoded ===")
        println(decoded)
        val encoded = TomlDialect.encode(decoded)
        println("=== inline encoded ===")
        println(encoded)
    }

    @Test
    fun testCommentedEncoder() {
        val input = """
            title = "TOML Example"
            enabled = true

            [server]
            host = "localhost"
            port = 8080

            [server.logging]
            level = "debug"

            [[products]]
            name = "Hammer"
            sku = 738594937
        """.trimIndent()

        val decode = TomlDialect.decode(input).getOrThrow()

        val comments = mapOf(
            "title" to "Main title",
            "enabled" to "Enable flag",
            "server" to "Server configuration",
            "server.host" to "Host address",
            "server.port" to "Listen port",
            "server.logging" to "Logging settings",
            "products" to "Product list",
        )

        val encoder = object : TomlCommentedEncoder() {
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
    }

    @Test
    fun testNestedCommentForcesExpand() {
        val input = """
            item = { name = "Laptop", price = 999.99, stock = 10 }
        """.trimIndent()

        val decode = TomlDialect.decode(input).getOrThrow()
        println("=== nested comment forced expand ===")
        println("decoded: $decode")

        val comments = mapOf(
            "item" to "商品信息",
            "item.name" to "商品名称",
            "item.price" to "价格",
        )

        val encoder = object : TomlCommentedEncoder() {
            override fun getComment(path: String): String? = comments[path]
        }

        val encoded = encoder.encode(decode)
        println("encoded:")
        println(encoded)

        assertTrue(encoded.contains("[item]"), "Should expand to [item] section")
        assertTrue(encoded.contains("# 商品信息"), "Should have item comment")
        assertTrue(encoded.contains("# 商品名称"), "Should have item.name comment")
        assertTrue(encoded.contains("# 价格"), "Should have item.price comment")
    }

    @Test
    fun testAllTypes() {
        val input = """
            str = "hello"
            int = 42
            float = 3.14
            bool = true
            hex = 0xDEADBEEF
            oct = 0o755
            bin = 0b11010110

            [nested]
            deep = "value"
            deeper.more = 99
        """.trimIndent()
        val decoded = TomlDialect.decode(input).getOrThrow()
        println("=== all types ===")
        println(decoded)
        val encoded = TomlDialect.encode(decoded)
        println("=== encoded ===")
        println(encoded)
        val reDecoded = TomlDialect.decode(encoded).getOrThrow()
        assertEquals(decoded, reDecoded)
    }
}
