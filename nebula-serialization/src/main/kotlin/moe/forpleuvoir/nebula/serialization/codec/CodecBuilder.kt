@file:Suppress("UNCHECKED_CAST", "unused")

package moe.forpleuvoir.nebula.serialization.codec

import moe.forpleuvoir.nebula.serialization.base.SerializeElement
import moe.forpleuvoir.nebula.serialization.base.SerializeNull
import moe.forpleuvoir.nebula.serialization.base.SerializeObject

class CodecBuilder<T> internal constructor() {

    internal val fields = mutableListOf<FieldDef<T, *>>()

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder1<T, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder1(this)
        }
    }
}

open class CodecBuilder1<T, A> internal constructor(
    private val builder: CodecBuilder<T>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder2<T, A, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder2(this)
        }
    }

    fun build(ctor: (A) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args -> ctor(args[f[0].name] as A) }
    }
}

open class CodecBuilder2<T, A, B> internal constructor(
    private val builder: CodecBuilder1<T, A>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder3<T, A, B, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder3(this)
        }
    }

    fun build(ctor: (A, B) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args -> ctor(args[f[0].name] as A, args[f[1].name] as B) }
    }
}

open class CodecBuilder3<T, A, B, C> internal constructor(
    private val builder: CodecBuilder2<T, A, B>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder4<T, A, B, C, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder4(this)
        }
    }

    fun build(ctor: (A, B, C) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(args[f[0].name] as A, args[f[1].name] as B, args[f[2].name] as C)
        }
    }
}

open class CodecBuilder4<T, A, B, C, D> internal constructor(
    private val builder: CodecBuilder3<T, A, B, C>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder5<T, A, B, C, D, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder5(this)
        }
    }

    fun build(ctor: (A, B, C, D) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(args[f[0].name] as A, args[f[1].name] as B, args[f[2].name] as C, args[f[3].name] as D)
        }
    }
}

open class CodecBuilder5<T, A, B, C, D, E> internal constructor(
    private val builder: CodecBuilder4<T, A, B, C, D>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun build(ctor: (A, B, C, D, E) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(
                args[f[0].name] as A,
                args[f[1].name] as B,
                args[f[2].name] as C,
                args[f[3].name] as D,
                args[f[4].name] as E,
            )
        }
    }
}
open class CodecBuilder6<T, A, B, C, D, E, F> internal constructor(
    private val builder: CodecBuilder5<T, A, B, C, D, E>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder7<T, A, B, C, D, E, F, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder7(this)
        }
    }

    fun build(ctor: (A, B, C, D, E, F) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(
                args[f[0].name] as A,
                args[f[1].name] as B,
                args[f[2].name] as C,
                args[f[3].name] as D,
                args[f[4].name] as E,
                args[f[5].name] as F
            )
        }
    }
}

open class CodecBuilder7<T, A, B, C, D, E, F, G> internal constructor(
    private val builder: CodecBuilder6<T, A, B, C, D, E, F>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder8<T, A, B, C, D, E, F, G, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder8(this)
        }
    }

    fun build(ctor: (A, B, C, D, E, F, G) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(
                args[f[0].name] as A,
                args[f[1].name] as B,
                args[f[2].name] as C,
                args[f[3].name] as D,
                args[f[4].name] as E,
                args[f[5].name] as F,
                args[f[6].name] as G
            )
        }
    }
}

open class CodecBuilder8<T, A, B, C, D, E, F, G, H> internal constructor(
    private val builder: CodecBuilder7<T, A, B, C, D, E, F, G>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder9<T, A, B, C, D, E, F, G, H, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder9(this)
        }
    }

    fun build(ctor: (A, B, C, D, E, F, G, H) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(
                args[f[0].name] as A,
                args[f[1].name] as B,
                args[f[2].name] as C,
                args[f[3].name] as D,
                args[f[4].name] as E,
                args[f[5].name] as F,
                args[f[6].name] as G,
                args[f[7].name] as H
            )
        }
    }
}

