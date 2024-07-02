@file:OptIn(ExperimentalTypeInference::class)

import kotlin.experimental.ExperimentalTypeInference
import kotlin.test.Test

class MiscTest {


    @Test
    fun t2() {
        println("为什么中文乱码")
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