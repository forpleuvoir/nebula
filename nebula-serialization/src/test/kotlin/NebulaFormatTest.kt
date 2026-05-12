import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.common.color.Colors
import moe.forpleuvoir.nebula.serialization.base.SerializeNull
import moe.forpleuvoir.nebula.serialization.base.SerializeObject
import moe.forpleuvoir.nebula.serialization.base.SerializePrimitive
import moe.forpleuvoir.nebula.serialization.codec.Codec
import moe.forpleuvoir.nebula.serialization.codec.*
import moe.forpleuvoir.nebula.serialization.nebula.NebulaFormat
import moe.forpleuvoir.nebula.serialization.nebula.SerializerRegistry
import moe.forpleuvoir.nebula.serialization.nebula.decode
import moe.forpleuvoir.nebula.serialization.nebula.encode
import moe.forpleuvoir.nebula.serialization.nebula.toKSerializer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Date
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

// region — test data classes

@Serializable
data class SimpleUser(val name: String, val age: Int)

@Serializable
data class NullableUser(val name: String, val age: Int? = null, val nickname: String? = "default")

@Serializable
data class ContextualUser(
    val name: String,
    @Contextual val color: Color,
    @Contextual val duration: Duration,
)

@Serializable
data class WithNested(
    val title: String,
    val user: SimpleUser,
    val tags: List<String>,
)

@Serializable
enum class Level { LOW, MEDIUM, HIGH }

@Serializable
data class WithEnum(val name: String, val level: Level)

@Serializable
data class WithMap(val title: String, val scores: Map<String, Int>)

@Serializable
data class WithDate(val name: String, @Contextual val date: Date)

@Serializable
data class WithNullableColor(val name: String, @Contextual val color: Color? = null)

@Serializable
data class TestStudent(val name: String, val age: Int, @Contextual val color: Color) {
    companion object {
        val CODEC = Codec.create<TestStudent>()
            .field<String>("name").getter(TestStudent::name).default("name").codec(Codec.string)
            .field<Int>("age").getter(TestStudent::age).skipDefault().default(22).codec(Codec.int)
            .field<Color>("color").getter(TestStudent::color).default(Colors.RED).codec(Codec.color)
            .build(::TestStudent)
    }
}

// endregion

class NebulaFormatTest {

    // region — basic round-trip

    @Test
    fun `basic round-trip`() {
        val user = SimpleUser("alice", 30)
        val element = user.encode()
        val decoded = element.decode<SimpleUser>()
        println("basic round-trip: $user → $element → $decoded")
        assertEquals(user, decoded)
    }

    @Test
    fun `nullable field round-trip`() {
        val nullUser = NullableUser("bob", null, null)
        val element = nullUser.encode()
        val decoded = element.decode<NullableUser>()
        println("nullable null: $nullUser → $element → $decoded")
        assertEquals(nullUser, decoded)

        val fullUser = NullableUser("bob", 25, "bobby")
        val element2 = fullUser.encode()
        val decoded2 = element2.decode<NullableUser>()
        println("nullable full: $fullUser → $element2 → $decoded2")
        assertEquals(fullUser, decoded2)
    }

    @Test
    fun `default value present`() {
        val user = NullableUser("charlie", 20)
        val element = user.encode()
        val decoded = element.decode<NullableUser>()
        println("default present: $user → $element → $decoded")
        assertEquals(user.nickname, decoded.nickname)
        assertEquals(user, decoded)
    }

    // endregion

    // region — contextual types

    @Test
    fun `contextual color round-trip`() {
        val user = ContextualUser("david", Colors.ARMY_GREEN, 5.seconds)
        val element = user.encode()
        val decoded = element.decode<ContextualUser>()
        println("contextual color: $user → $element → $decoded")
        assertEquals(user, decoded)
    }

    @Test
    fun `contextual date round-trip`() {
        val wd = WithDate("event", Date())
        val element = wd.encode()
        val decoded = element.decode<WithDate>()
        println("contextual date: $wd → $element → $decoded")
        assertEquals(wd.date.time / 1000, decoded.date.time / 1000)
    }

    @Test
    fun `nullable contextual color`() {
        val empty = WithNullableColor("no-color")
        val element1 = empty.encode()
        val decoded1 = element1.decode<WithNullableColor>()
        println("nullable color(empty): $empty → $element1 → $decoded1")
        assertEquals(empty, decoded1)

        val hasColor = WithNullableColor("with-color", Colors.CHERRY_RED)
        val element2 = hasColor.encode()
        val decoded2 = element2.decode<WithNullableColor>()
        println("nullable color(yes): $hasColor → $element2 → $decoded2")
        assertEquals(hasColor, decoded2)
    }