open class CodecBuilder9<T, A, B, C, D, E, F, G, H, I> internal constructor(
    private val builder: CodecBuilder8<T, A, B, C, D, E, F, G, H>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder10<T, A, B, C, D, E, F, G, H, I, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder10(this)
        }
    }

    fun build(ctor: (A, B, C, D, E, F, G, H, I) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(
                args[f[0].name] as A,
                args[f[1].name] as B,
                args[f[2].name] as C,
                args[f[3].name] as D,
                args[f[4].name] as E,
                args[f[5].name] as F,
                args[f[6].name] as G,
                args[f[7].name] as H,
                args[f[8].name] as I
            )
        }
    }
}

open class CodecBuilder10<T, A, B, C, D, E, F, G, H, I, J> internal constructor(
    private val builder: CodecBuilder9<T, A, B, C, D, E, F, G, H, I>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder11<T, A, B, C, D, E, F, G, H, I, J, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder11(this)
        }
    }

    fun build(ctor: (A, B, C, D, E, F, G, H, I, J) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(
                args[f[0].name] as A,
                args[f[1].name] as B,
                args[f[2].name] as C,
                args[f[3].name] as D,
                args[f[4].name] as E,
                args[f[5].name] as F,
                args[f[6].name] as G,
                args[f[7].name] as H,
                args[f[8].name] as I,
                args[f[9].name] as J
            )
        }
    }
}

open class CodecBuilder11<T, A, B, C, D, E, F, G, H, I, J, K> internal constructor(
    private val builder: CodecBuilder10<T, A, B, C, D, E, F, G, H, I, J>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder12<T, A, B, C, D, E, F, G, H, I, J, K, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder12(this)
        }
    }

    fun build(ctor: (A, B, C, D, E, F, G, H, I, J, K) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(
                args[f[0].name] as A,
                args[f[1].name] as B,
                args[f[2].name] as C,
                args[f[3].name] as D,
                args[f[4].name] as E,
                args[f[5].name] as F,
                args[f[6].name] as G,
                args[f[7].name] as H,
                args[f[8].name] as I,
                args[f[9].name] as J,
                args[f[10].name] as K
            )
        }
    }
}

open class CodecBuilder12<T, A, B, C, D, E, F, G, H, I, J, K, L> internal constructor(
    private val builder: CodecBuilder11<T, A, B, C, D, E, F, G, H, I, J, K>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder13<T, A, B, C, D, E, F, G, H, I, J, K, L, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder13(this)
        }
    }

    fun build(ctor: (A, B, C, D, E, F, G, H, I, J, K, L) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(
                args[f[0].name] as A,
                args[f[1].name] as B,
                args[f[2].name] as C,
                args[f[3].name] as D,
                args[f[4].name] as E,
                args[f[5].name] as F,
                args[f[6].name] as G,
                args[f[7].name] as H,
                args[f[8].name] as I,
                args[f[9].name] as J,
                args[f[10].name] as K,
                args[f[11].name] as L
            )
        }
    }
}

open class CodecBuilder13<T, A, B, C, D, E, F, G, H, I, J, K, L, M> internal constructor(
    private val builder: CodecBuilder12<T, A, B, C, D, E, F, G, H, I, J, K, L>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder14<T, A, B, C, D, E, F, G, H, I, J, K, L, M, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder14(this)
        }
    }

    fun build(ctor: (A, B, C, D, E, F, G, H, I, J, K, L, M) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(
                args[f[0].name] as A,
                args[f[1].name] as B,
                args[f[2].name] as C,
                args[f[3].name] as D,
                args[f[4].name] as E,
                args[f[5].name] as F,
                args[f[6].name] as G,
                args[f[7].name] as H,
                args[f[8].name] as I,
                args[f[9].name] as J,
                args[f[10].name] as K,
                args[f[11].name] as L,
                args[f[12].name] as M
            )
        }
    }
}

open class CodecBuilder14<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N> internal constructor(
    private val builder: CodecBuilder13<T, A, B, C, D, E, F, G, H, I, J, K, L, M>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder15<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder15(this)
        }
    }

    fun build(ctor: (A, B, C, D, E, F, G, H, I, J, K, L, M, N) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(
                args[f[0].name] as A,
                args[f[1].name] as B,
                args[f[2].name] as C,
                args[f[3].name] as D,
                args[f[4].name] as E,
                args[f[5].name] as F,
                args[f[6].name] as G,
                args[f[7].name] as H,
                args[f[8].name] as I,
                args[f[9].name] as J,
                args[f[10].name] as K,
                args[f[11].name] as L,
                args[f[12].name] as M,
                args[f[13].name] as N
            )
        }
    }
}

