import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.common.color.HSVHelper
import org.junit.jupiter.api.Test
import java.awt.Color.HSBtoRGB
import java.io.File
import java.io.FileWriter
import java.util.*
import kotlin.time.measureTime

class ColorTest {


    @Test
    fun test1() {
        val a = Color.fromHSV(1f, 1f, 1f, 0.5f)
        val b = Color.fromHSV(1f, 1f, 1f, 1f)
        print(a)
        println(a.rgb)
        print(b)
        println(b.rgb)
        println(Color.fromARGB(1f, 1f, 1f) == Color.fromARGB(1f, 1f, 1f))
        HSVHelper.cache.toList().forEach { (k, v) -> println("${k.toString(16)} -> $v -> ${Color.fromHSV(v.hue, v.saturation, v.value)}") }
    }

    @Test
    fun test() {
        val randomColor: UInt = ((Math.random() * 0xFFFFFFFFu.toDouble()).toUInt())
        println("随机生成的颜色值：0x${randomColor.toString(16).uppercase(Locale.getDefault())}")

        val isValid = Color.isValidColor(randomColor.toInt())
        if (isValid) {
            println("该颜色值是有效的。${Color.fromARGB(randomColor)}")
        } else {
            println("该颜色值是无效的。")
        }
    }
}


fun map() {
    measureTime {
        val colorNames = File("D:\\workspace\\kotlin\\nebula\\color").readLines()
        val colorCodes = File("D:\\workspace\\kotlin\\nebula\\color2").readLines()
        val colorStrs = ArrayList<String>(colorCodes.size)
        colorNames.forEachIndexed { index, it ->
            val name = it.replace(" ", "_").replace("-", "_").uppercase()
            val color = Color.fromHexString(colorCodes[index])
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