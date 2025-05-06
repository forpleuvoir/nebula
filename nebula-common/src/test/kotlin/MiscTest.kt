@file:OptIn(ExperimentalTypeInference::class)

import kotlin.experimental.ExperimentalTypeInference
import kotlin.test.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

class MiscTest {


    @Test
    fun t2() {
        println(365.days)
        println(Duration.parse("96d"))
    }

    @Test
    fun t1() {
        val message = "c@s1/time set day"
        val consoleCommandRegex = Regex("c(@([^/]*)/)?(.*)")
        println(message.matches(consoleCommandRegex))
        consoleCommandRegex.find(message)?.let { matchResult ->
            val serverId = matchResult.groupValues[2]
            val cmd = matchResult.groupValues[3]
            println("$serverId:$cmd")
        }
    }

    @Test
    fun t3() {
        val spacing = 1f
        val groupOffset = 0f
        val sizes = listOf(4f, 5f, 5f, 5f, 6f)

        sizes.runningFold(groupOffset) { offset, size ->
            offset + size + spacing
        }.dropLast(1).forEach {
            println(it)
        }

        var offset = groupOffset
        sizes.map { size ->
            val _offset = offset
            offset += size + spacing
            _offset
        }.forEach {
            println(it)
        }
    }

}

fun main() {
    testFun({ 1 })
}

@JvmName("testFunString")
@OverloadResolutionByLambdaReturnType
fun testFun(arg: () -> String, arg2: () -> Boolean = { true }) {
    println(arg())

}

@JvmName("testFunInt")
@OverloadResolutionByLambdaReturnType
fun testFun(arg: () -> Int, arg2: () -> String = { "" }) {
    println(arg())
}

@JvmName("testFun2String")
@OverloadResolutionByLambdaReturnType
fun testFun2(arg: () -> String, arg2: Int = 2) {
    println(arg())

}

@JvmName("testFun2Int")
@OverloadResolutionByLambdaReturnType
fun testFun2(arg: () -> Int, arg2: Int = 2) {
    println(arg())
}