import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.common.color.Colors
import moe.forpleuvoir.nebula.serialization.base.*
import moe.forpleuvoir.nebula.serialization.base.builder.build
import moe.forpleuvoir.nebula.serialization.codec.CODEC
import moe.forpleuvoir.nebula.serialization.codec.Codec
import moe.forpleuvoir.nebula.serialization.codec.deserialization
import moe.forpleuvoir.nebula.serialization.codec.nullable
import moe.forpleuvoir.nebula.serialization.codec.serialization
import moe.forpleuvoir.nebula.serialization.nebula.decode
import moe.forpleuvoir.nebula.serialization.nebula.encode
import moe.forpleuvoir.nebula.serialization.nebula.toKSerializer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds


@Serializable
data class User(val name: String, val age: Int?) {
    companion object {
        val CODEC = Codec.create<User>()
            .field<String>("name").getter(User::name).default("name").codec(Codec.string)
            .field<Int?>("age").getter(User::age).skipNull().codec(Codec.int.nullable())
            .build(::User)
    }
}

class SerializationTest {

    @Serializable
    data class Student(val name: String, val age: Int,@Contextual val color: Color) {
        companion object {
            val CODEC = Codec.create<Student>()
                .field<String>("name").getter(Student::name).default("name").codec(Codec.string)
                .field<Int>("age").getter(Student::age).skipDefault().default(22).codec(Codec.int)
                .field<Color>("color").getter(Student::color).default(Colors.RED).codec(Color.CODEC)
                .build(::Student)
        }
    }

    object StudentSerializer : KSerializer<Student> by Student.CODEC.toKSerializer()

    @Test
    fun testSerializer() {
        val student = Student("forpleuvoir", 22,Colors.ARMY_GREEN)
//        println(NebulaFormat.encodeToElement(student, StudentSerializer))
        println(student.encode())
    }

    @Test
    fun testCodec() {
        context(Student.CODEC) {
            val student = Student("forpleuvoir", 22,Colors.ARMY_GREEN)
            val element = student.serialization
            println(element)
            println(element.deserialization.getOrThrow())

            SerializeObject.build {
                context(Codec.string) {
                    "name" to "forpleuvoir"
                    "age" to "怎么是字符串"
                }
            }.deserialization.let {
                println(it.getOrThrow())
            }
        }
        context(User.CODEC) {
            val user = User("forpleuvoir", null)
            val element = user.serialization
            println(element)
            println(element.deserialization.getOrThrow())
            SerializeObject.build {
                context(Codec.int.nullable(), Codec.string) {
                    "name" to "dhwuia"
                }
            }.let {
                println(it.deserialization.getOrThrow())
            }
        }
    }

    @Test
    fun `round-trip with non-null fields`() {
        val user = User("forpleuvoir", 25)
        val element = user.encode()
        val decoded = element.decode<User>()
        assertEquals(user, decoded)
    }

    @Test
    fun `round-trip with null field`() {
        val user = User("forpleuvoir", null)
        val element = user.encode()
        val decoded = element.decode<User>()
        assertEquals(user, decoded)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun `missing field throws exception`() {
        val obj = SerializeObject().apply {
            this["name"] = SerializePrimitive("Bob")
        }
        org.junit.jupiter.api.assertThrows<kotlinx.serialization.MissingFieldException> {
            obj.decode<User>()
        }
    }

    @Test
    fun `encode produces array for list`() {
        val list = listOf("a", "b", "c")
        val element = list.encode()
        val decoded = element.decode<List<String>>()
        assertEquals(list, decoded)
    }

    @Test
    fun `encode produces object with null for nullable`() {
        val obj = SerializeObject().apply {
            this["name"] = SerializePrimitive("test")
            this["age"] = SerializeNull
        }
        val decoded = obj.decode<User>()
        assertEquals(User("test", null), decoded)
    }

    @Test
    fun `round-trip nested data class`() {
        val team = Team("dev", listOf(User("alice", 30), User("bob", null)), 16.seconds)
        val element = team.encode()
        println(element)
        val decoded = element.decode<Team>()
        assertEquals(team, decoded)
    }

    @Test
    fun `round-trip enum`() {
        val item = Item(Status.ACTIVE, "active item")
        val element = item.encode()
        val decoded = element.decode<Item>()
        assertEquals(item, decoded)
    }

    @Test
    fun `round-trip map string to int`() {
        val map = mapOf("x" to 1, "y" to 2)
        val element = map.encode()
        val decoded = element.decode<Map<String, Int>>()
        assertEquals(map, decoded)
    }

    @Test
    fun `testCodec2`() {
        val user = User("forpleuvoir", null)
        val element = user.encode()
        println(element)
        println(element.decode<User>())
    }
}

@Serializable
enum class Status { ACTIVE, INACTIVE, PENDING }

@Serializable
data class Item(val status: Status, val name: String)

@Serializable
data class Team(val name: String, val members: List<User>, val duration: Duration)