open class CodecBuilder15<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> internal constructor(
    private val builder: CodecBuilder14<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder16<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder16(this)
        }
    }

    fun build(ctor: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(
                args[f[0].name] as A,
                args[f[1].name] as B,
                args[f[2].name] as C,
                args[f[3].name] as D,
                args[f[4].name] as E,
                args[f[5].name] as F,
                args[f[6].name] as G,
                args[f[7].name] as H,
                args[f[8].name] as I,
                args[f[9].name] as J,
                args[f[10].name] as K,
                args[f[11].name] as L,
                args[f[12].name] as M,
                args[f[13].name] as N,
                args[f[14].name] as O
            )
        }
    }
}

open class CodecBuilder16<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P> internal constructor(
    private val builder: CodecBuilder15<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder17<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder17(this)
        }
    }

    fun build(ctor: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(
                args[f[0].name] as A,
                args[f[1].name] as B,
                args[f[2].name] as C,
                args[f[3].name] as D,
                args[f[4].name] as E,
                args[f[5].name] as F,
                args[f[6].name] as G,
                args[f[7].name] as H,
                args[f[8].name] as I,
                args[f[9].name] as J,
                args[f[10].name] as K,
                args[f[11].name] as L,
                args[f[12].name] as M,
                args[f[13].name] as N,
                args[f[14].name] as O,
                args[f[15].name] as P
            )
        }
    }
}

open class CodecBuilder17<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q> internal constructor(
    private val builder: CodecBuilder16<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder18<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder18(this)
        }
    }

    fun build(ctor: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(
                args[f[0].name] as A,
                args[f[1].name] as B,
                args[f[2].name] as C,
                args[f[3].name] as D,
                args[f[4].name] as E,
                args[f[5].name] as F,
                args[f[6].name] as G,
                args[f[7].name] as H,
                args[f[8].name] as I,
                args[f[9].name] as J,
                args[f[10].name] as K,
                args[f[11].name] as L,
                args[f[12].name] as M,
                args[f[13].name] as N,
                args[f[14].name] as O,
                args[f[15].name] as P,
                args[f[16].name] as Q
            )
        }
    }
}

open class CodecBuilder18<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R> internal constructor(
    private val builder: CodecBuilder17<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder19<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder19(this)
        }
    }

    fun build(ctor: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(
                args[f[0].name] as A,
                args[f[1].name] as B,
                args[f[2].name] as C,
                args[f[3].name] as D,
                args[f[4].name] as E,
                args[f[5].name] as F,
                args[f[6].name] as G,
                args[f[7].name] as H,
                args[f[8].name] as I,
                args[f[9].name] as J,
                args[f[10].name] as K,
                args[f[11].name] as L,
                args[f[12].name] as M,
                args[f[13].name] as N,
                args[f[14].name] as O,
                args[f[15].name] as P,
                args[f[16].name] as Q,
                args[f[17].name] as R
            )
        }
    }
}

open class CodecBuilder19<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S> internal constructor(
    private val builder: CodecBuilder18<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder20<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder20(this)
        }
    }

    fun build(ctor: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(
                args[f[0].name] as A,
                args[f[1].name] as B,
                args[f[2].name] as C,
                args[f[3].name] as D,
                args[f[4].name] as E,
                args[f[5].name] as F,
                args[f[6].name] as G,
                args[f[7].name] as H,
                args[f[8].name] as I,
                args[f[9].name] as J,
                args[f[10].name] as K,
                args[f[11].name] as L,
                args[f[12].name] as M,
                args[f[13].name] as N,
                args[f[14].name] as O,
                args[f[15].name] as P,
                args[f[16].name] as Q,
                args[f[17].name] as R,
                args[f[18].name] as S
            )
        }
    }
}

