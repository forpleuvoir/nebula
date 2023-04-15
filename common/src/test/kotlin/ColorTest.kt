import com.forpleuvoir.nebula.common.color.Color
import java.util.*

fun main() {
	test()

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
