@file:OptIn(ExperimentalTypeInference::class)

import kotlin.experimental.ExperimentalTypeInference
import kotlin.test.Test

class MiscTest {

    @Test
    fun t1() {

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