open class CodecBuilder20<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, U> internal constructor(
    private val builder: CodecBuilder19<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder21<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, U, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder21(this)
        }
    }

    fun build(ctor: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, U) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(
                args[f[0].name] as A,
                args[f[1].name] as B,
                args[f[2].name] as C,
                args[f[3].name] as D,
                args[f[4].name] as E,
                args[f[5].name] as F,
                args[f[6].name] as G,
                args[f[7].name] as H,
                args[f[8].name] as I,
                args[f[9].name] as J,
                args[f[10].name] as K,
                args[f[11].name] as L,
                args[f[12].name] as M,
                args[f[13].name] as N,
                args[f[14].name] as O,
                args[f[15].name] as P,
                args[f[16].name] as Q,
                args[f[17].name] as R,
                args[f[18].name] as S,
                args[f[19].name] as U
            )
        }
    }
}

open class CodecBuilder21<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, U, V> internal constructor(
    private val builder: CodecBuilder20<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, U>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun <Val> field(name: String): FieldBuilder<T, Val, CodecBuilder22<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, U, V, Val>> {
        return FieldBuilder(name) { def ->
            fields.add(def)
            CodecBuilder22(this)
        }
    }

    fun build(ctor: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, U, V) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(
                args[f[0].name] as A,
                args[f[1].name] as B,
                args[f[2].name] as C,
                args[f[3].name] as D,
                args[f[4].name] as E,
                args[f[5].name] as F,
                args[f[6].name] as G,
                args[f[7].name] as H,
                args[f[8].name] as I,
                args[f[9].name] as J,
                args[f[10].name] as K,
                args[f[11].name] as L,
                args[f[12].name] as M,
                args[f[13].name] as N,
                args[f[14].name] as O,
                args[f[15].name] as P,
                args[f[16].name] as Q,
                args[f[17].name] as R,
                args[f[18].name] as S,
                args[f[19].name] as U,
                args[f[20].name] as V
            )
        }
    }
}

open class CodecBuilder22<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, U, V, W> internal constructor(
    private val builder: CodecBuilder21<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, U, V>
) {
    internal val fields: MutableList<FieldDef<T, *>> get() = builder.fields

    fun build(ctor: (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, U, V, W) -> T): Codec<T> {
        val f = fields.toList()
        return CodecImpl(f) { args ->
            ctor(
                args[f[0].name] as A,
                args[f[1].name] as B,
                args[f[2].name] as C,
                args[f[3].name] as D,
                args[f[4].name] as E,
                args[f[5].name] as F,
                args[f[6].name] as G,
                args[f[7].name] as H,
                args[f[8].name] as I,
                args[f[9].name] as J,
                args[f[10].name] as K,
                args[f[11].name] as L,
                args[f[12].name] as M,
                args[f[13].name] as N,
                args[f[14].name] as O,
                args[f[15].name] as P,
                args[f[16].name] as Q,
                args[f[17].name] as R,
                args[f[18].name] as S,
                args[f[19].name] as U,
                args[f[20].name] as V,
                args[f[21].name] as W
            )
        }
    }
}


internal class CodecImpl<T>(
    private val fields: List<FieldDef<T, *>>,
    private val construct: (Map<String, Any?>) -> T,
) : Codec<T> {

    @Suppress("UNCHECKED_CAST")
    override fun serialization(target: T): SerializeElement {
        val obj = SerializeObject()
        for (f in fields) {
            val value = f.getter(target)
            if (f.skipNull && value == null) continue
            if (f.skipDefault && f.hasDefault && value == f.default) continue
            val codec = f.codec as Codec<Any?>
            obj[f.name] = codec.serialization(value)
        }
        return obj
    }

    @Suppress("UNCHECKED_CAST")
    override fun deserialization(element: SerializeElement): Result<T> = runCatching {
        val obj = element.asObject!!
        val args = mutableMapOf<String, Any?>()
        for (f in fields) {
            val codec = f.codec as Codec<Any?>
            val hasDefault = f.hasDefault
            val default = f.default
            args[f.name] = if (obj.containsKey(f.name)) {
                val result = codec.deserialization(obj[f.name]!!)
                if (result.isSuccess) result.getOrThrow()
                else if (hasDefault) default
                else throw IllegalArgumentException("failed to deserialize field '${f.name}'")
            } else if (hasDefault) {
                default
            } else {
                codec.deserialization(SerializeNull)
                    .getOrElse { throw IllegalArgumentException("missing required field '${f.name}'") }
            }
        }
        construct(args)
    }
}
