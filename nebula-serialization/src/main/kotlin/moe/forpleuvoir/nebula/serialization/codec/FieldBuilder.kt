package moe.forpleuvoir.nebula.serialization.codec

data class FieldDef<T, V>(
    val name: String,
    val codec: Codec<V>,
    val getter: (T) -> V,
    val default: V?,
    val hasDefault: Boolean,
    val skipDefault: Boolean,
    val skipNull: Boolean,
)

class FieldBuilder<T, V, out Next>(
    private val name: String,
    private val onComplete: (FieldDef<T, V>) -> Next
) {
    private var getter: ((T) -> V)? = null
    private var default: V? = null
    private var hasDefault = false
    private var skipDefault = false
    private var skipNull = false

    fun getter(getter: (T) -> V): FieldBuilder<T, V, Next> {
        this.getter = getter
        return this
    }

    fun default(default: V): FieldBuilder<T, V, Next> {
        hasDefault = true
        this.default = default
        return this
    }

    fun skipDefault(): FieldBuilder<T, V, Next> {
        skipDefault = true
        return this
    }

    fun skipNull(): FieldBuilder<T, V, Next> {
        skipNull = true
        return this
    }

    fun codec(codec: Codec<V>): Next {
        val g = getter ?: error("getter must be set before codec")
        return onComplete(FieldDef(name, codec, g, default, hasDefault, skipDefault, skipNull))
    }

    context(codec: Codec<V>)
    inline val codec get() = codec(codec)

}
