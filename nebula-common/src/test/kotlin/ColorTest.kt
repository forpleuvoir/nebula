import moe.forpleuvoir.nebula.common.color.Color
import moe.forpleuvoir.nebula.common.color.HSVHelper
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileWriter
import kotlin.time.measureTime

class ColorTest {


    @Test
    fun test1() {
        val a = Color.fromHSV(0.75f, 1f, 0.65f, 0.5f)
        val b = Color.fromHSV(1f, 1f, 1f, 1f)
        print(a)
        println(a.rgb)
        print(b)
        println(b.rgb)
        val color: Color = [16, 16, 32]
        print(color)
        println(Color.fromARGB(1f, 1f, 1f) == Color.fromARGB(1f, 1f, 1f))
        HSVHelper.cache.toList().forEach { (k, v) -> println("${k.toString(16)} -> $v -> ${Color.fromHSV(v.hue, v.saturation, v.value)}") }
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