import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.common.color.HSVColor
import java.io.File
import java.io.FileWriter
import java.util.*
import kotlin.time.measureTime

fun main() {
    println(Color(0xFF8CECFFu))
}

fun hsv() {
    val c = HSVColor(hue = 123f, saturation = 0.23f, value = 0.66f)
    println(c)
    c.argb = 0xfffff
    println(c)
}

fun test() {
    val randomColor: UInt = ((Math.random() * 0xFFFFFFFFu.toDouble()).toUInt())
    println("随机生成的颜色值：0x${randomColor.toString(16).uppercase(Locale.getDefault())}")

    val isValid = Color.isValidColor(randomColor)
    if (isValid) {
        println("该颜色值是有效的。")
    } else {
        println("该颜色值是无效的。")
    }
}


fun map() {
    measureTime {
        val colorNames = File("D:\\workspace\\kotlin\\nebula\\color").readLines()
        val colorCodes = File("D:\\workspace\\kotlin\\nebula\\color2").readLines()
        val colorStrs = ArrayList<String>(colorCodes.size)
        colorNames.forEachIndexed { index, it ->
            val name = it.replace(" ", "_").replace("-", "_").uppercase()
            val color = Color(Color.decode(colorCodes[index]))
            colorStrs.add("@JvmStatic\n")
            colorStrs.add("val $name : RGBColor get() = Color(${color.red} ,${color.green} ,${color.blue})\n\n")
        }
        val out = File("D:\\workspace\\kotlin\\nebula\\out")
        if (!out.exists()) out.createNewFile()
        FileWriter(out).use { file ->
            colorStrs.forEach {
                file.append(it)
            }
        }
    }.let(::println)
}