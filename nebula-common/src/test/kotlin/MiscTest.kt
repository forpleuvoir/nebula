@file:OptIn(ExperimentalTypeInference::class)

import java.io.File
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

    @Test
    fun t4() {
        var str = "我需要一个字符串长度达到45的字符串,我了给去,这个字符串有多长呢.长达四十五个字符.还多得多得多"

        val existingRanges = mutableListOf<RangeWithCustomData>()

        existingRanges.addAndSplitRanges(RangeWithCustomData(0..str.lastIndex, "0"))
        existingRanges.addAndSplitRanges(RangeWithCustomData(0..25, "A"))
        existingRanges.addAndSplitRanges(RangeWithCustomData(33..45, "B"))
        existingRanges.addAndSplitRanges(RangeWithCustomData(7..22, "D"))

        str = str.insert(33, "这是插入部分")
        existingRanges.insert(33, "这是插入部分".length)

        existingRanges.forEach { println("Range: ${it.range}, Data: ${it.data}") }
        println(str.applyData(existingRanges))
    }

    @Test
    fun file() {
        println("我要创建一个超长文路径的文件夹")
        val fileName = buildString {
            repeat(15) {
                append("超长文件名")
            }
        }
        val folder = File(fileName)
        if (folder.mkdirs()) {
            println("文件夹已成功创建：$fileName")
        } else {
            println("文件夹创建失败，可能路径过长或已存在：$fileName")
        }
    }

}

//context(self: String)
//fun inserts(index: Int, stringToInsert: String): String {
//    require(index in 0..self.length) { "Index out of bounds" }
//    return self.substring(0, index) + stringToInsert + self.substring(index)
//}

fun String.insert(index: Int, stringToInsert: String): String {
    require(index in 0..length) { "Index out of bounds" }
    return substring(0, index) + stringToInsert + substring(index)
}


data class RangeWithCustomData(val range: IntRange, val data: Any)

fun String.applyData(data: List<RangeWithCustomData>): String {
    return buildString {
        data.forEach {
            append("${it.data}(${this@applyData.substring(it.range)})")
        }
    }
}

fun MutableList<RangeWithCustomData>.insert(index: Int, length: Int) {
    this.map { data ->
        RangeWithCustomData(
            range = (data.range.first + if (data.range.first > index) length else 0)..(data.range.last + if (data.range.last > index) length else 0),
            data = data.data
        )
    }.let {
        this.clear()
        this.addAll(it)
    }
}

fun MutableList<RangeWithCustomData>.addAndSplitRanges(newRangeWithCustomData: RangeWithCustomData) {
    if (newRangeWithCustomData.range.last - newRangeWithCustomData.range.first < 1) return
    // 提取所有关键点（起点和终点）
    val points = mutableSetOf<Int>()
    for (range in this) {
        points.add(range.range.first)
        points.add(range.range.last + 1) // 使用 last + 1 表示开区间
    }
    points.add(newRangeWithCustomData.range.first)
    points.add(newRangeWithCustomData.range.last + 1)

    // 去重并排序
    val sortedPoints = points.sorted()

    // 构建结果列表
    val result = mutableListOf<RangeWithCustomData>()

    for (i in 0 until sortedPoints.size - 1) {
        val start = sortedPoints[i]
        val end = sortedPoints[i + 1] - 1 // 转换回闭区间

        if (start > end) continue // 跳过无效范围（如空范围）

        // 判断当前范围属于哪个数据
        val currentRange = start..end
        val overlappingExisting = this.find { it.range.contains(currentRange.first) }
        val isOverlappingNew = newRangeWithCustomData.range.contains(currentRange.first)

        val data = when {
            isOverlappingNew            -> {
                mergeData(overlappingExisting?.data ?: "null", newRangeWithCustomData.data)
            }

            overlappingExisting != null -> overlappingExisting.data
            else                        -> "null"
        }

        result.add(RangeWithCustomData(currentRange, data))
    }

    this.clear()
    this.addAll(result)
}

fun mergeData(data1: Any, data2: Any): Any {
    return if (data1 == "null" && data2 == "null") ""
    else if (data1 == "null") data2
    else if (data2 == "null") data1
    else "$data1+$data2"
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