@file:Suppress("UNUSED", "MemberVisibilityCanBePrivate")

package com.forpleuvoir.nebula.common.color

import com.forpleuvoir.nebula.common.color.Color.Companion.fixValue
import com.forpleuvoir.nebula.common.util.clamp
import com.forpleuvoir.nebula.common.util.fillBefore

class HSBColor(
	hue: Float = 360f,
	saturation: Float = 100f,
	brightness: Float = 100f,
	alpha: Float = 1f,
	private val checkRange: Boolean = true
) {

	constructor(argb: Int, checkRange: Boolean = true) : this(checkRange = checkRange) {
		this.argb = argb
	}

	var argb: Int
		get() {
			val saturation = this.saturation / 100
			val brightness = this.brightness / 100
			val i = ((this.hue / 60) % 6).toInt()
			val f = hue / 60 - i
			val p = brightness * (1F - saturation)
			val q = brightness * (1F - f * saturation)
			val t = brightness * (1F - (1F - f) * saturation)
			val rgb: Array<Float> = when (i) {
				0    -> arrayOf(brightness, t, p)
				1    -> arrayOf(q, brightness, p)
				2    -> arrayOf(p, brightness, t)
				3    -> arrayOf(p, q, brightness)
				4    -> arrayOf(t, p, brightness)
				5    -> arrayOf(brightness, p, q)
				else -> arrayOf(0F, 0F, 0F)
			}.apply {
				for (j in this.indices) {
					this[j] *= 255F
				}
			}
			return ((alpha * 255).toInt() shl 24) or (rgb[0].toInt() shl 16) or (rgb[1].toInt() shl 8) or (rgb[2].toInt() shl 0)
		}
		set(value) {
			val r = value shr 16 and 0xFF
			val g = value shr 8 and 0xFF
			val b = value and 0xFF
			val rgb = arrayOf(r, g, b).apply { sort() }
			val max = rgb[2]
			val min = rgb[0]
			val brightness = max.toFloat() / 255
			val saturation = if (max == 0) 0.0f else 1.0f - (min.toFloat() / max)
			val hue = if (max == min) {
				0.0f
			} else if (max == r && g >= b) {
				60f * ((g - b).toFloat() / (max - min)) + 0.0f
			} else if (max == r) {
				60f * ((g - b).toFloat() / (max - min)) + 360.0f
			} else if (max == g) {
				60f * ((b - r).toFloat() / (max - min)) + 120.0f
			} else if (max == b) {
				60f * ((r - g).toFloat() / (max - min)) + 240.0f
			} else 0.0f
			this.hue = hue.clamp(0, 360)
			this.saturation = saturation * 100.clamp(0, 100)
			this.brightness = brightness * 100.clamp(0, 100)
			this.alpha = (value shr 24 and 0xFF).toFloat() / 255
		}

	var rgb: Int
		get() = argb and 0xFFFFFF
		set(value) {
			argb = argb and 0xFF000000.toInt() or value
		}

	val hexStr: String get() = "#${argb.toUInt().toString(16).fillBefore(8, '0').uppercase()}"

	/**
	 * ?????? Range(0.0F ~ 360.0F)
	 */
	var hue: Float = hue.fixValue(checkRange, "Hue", maxValue = 360F)
		set(value) {
			field = value.fixValue(checkRange, "Hue", maxValue = 360F)
		}

	fun hue(hue: Float): HSBColor = this.apply { this.hue = hue }

	/**
	 * ????????? Range(0.0F ~ 100.0F)
	 */
	var saturation: Float = saturation.fixValue(checkRange, "Saturation", maxValue = 100F)
		set(value) {
			field = value.fixValue(checkRange, "Saturation", maxValue = 100F)
		}

	fun saturation(saturation: Float): HSBColor = this.apply { this.saturation = saturation }

	/**
	 * ?????? Range(0.0F ~ 100.0F)
	 */
	var brightness: Float = brightness.fixValue(checkRange, "Brightness", maxValue = 100F)
		set(value) {
			field = value.fixValue(checkRange, "Brightness", maxValue = 100F)
		}

	fun brightness(brightness: Float): HSBColor = this.apply { this.brightness = brightness }

	/**
	 * ???????????? Range(0.0F ~ 1.0F)
	 */
	var alpha: Float = alpha.fixValue(checkRange, "Alpha")
		set(value) {
			field = value.fixValue(checkRange, "Alpha")
		}

	fun alpha(alpha: Float): HSBColor = this.apply { this.alpha = alpha }

	/**
	 * ???????????????????????????????????????????????????
	 *
	 * ???????????? = ?????????????????? * opacity
	 *
	 * @param opacity [Float] Range(0.0F ~ 1.0F)
	 * @return [HSBColor] ????????????
	 */
	fun opacity(opacity: Float): HSBColor = this.copy().apply { alpha *= opacity.fixValue(checkRange, "Opacity") }

	fun copy(): HSBColor = HSBColor(hue, saturation, brightness, alpha)

	override fun toString(): String {
		return "HSBColor(argb=$argb, hexStr='$hexStr', hue=$hue, saturation=$saturation, brightness=$brightness, alpha=$alpha)"
	}


}

