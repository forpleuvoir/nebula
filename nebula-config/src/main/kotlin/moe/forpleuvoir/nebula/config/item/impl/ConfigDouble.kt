package moe.forpleuvoir.nebula.config.item.impl

class ConfigDouble(
	key: String,
	defaultValue: Double,
	minValue: Double = Double.MIN_VALUE,
	maxValue: Double = Double.MAX_VALUE
) : ConfigNumber<Double>(key, defaultValue, minValue, maxValue) {

	override val mapping: Number.() -> Double get() = Number::toDouble

}