    // endregion

    // region — list / map / nested

    @Test
    fun `list of primitives`() {
        val list = listOf("x", "y", "z")
        val element = list.encode()
        val decoded = element.decode<List<String>>()
        println("list<String>: $list → $element → $decoded")
        assertEquals(list, decoded)
    }

    @Test
    fun `list of objects`() {
        val list = listOf(SimpleUser("a", 1), SimpleUser("b", 2))
        val element = list.encode()
        val decoded = element.decode<List<SimpleUser>>()
        println("list<SimpleUser>: $list → $element → $decoded")
        assertEquals(list, decoded)
    }

    @Test
    fun `map string to int`() {
        val map = mapOf("a" to 1, "b" to 2)
        val element = map.encode()
        val decoded = element.decode<Map<String, Int>>()
        println("map<String,Int>: $map → $element → $decoded")
        assertEquals(map, decoded)
    }

    @Test
    fun `nested data class`() {
        val nested = WithNested(
            title = "hello",
            user = SimpleUser("eve", 28),
            tags = listOf("kotlin", "serialization"),
        )
        val element = nested.encode()
        val decoded = element.decode<WithNested>()
        println("nested: $nested → $element → $decoded")
        assertEquals(nested, decoded)
    }

    // endregion

    // region — enum

    @Test
    fun `enum round-trip`() {
        val item = WithEnum("test", Level.HIGH)
        val element = item.encode()
        val decoded = element.decode<WithEnum>()
        println("enum: $item → $element → $decoded")
        assertEquals(item, decoded)
    }

    // endregion

    // region — top-level primitives

    @Test
    fun `top-level string`() {
        val element = "hello nebula".encode()
        val decoded = element.decode<String>()
        println("top-level string: \"hello nebula\" → $element → \"$decoded\"")
        assertEquals("hello nebula", decoded)
    }

    @Test
    fun `top-level int`() {
        val element = 42.encode()
        val decoded = element.decode<Int>()
        println("top-level int: 42 → $element → $decoded")
        assertEquals(42, decoded)
    }

    @Test
    fun `top-level boolean`() {
        val element = true.encode()
        val decoded = element.decode<Boolean>()
        println("top-level boolean: true → $element → $decoded")
        assertEquals(true, decoded)
    }

    // endregion

    // region — codec serializer

    @Test
    fun `codec serializer round-trip`() {
        val student = TestStudent("forpleuvoir", 22, Colors.ARMY_GREEN)
        val element = student.encode()
        val decoded = element.decode<TestStudent>()
        println("codec serializer: $student → $element → $decoded")
        assertEquals(student, decoded)
    }

    // endregion

    // region — registry-derived types

    @Test
    fun `int range via registry`() {
        val range = 1..10
        val element = range.encode()
        val decoded = element.decode<IntRange>()
        println("IntRange: $range → $element → $decoded")
        assertEquals(range, decoded)
    }

    @Test
    fun `long range via registry`() {
        val range = 100L..200L
        val element = range.encode()
        val decoded = element.decode<LongRange>()
        println("LongRange: $range → $element → $decoded")
        assertEquals(range, decoded)
    }

    // endregion

    // region — manual SerializeObject decode

    @Test
    fun `decode manually built object`() {
        val obj = SerializeObject().apply {
            this["name"] = SerializePrimitive("manual")
            this["age"] = SerializePrimitive(99)
        }
        val decoded = obj.decode<SimpleUser>()
        println("manual object: $obj → $decoded")
        assertEquals(SimpleUser("manual", 99), decoded)
    }

    @Test
    fun `decode with explicit null`() {
        val obj = SerializeObject().apply {
            this["name"] = SerializePrimitive("test")
            this["age"] = SerializeNull
            this["nickname"] = SerializeNull
        }
        val decoded = obj.decode<NullableUser>()
        println("explicit nulls: $obj → $decoded")
        assertEquals(NullableUser("test", null, null), decoded)
    }

    // endregion

    // region — explicit NebulaFormat

    @Test
    fun `explicit format round-trip`() {
        val user = SimpleUser("explicit", 77)
        val element = NebulaFormat.encodeToElement(user, SimpleUser.serializer())
        val decoded = NebulaFormat.decodeFromElement(element, SimpleUser.serializer())
        println("explicit format: $user → $element → $decoded")
        assertEquals(user, decoded)
    }

    // endregion



    companion object {
        init {
            // Register Codec-based serializer for TestStudent
            SerializerRegistry.register(TestStudent::class, TestStudent.CODEC.toKSerializer())
        }
    }
}
